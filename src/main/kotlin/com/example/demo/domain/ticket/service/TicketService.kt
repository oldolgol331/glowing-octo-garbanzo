package com.example.demo.domain.ticket.service

import com.example.demo.domain.ticket.dto.TicketResponse

/**
 * PackageName : com.example.demo.domain.ticket.service
 * FileName    : TicketService
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 티켓 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
interface TicketService {

    fun issueTicket_lockAop(concertId: Long): TicketResponse
    fun issueTicket_lockTemplate(concertId: Long): TicketResponse

}