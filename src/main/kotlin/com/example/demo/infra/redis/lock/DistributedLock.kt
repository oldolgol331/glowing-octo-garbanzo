package com.example.demo.infra.redis.lock

import com.example.demo.infra.redis.lock.LockType.REENTRANT
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

/**
 * PackageName : com.example.demo.infra.redis.lock
 * FileName    : DistributedLock
 * Author      : oldolgol331
 * Date        : 26. 2. 14.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 14.    oldolgol331          Initial creation
 */
@Target(FUNCTION)
@Retention(RUNTIME)
annotation class DistributedLock(
    val key: String,
    val lockType: LockType = REENTRANT,
    val waitTime: Long = 3L,
    val leaseTime: Long = -1L,
    val timeUnit: TimeUnit = SECONDS,
    val exceptionClass: KClass<out RuntimeException> = LockAcquisitionFailedException::class
)
