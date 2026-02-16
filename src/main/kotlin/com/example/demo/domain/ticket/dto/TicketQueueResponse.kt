package com.example.demo.domain.ticket.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * PackageName : com.example.demo.domain.ticket.dto
 * FileName    : TicketQueueResponse
 * Author      : oldolgol331
 * Date        : 26. 2. 16.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 16.    oldolgol331          Initial creation
 */
@Schema(name = "콘서트 대기 응답 DTO")
data class TicketQueueResponse(
    @field:NotBlank @field:JsonProperty("queue_token") @Schema(description = "대기열 유저 ID 토큰") val userId: String,
    @field:JsonProperty("rank") @Schema(description = "대기열 랭킹") val rank: Long
)