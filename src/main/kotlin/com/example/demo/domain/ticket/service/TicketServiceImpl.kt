package com.example.demo.domain.ticket.service

import com.example.demo.common.error.BusinessException
import com.example.demo.common.response.ErrorCode.*
import com.example.demo.domain.concert.constant.ConcertConst.LIMIT
import com.example.demo.domain.concert.dao.ConcertQueueRepository
import com.example.demo.domain.concert.dao.ConcertRepository
import com.example.demo.domain.concert.model.ConcertStatus.SOLD_OUT
import com.example.demo.domain.ticket.dao.TicketRepository
import com.example.demo.domain.ticket.dto.TicketQueueResponse
import com.example.demo.domain.ticket.dto.TicketResponse
import com.example.demo.domain.ticket.model.Ticket
import com.example.demo.infra.redis.lock.DistributedLock
import com.example.demo.infra.redis.lock.DistributedLockTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

/**
 * PackageName : com.example.demo.domain.ticket.service
 * FileName    : TicketServiceImpl
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 티켓 서비스 구현
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Service
class TicketServiceImpl(
    private val concertRepository: ConcertRepository,
    private val ticketRepository: TicketRepository,
    private val distributedLockTemplate: DistributedLockTemplate,
    private val concertQueueRepository: ConcertQueueRepository
) : TicketService {

    /**
     * 콘서트에 티켓을 등록한다.
     *
     * @param concertId 콘서트 ID
     * @return          티켓 유저 ID
     */
    @DistributedLock(key = "'concert:' + #concertId", waitTime = 0L, leaseTime = 3L)
    @Transactional
    override fun issueTicket_lockAop(concertId: Long): TicketResponse {
        val concert = concertRepository.findByIdOrNull(concertId) ?: throw BusinessException(CONCERT_NOT_FOUND)
        if (!concert.validateReservation()) throw BusinessException(TICKET_MAX_RESERVED)
        return TicketResponse(
            ticketRepository.save<Ticket>(
                Ticket(
                    concert,
                    UUID.randomUUID(),
                    LocalDateTime.now()
                )
            ).userId.toString()
        )
    }

    /**
     * 콘서트에 티켓을 등록한다.
     *
     * @param concertId 콘서트 ID
     * @return          티켓 유저 ID
     */
    override fun issueTicket_lockTemplate(concertId: Long): TicketResponse =
        distributedLockTemplate.executeWithTransaction(
            lockName = "concert:$concertId",
            waitTime = 0L,
            leaseTime = 3L
        ) {
            val concert = concertRepository.findByIdOrNull(concertId) ?: throw BusinessException(CONCERT_NOT_FOUND)
            if (!concert.validateReservation()) throw BusinessException(TICKET_MAX_RESERVED)
            TicketResponse(
                ticketRepository.save<Ticket>(
                    Ticket(
                        concert,
                        UUID.randomUUID(),
                        LocalDateTime.now()
                    )
                ).userId.toString()
            )
        }

    /**
     * 콘서트 등록 대기열 순서를 반환한다.
     *
     * @param concertId 콘서트 ID
     * @return          콘서트 등록 대기열 순서
     */
    override fun registerQueue(concertId: Long): TicketQueueResponse {
        if (concertQueueRepository.isSoldOut(concertId)) throw BusinessException(TICKET_MAX_RESERVED)

        val userId = UUID.randomUUID()
        val waitingSize = concertQueueRepository.getWaitingSize(concertId)
        val activeSize = concertQueueRepository.getActiveSize(concertId)
        val allowLimit = LIMIT

        if (waitingSize == 0L && activeSize < allowLimit) {
            concertQueueRepository.addToActiveQueue(concertId, userId)
            return TicketQueueResponse(userId.toString(), 0L)
        }

        concertQueueRepository.addToWaitingQueue(concertId, userId)
        val rank = (concertQueueRepository.getRank(concertId, userId) ?: 0L) + 1L

        return TicketQueueResponse(userId.toString(), rank)
    }

    /**
     * 콘서트 등록 대기열을 통과한 유저 ID만 예약한다.
     *
     * @param concertId 콘서트 ID
     * @param userId    유저 ID
     * @return          티켓 유저 ID
     */
    override fun issueTicket(
        concertId: Long,
        userId: UUID
    ): TicketResponse {
        if (concertQueueRepository.isSoldOut(concertId)) throw BusinessException(TICKET_MAX_RESERVED)

        if (!concertQueueRepository.isActive(concertId, userId)) {
            val rank = concertQueueRepository.getRank(concertId, userId)
            throw BusinessException(
                ACCESS_DENIED,
                "Access denied. You are not allowed to reserve tickets for concert $concertId (rank: ${rank?.plus(1) ?: "unknown"})"
            )
        }

        return distributedLockTemplate.executeWithTransaction(
            lockName = "concert:$concertId",
            waitTime = 5L,
            leaseTime = 3L
        ) {
            val concert = concertRepository.findByIdOrNull(concertId) ?: throw BusinessException(CONCERT_NOT_FOUND)

            if (concert.status == SOLD_OUT || !concert.validateReservation())
                throw BusinessException(TICKET_MAX_RESERVED)

            val ticket = ticketRepository.save(Ticket(concert, userId, LocalDateTime.now()))

            val currentIssuedCount = ticketRepository.countByConcertId(concertId)
            if (currentIssuedCount >= concert.maxSeats) {
                concert.status = SOLD_OUT
                concertQueueRepository.setSoldOut(concertId)
                concertQueueRepository.clearWaitingQueue(concertId)
                concertQueueRepository.clearActiveQueue(concertId)
            } else
                concertQueueRepository.removeActiveQueue(concertId, userId)

            TicketResponse(ticket.userId.toString())
        }
    }

}