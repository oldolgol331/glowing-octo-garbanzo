package com.example.demo.common.response.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

/**
 * PackageName : com.example.demo.common.response.annotation
 * FileName    : CustomPageResponse
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@Target(FUNCTION)
@Retention(RUNTIME)
annotation class CustomPageResponse(
    val content: Boolean = true,
    val totalElements: Boolean = true,
    val totalPages: Boolean = true,
    val size: Boolean = true,
    val number: Boolean = true,
    val numberOfElements: Boolean = true,
    val sort: Boolean = true,
    val empty: Boolean = true,
    val hasContent: Boolean = true,
    val first: Boolean = true,
    val last: Boolean = true,
    val hasPrevious: Boolean = true,
    val hasNext: Boolean = true
)
