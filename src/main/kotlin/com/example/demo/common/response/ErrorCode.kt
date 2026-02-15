package com.example.demo.common.response

import org.springframework.http.HttpStatus

/**
 * PackageName : com.example.demo.common.response
 * FileName    : ErrorCode
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description : 공통 응답 예외 코드
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    // 공통(Common)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "CO001", "유효하지 않은 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "CO002", "유효하지 않은 타입 값입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "CO003", "해당 엔티티를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "CO004", "지원하지 않는 HTTP Method 입니다."),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "CO005", "지원하지 않는 Content-Type 입니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "CO006", "요청 인자의 타입이 올바르지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "CO007", "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "CO008", "인증 정보가 유효하지 않습니다."),
    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "CO009", "데이터베이스 제약 조건 위반입니다."),
    BAD_SQL_GRAMMAR(HttpStatus.INTERNAL_SERVER_ERROR, "CO010", "잘못된 SQL 문법 오류가 발생했습니다."),
    REQUEST_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "CO011", "요청의 크기가 너무 큽니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "CO012", "너무 많은 요청을 보냈습니다. 잠시 후 다시 시도해주세요."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "CO013", "요청하신 리소스를 찾을 수 없습니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "CO014", "필수 입력값이 누락되었습니다."),
    INVALID_CLIENT_IP(HttpStatus.BAD_REQUEST, "CO015", "유효하지 않은 클라이언트 IP입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CO999", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."),

    // 콘서트(Concert)
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "CC001", "해당 콘서트를 찾을 수 없습니다."),
    CONCERT_NAME_DUPLICATED(HttpStatus.CONFLICT, "CC002", "이미 존재하는 콘서트 이름입니다."),

    // 티켓(Ticket)
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "TK001", "해당 티켓을 찾을 수 없습니다."),
    TICKET_ALREADY_RESERVED(HttpStatus.CONFLICT, "TK002", "이미 예약된 티켓입니다."),
    TICKET_MAX_RESERVED(HttpStatus.CONFLICT, "TK003", "최대 예약 갯수에 도달했습니다.")
}