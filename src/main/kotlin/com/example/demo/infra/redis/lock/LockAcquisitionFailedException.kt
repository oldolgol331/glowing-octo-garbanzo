package com.example.demo.infra.redis.lock

/**
 * PackageName : com.example.demo.infra.redis.lock
 * FileName    : LockAcquisitionFailedException
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
class LockAcquisitionFailedException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)