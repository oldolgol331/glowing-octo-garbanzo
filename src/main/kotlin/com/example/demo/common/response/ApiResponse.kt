package com.example.demo.common.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import org.springframework.http.HttpStatus

/**
 * PackageName : com.example.demo.common.response
 * FileName    : ApiResponse
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description : 공통 API 응답
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@JsonInclude(NON_NULL)
data class ApiResponse<T>(
    @JsonIgnore val status: HttpStatus,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> of(status: HttpStatus, message: String, data: T? = null) = ApiResponse(status, message, data)

        fun <T> success(successCode: SuccessCode, data: T? = null) = ApiResponse(
            successCode.status, successCode.message, data
        )

        fun <T> error(errorCode: ErrorCode, message: String? = null, data: T? = null) = ApiResponse(
            errorCode.status, message ?: errorCode.message, data
        )
    }
}
