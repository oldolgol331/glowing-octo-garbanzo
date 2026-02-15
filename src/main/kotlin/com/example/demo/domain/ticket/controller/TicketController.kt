package com.example.demo.domain.ticket.controller

import com.example.demo.common.response.ApiResponse
import com.example.demo.common.response.SuccessCode.TICKET_ISSUED
import com.example.demo.domain.ticket.dto.TicketResponse
import com.example.demo.domain.ticket.service.TicketService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * PackageName : com.example.demo.domain.ticket.controller
 * FileName    : TicketController
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 티켓 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "티켓 API", description = "티켓을 예약할 수 있는 API를 제공한다.")
class TicketController(
    private val ticketService: TicketService
) {

    @PostMapping("/aop/{concertId}")
    @Operation(summary = "콘서트에 티켓을 등록한다.")
    fun issueTicket_lockAop(@PathVariable concertId: Long): ApiResponse<TicketResponse> =
        ApiResponse.success(TICKET_ISSUED, ticketService.issueTicket_lockAop(concertId))

    @PostMapping("/template/{concertId}")
    @Operation(summary = "콘서트에 티켓을 등록한다.")
    fun issueTicket_lockTemplate(@PathVariable concertId: Long): ApiResponse<TicketResponse> =
        ApiResponse.success(TICKET_ISSUED, ticketService.issueTicket_lockTemplate(concertId))

}