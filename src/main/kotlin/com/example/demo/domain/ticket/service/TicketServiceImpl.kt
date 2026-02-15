package com.example.demo.domain.ticket.service

import com.example.demo.common.error.BusinessException
import com.example.demo.common.response.ErrorCode.CONCERT_NOT_FOUND
import com.example.demo.common.response.ErrorCode.TICKET_MAX_RESERVED
import com.example.demo.domain.concert.dao.ConcertRepository
import com.example.demo.domain.ticket.dao.TicketRepository
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
    private val distributedLockTemplate: DistributedLockTemplate
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
    @Transactional
    override fun issueTicket_lockTemplate(concertId: Long): TicketResponse =
        distributedLockTemplate.execute(
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

}