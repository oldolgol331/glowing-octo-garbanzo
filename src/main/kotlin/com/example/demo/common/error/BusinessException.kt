package com.example.demo.common.error

import com.example.demo.common.response.ErrorCode

/**
 * PackageName : com.example.demo.common.error
 * FileName    : BusinessException
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description : 커스텀 예외
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
class BusinessException(
    val errorCode: ErrorCode,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message ?: errorCode.message, cause)