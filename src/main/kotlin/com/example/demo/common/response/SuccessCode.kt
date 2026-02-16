package com.example.demo.common.response

import org.springframework.http.HttpStatus

/**
 * PackageName : com.example.demo.common.response
 * FileName    : SuccessCode
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description : 공통 응답 성공 코드
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
enum class SuccessCode(
    val status: HttpStatus,
    val message: String
) {
    // 공통(Common)
    REQUEST_SUCCESS(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    CREATE_SUCCESS(HttpStatus.CREATED, "성공적으로 생성되었습니다."),
    UPDATE_SUCCESS(HttpStatus.OK, "성공적으로 업데이트되었습니다."),
    DELETE_SUCCESS(HttpStatus.OK, "성공적으로 삭제되었습니다."),

    // 티켓(Ticket)
    TICKET_QUEUED(HttpStatus.OK, "티켓 대기열에 등록되었습니다."),
    TICKET_ISSUED(HttpStatus.CREATED, "티켓이 등록되었습니다.")
}