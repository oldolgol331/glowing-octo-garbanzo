package com.example.demo.domain.ticket.dao

import com.example.demo.domain.concert.model.Concert
import com.example.demo.domain.ticket.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * PackageName : com.example.demo.domain.ticket.dao
 * FileName    : TicketRepository
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 티켓 DAO
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
interface TicketRepository : JpaRepository<Ticket, Long> {

    fun findByConcertAndUserId(concert: Concert, userId: UUID): Ticket?
    fun findByConcert_IdAndUserId(concertId: Long, userId: UUID): Ticket?

}