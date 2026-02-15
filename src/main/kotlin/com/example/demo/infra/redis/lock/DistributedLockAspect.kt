package com.example.demo.infra.redis.lock

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * PackageName : com.example.demo.infra.redis.lock
 * FileName    : DistributedLockAspect
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Aspect
@Component
@Order(LOWEST_PRECEDENCE - 1)
class DistributedLockAspect(
    private val distributedLockTemplate: DistributedLockTemplate
) {

    @Around("@annotation(distributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint, distributedLock: DistributedLock): Any? = distributedLockTemplate.execute(
        CustomSpELParser.getDynamicValue(
            (joinPoint.signature as MethodSignature).parameterNames,
            joinPoint.args,
            distributedLock.key
        ).toString(),
        distributedLock.lockType,
        distributedLock.waitTime,
        distributedLock.leaseTime,
        distributedLock.timeUnit,
        distributedLock.exceptionClass
    ) {
        joinPoint.proceed()
    }

}