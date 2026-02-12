package com.example.demo.common.error

import com.example.demo.common.response.ApiResponse
import com.example.demo.common.response.ErrorCode
import com.example.demo.common.response.ErrorCode.*
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

/**
 * PackageName : com.example.demo.common.error
 * FileName    : GlobalExceptionHandler
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description : 공통 예외 처리 핸들러
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger(this::class.java.name)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleMethodArgumentNotValidException: ${e.message}" }
        return createResponse(INVALID_INPUT_VALUE, e.bindingResult.allErrors.joinToString(", ") { it.defaultMessage })
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleBindException: ${e.message}" }
        return createResponse(INVALID_INPUT_VALUE, e.bindingResult.allErrors.joinToString(", ") { it.defaultMessage })
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleMethodArgumentTypeMismatchException: ${e.message} for property ${e.name}" }
        return createResponse(INVALID_INPUT_VALUE, "요청 인자 '${e.name}'의 타입이 올바르지 않습니다. 예상 타입: ${e.requiredType?.name}")
    }

    @ExceptionHandler(ServletRequestBindingException::class)
    fun handleServletRequestBindingException(e: ServletRequestBindingException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleServletRequestBindingException: ${e.message}" }
        return createResponse(INVALID_INPUT_VALUE, "필수 요청 파라미터 또는 헤더가 누락되었습니다.")
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleHttpRequestMethodNotSupportedException: ${e.message}" }
        return createResponse(
            INVALID_INPUT_VALUE,
            "요청하신 HTTP Method '${e.method}' 는 이 리소스에서 지원되지 않습니다. 지원되는 Method: [${e.supportedHttpMethods?.joinToString(", ")}]"
        )
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(e: HttpMediaTypeNotSupportedException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleHttpMediaTypeNotSupportedException: ${e.message}" }
        return createResponse(
            INVALID_INPUT_VALUE,
            "요청하신 Content-Type '${e.contentType}' 는 지원되지 않습니다. 지원되는 Content-Type: [${
                e.supportedMediaTypes?.joinToString(", ")
            }]"
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleHttpMessageNotReadableException: ${e.message}" }
        return createResponse(INVALID_INPUT_VALUE, "요청하신 JSON 형식이 올바르지 않습니다. JSON 형식을 확인해주세요.")
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleNoResourceFoundException: ${e.message}" }
        return createResponse(RESOURCE_NOT_FOUND)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleEntityNotFoundException: ${e.message}" }
        return createResponse(ENTITY_NOT_FOUND)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleDataIntegrityViolationException: ${e.message}" }
        return createResponse(INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleAccessDeniedException: ${e.message}" }
        return createResponse(ACCESS_DENIED)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleCustomException(e: BusinessException): ResponseEntity<ApiResponse<Unit>> {
        log.warn { "handleCustomException code: ${e.errorCode.name}, message: ${e.message}" }
        return createResponse(e.errorCode, e.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error { "handleException: ${e.message}" }
        return createResponse(INTERNAL_SERVER_ERROR)
    }

    // ========================= 내부 메서드 =========================

    private fun createResponse(errorCode: ErrorCode, message: String? = null): ResponseEntity<ApiResponse<Unit>> =
        ResponseEntity.status(errorCode.status).body(ApiResponse.error(errorCode, message))

}