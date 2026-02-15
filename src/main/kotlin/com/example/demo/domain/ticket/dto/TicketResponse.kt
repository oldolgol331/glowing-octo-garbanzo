package com.example.demo.domain.ticket.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * PackageName : com.example.demo.domain.ticket.dto
 * FileName    : TicketResponse
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 티켓 응답 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Schema(name = "티켓 응답 DTO")
data class TicketResponse(
    @field:NotBlank @field:JsonProperty("user_id") @Schema(description = "티켓 유저 ID") val userId: String
)
