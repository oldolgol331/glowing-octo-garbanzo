package com.example.demo.infra.redis.lock

/**
 * PackageName : com.example.demo.infra.redis.lock
 * FileName    : LockType
 * Author      : oldolgol331
 * Date        : 26. 2. 14.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 14.    oldolgol331          Initial creation
 */
enum class LockType {
    REENTRANT,
    FAIR,
    READ,
    WRITE
}