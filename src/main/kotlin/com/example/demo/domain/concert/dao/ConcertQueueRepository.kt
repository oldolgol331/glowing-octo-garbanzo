package com.example.demo.domain.concert.dao

import com.example.demo.infra.redis.dao.RedisRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.TimeUnit.MINUTES

/**
 * PackageName : com.example.demo.domain.concert.dao
 * FileName    : ConcertQueueRepository
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Repository
class ConcertQueueRepository(
    private val redisRepository: RedisRepository
) {

    private fun getWaitingKey(concertId: Long) = "queue:waiting:concert:$concertId"
    private fun getActiveKey(concertId: Long) = "queue:active:concert:$concertId"
    private fun getSoldOutKey(concertId: Long) = "concert:soldout:$concertId"

    fun addToWaitingQueue(concertId: Long, userId: UUID) = redisRepository.addToZSetIfAbsent(
        getWaitingKey(concertId),
        userId.toString(),
        System.currentTimeMillis().toDouble()   // Score
    )

    fun getWaitingSize(concertId: Long) = redisRepository.getZSetSize(getWaitingKey(concertId))

    fun clearWaitingQueue(concertId: Long) = redisRepository.deleteData(getWaitingKey(concertId))

    fun popMinFromWaitingQueue(concertId: Long, count: Long) =
        redisRepository.popMinFromZSet(getWaitingKey(concertId), count).map { it.value.toString() }.toSet()

    fun addToActiveQueue(concertId: Long, userId: UUID) {
        val activeKey = getActiveKey(concertId)
        redisRepository.addToSet(activeKey, userId.toString())
        redisRepository.expire(activeKey, 10, MINUTES)
    }

    fun removeActiveQueue(concertId: Long, userId: UUID) =
        redisRepository.removeFromSet(getActiveKey(concertId), userId.toString())

    fun clearActiveQueue(concertId: Long) = redisRepository.deleteData(getActiveKey(concertId))

    fun getActiveSize(concertId: Long) = redisRepository.getSetSize(getActiveKey(concertId))

    fun isActive(concertId: Long, userId: UUID) =
        redisRepository.isSetMember(getActiveKey(concertId), userId.toString())

    fun getRank(concertId: Long, userId: UUID) = redisRepository.getZSetRank(getActiveKey(concertId), userId.toString())

    fun setSoldOut(concertId: Long) = redisRepository.setValue(getSoldOutKey(concertId), "TRUE")

    fun isSoldOut(concertId: Long) = redisRepository.hasKey(getSoldOutKey(concertId))

}