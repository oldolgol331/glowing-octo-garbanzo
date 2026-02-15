package com.example.demo.domain.ticket.model

import com.example.demo.common.error.BusinessException
import com.example.demo.common.model.BaseAuditingEntity
import com.example.demo.common.response.ErrorCode.TICKET_MAX_RESERVED
import com.example.demo.domain.concert.model.Concert
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDateTime
import java.util.*

/**
 * PackageName : com.example.demo.domain.ticket.model
 * FileName    : Ticket
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 티켓 모델
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Entity
@Table(
    name = "tickets",
    uniqueConstraints = [UniqueConstraint(
        name = "uq_tickets_concert_id_user_id",
        columnNames = ["concert_id", "user_id"]
    )]
)
class Ticket(
    concert: Concert,
    @Column(name = "user_id", nullable = false, updatable = false) val userId: UUID,
    @Column(name = "issued_at", nullable = false, updatable = false) val issuedAt: LocalDateTime
) : BaseAuditingEntity() {

    init {
        setRelationshipWithConcert(concert)
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ticket_id", nullable = false, updatable = false)
    val id: Long = 0L

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "concert_id", nullable = false, updatable = false)
    var concert: Concert? = null
        protected set

    // ========================= 연관관계 메서드 =========================

    /**
     * 콘서트에 연관관계를 설정한다.
     *
     * @param input 콘서트
     */
    private fun setRelationshipWithConcert(input: Concert) {
        if (!input.validateReservation()) throw BusinessException(TICKET_MAX_RESERVED);
        concert = input
        input.tickets.add(this)
    }

}