package com.example.demo.infra.redis.lock

import com.example.demo.infra.redis.lock.LockType.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * PackageName : com.example.demo.infra.redis.lock
 * FileName    : DistributedLockTemplate
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Component
class DistributedLockTemplate(
    private val redissonClient: RedissonClient
) {

    private val log = KotlinLogging.logger {}

    /**
     * 락을 사용하여 블록 실행한다.
     *
     * @param lockName       락의 이름
     * @param lockType       락의 타입 (REENTRANT, FAIR, READ, WRITE)
     * @param waitTime       락 잠금 시간
     * @param leaseTime      락 유지 시간 (-1이면 무제한)
     * @param timeUnit       시간 단위
     * @param exceptionClass 락 획득 실패 시 발생할 예외 클래스
     * @param block          실행할 블록
     * @return               블록 실행 결과
     */
    fun <T> execute(
        lockName: String,
        lockType: LockType = REENTRANT,
        waitTime: Long = 3L,
        leaseTime: Long = -1L,
        timeUnit: TimeUnit = SECONDS,
        exceptionClass: KClass<out RuntimeException> = LockAcquisitionFailedException::class,
        block: () -> T
    ): T {
        validateLockName(lockName)

        val rLock = getLockByType(lockName, lockType)
        var lockAcquired = false

        try {
            lockAcquired = if (leaseTime == -1L) rLock.tryLock(waitTime, timeUnit)
            else rLock.tryLock(waitTime, leaseTime, timeUnit)

            if (!lockAcquired) {
                log.warn { "Failed to acquire redisson lock - Key: $lockName, Type: $lockType" }
                throw createException(exceptionClass, "Lock not available: $lockName")
            }

            log.debug { "Successfully acquired redisson lock - Key: $lockName, Type: $lockType" }

            return block()
        } catch (e: InterruptedException) {
            log.error(e) { "Redisson lock was interrupted - Key: $lockName" }
            Thread.currentThread().interrupt()
            throw IllegalStateException("Redisson lock was interrupted", e)
        } finally {
            try {
                if (rLock.isLocked && rLock.isHeldByCurrentThread) {
                    if (TransactionSynchronizationManager.isActualTransactionActive())
                        registerUnlockAfterTransactionCompletion(rLock, lockName)
                    else {
                        rLock.unlock()
                        log.debug { "Released redisson lock - Key: $lockName" }
                    }
                } else
                    log.warn { "Attempted to unlock a lock that is not held by current thread - Key: $lockName, isLocked: ${rLock.isLocked}, isHeldByCurrentThread: ${rLock.isHeldByCurrentThread}" }
            } catch (e: Exception) {
                log.error(e) { "Redisson lock was already released - Key: $lockName" }
            }
        }
    }

    // ========================= 내부 메서드 =========================

    /**
     * 락 이름에 올바르지 않은 문자가 포함되었는지 검사한다.
     *
     * @param lockName 락 이름
     */
    private fun validateLockName(lockName: String) {
        if (lockName.isBlank()) throw IllegalArgumentException("Lock name cannot be blank")

        if (!lockName.matches(Regex("^[a-zA-Z0-9\\-_.:]+$"))) {
            log.warn { "Invalid lock name detected: $lockName" }
            throw IllegalArgumentException("Invalid lock name: $lockName")
        }

        if (lockName.length > 255) throw IllegalArgumentException("Lock name too long: ${lockName.length} chars")
    }

    /**
     * 락 타입에 따라 적절한 락 인스턴스를 반환한다.
     *
     * @param key  락 이름
     * @param type 락 타입
     * @return     락 인스턴스
     */
    private fun getLockByType(key: String, type: LockType): RLock = when (type) {
        REENTRANT -> redissonClient.getLock(key)
        FAIR -> redissonClient.getFairLock(key)
        READ -> redissonClient.getReadWriteLock(key).readLock()
        WRITE -> redissonClient.getReadWriteLock(key).writeLock()
    }

    /**
     * 예외 클래스에 따라 적절한 예외를 생성한다.
     *
     * @param exceptionClass 예외 클래스
     * @param message        예외 메시지
     * @return               예외
     */
    private fun createException(
        exceptionClass: KClass<out RuntimeException>,
        message: String
    ): RuntimeException = try {
        exceptionClass.primaryConstructor?.call(message)
            ?: exceptionClass.java.getConstructor(String::class.java).newInstance(message)
    } catch (e: Exception) {
        log.error { "Failed to create exception: ${exceptionClass.simpleName}" }
        LockAcquisitionFailedException(message, e)
    }

    /**
     * 트랜잭션 커밋/롤백이 완료된 직후 락을 해제한다.
     *
     * @param rLock    락 인스턴스
     * @param lockName 락 이름
     */
    private fun registerUnlockAfterTransactionCompletion(rLock: RLock, lockName: String) {
        log.info { "Detection of lock usage inside the transaction - Delays release time to after transaction completion, Key: $lockName" }

        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCompletion(status: Int) {
                try {
                    if (rLock.isLocked && rLock.isHeldByCurrentThread) {
                        rLock.unlock()
                        log.debug { "After transaction completion, released redisson lock - Key: $lockName" }
                    }
                } catch (e: Exception) {
                    log.error(e) { "Error unlocking redisson lock after transaction completion - Key: $lockName" }
                }
            }
        })
    }

}