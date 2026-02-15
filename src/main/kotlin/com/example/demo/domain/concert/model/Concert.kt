package com.example.demo.domain.concert.model

import com.example.demo.common.model.BaseAuditingEntity
import com.example.demo.domain.ticket.model.Ticket
import jakarta.persistence.*
import jakarta.persistence.CascadeType.REMOVE
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.validation.constraints.Min
import java.time.LocalDateTime

/**
 * PackageName : com.example.demo.domain.concert.model
 * FileName    : Concert
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 콘서트 모델
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Entity
@Table(name = "concerts")
class Concert(
    @Column(name = "name", nullable = false, unique = true) var name: String,
    @Column(name = "date", nullable = false) var date: LocalDateTime,
    @Column(name = "max_seats", nullable = false) @Min(1) var maxSeats: Int
) : BaseAuditingEntity() {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "concert_id", nullable = false, updatable = false)
    val id: Long = 0L

    @OneToMany(mappedBy = "concert", cascade = [REMOVE], orphanRemoval = true)
    val tickets: MutableList<Ticket> = mutableListOf()

    // ========================= 비즈니스 메서드 =========================

    /**
     * 예약 갯수가 최대 예약 갯수를 넘었는지 확인한다.
     */
    fun validateReservation() = tickets.size < maxSeats

}