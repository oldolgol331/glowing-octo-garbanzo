package com.example.demo.common.response.advice

import com.example.demo.common.response.ApiResponse
import com.example.demo.common.response.annotation.CustomPageResponse
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * PackageName : com.example.demo.common.response.advice
 * FileName    : PageResponseAdvice
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@RestControllerAdvice
class PageResponseAdvice : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>?>
    ) = returnType.hasMethodAnnotation(CustomPageResponse::class.java)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>?>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val annotation = returnType.getMethodAnnotation(CustomPageResponse::class.java) ?: return body
        return when (body) {
            is Page<*> -> body.toCustomMap(annotation)
            is ApiResponse<*> -> handleApiResponse(body, annotation)
            else -> body
        }
    }

    /**
     * API 응답 데이터를 커스텀 페이지 데이터로 변환한다.
     *
     * @param apiResponse   API 응답 데이터
     * @param annotation    커스텀 페이지 데이터 어노테이션
     * @return 커스텀 페이지 데이터
     */
    private fun handleApiResponse(apiResponse: ApiResponse<*>, annotation: CustomPageResponse): Any {
        val data = apiResponse.data
        return if (data is Page<*>) ApiResponse.of(
            apiResponse.status,
            apiResponse.message,
            data.toCustomMap(annotation)
        )
        else apiResponse
    }

    fun Page<*>.toCustomMap(annotation: CustomPageResponse): Map<String, Any> = buildMap {
        if (annotation.content) put("content", content)
        if (annotation.totalElements) put("total_elements", totalElements)
        if (annotation.totalPages) put("total_pages", totalPages)
        if (annotation.size) put("size", size)
        if (annotation.number) put("number", number)
        if (annotation.numberOfElements) put("number_of_elements", numberOfElements)
        if (annotation.sort) put("sort", sort)
        if (annotation.empty) put("empty", isEmpty)
        if (annotation.hasContent) put("has_content", hasContent())
        if (annotation.first) put("first", isFirst)
        if (annotation.last) put("last", isLast)
        if (annotation.hasPrevious) put("has_previous", hasPrevious())
        if (annotation.hasNext) put("has_next", hasNext())
    }

}