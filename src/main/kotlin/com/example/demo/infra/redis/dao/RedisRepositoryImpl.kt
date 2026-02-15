package com.example.demo.infra.redis.dao

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.ZSetOperations.TypedTuple
import org.springframework.stereotype.Repository
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * PackageName : com.example.demo.infra.redis.dao
 * FileName    : RedisRepositoryImpl
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : Redis DAO 구현
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Repository
class RedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) : RedisRepository {

    //==================================================
    //== Generic / Key Operations
    //==================================================

    override fun hasKey(key: String) = redisTemplate.hasKey(key) == true

    override fun deleteData(key: String) = redisTemplate.delete(key) == true

    override fun deleteData(keys: Collection<String>) = redisTemplate.delete(keys)

    override fun expire(key: String, duration: Duration) {
        redisTemplate.expire(key, duration)
    }

    override fun expire(key: String, timeout: Long, unit: TimeUnit) {
        redisTemplate.expire(key, timeout, unit)
    }

    override fun getExpire(key: String) = redisTemplate.getExpire(key)

    override fun getExpire(key: String, unit: TimeUnit) = redisTemplate.getExpire(key, unit)

    override fun keys(pattern: String) = redisTemplate.keys(pattern)

    override fun scan(options: ScanOptions): Cursor<String> = redisTemplate.scan(options)

    //==================================================
    //== String (Value) Operations
    //==================================================

    override fun setValue(key: String, value: Any) {
        redisTemplate.opsForValue().set(key, value)
    }

    override fun setValue(key: String, value: Any, offset: Long) {
        redisTemplate.opsForValue().set(key, value, offset)
    }

    override fun setValue(key: String, value: Any, duration: Duration) {
        redisTemplate.opsForValue().set(key, value, duration)
    }

    override fun setValue(key: String, value: Any, timeout: Long, unit: TimeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit)
    }

    override fun setValueIfAbsent(key: String, value: Any) = redisTemplate.opsForValue().setIfAbsent(key, value) == true

    override fun setValueIfAbsent(key: String, value: Any, uration: Duration) =
        redisTemplate.opsForValue().setIfAbsent(key, value, uration) == true

    override fun setValueIfAbsent(key: String, value: Any, timeout: Long, unit: TimeUnit) =
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit) == true

    override fun setValueIfPresent(key: String, value: Any) =
        redisTemplate.opsForValue().setIfPresent(key, value) == true

    override fun setValueIfPresent(key: String, value: Any, duration: Duration) =
        redisTemplate.opsForValue().setIfPresent(key, value, duration) == true

    override fun setValueIfPresent(key: String, value: Any, timeout: Long, unit: TimeUnit) =
        redisTemplate.opsForValue().setIfPresent(key, value, timeout, unit) == true

    override fun multiSetValue(map: Map<String, Any>) {
        redisTemplate.opsForValue().multiSet(map)
    }

    override fun multiSetValueIfAbsent(map: Map<String, Any>) =
        redisTemplate.opsForValue().multiSetIfAbsent(map) == true

    override fun <T> getValue(key: String, type: Class<T>) =
        redisTemplate.opsForValue().get(key)?.let { objectMapper.convertValue(it, type) }

    override fun <T> multiGetValues(keys: Collection<String>, type: Class<T>): List<T> {
        val values = redisTemplate.opsForValue().multiGet(keys)
        if (values.isNullOrEmpty()) return emptyList()
        return values.mapNotNull { value -> value?.let { objectMapper.convertValue(it, type) } }
    }

    override fun increment(key: String) = redisTemplate.opsForValue().increment(key) ?: 0L

    override fun increment(key: String, delta: Long) = redisTemplate.opsForValue().increment(key, delta) ?: 0L

    override fun increment(key: String, delta: Double) = redisTemplate.opsForValue().increment(key, delta) ?: 0.0

    override fun decrement(key: String) = redisTemplate.opsForValue().decrement(key) ?: 0L

    override fun decrement(key: String, delta: Long) = redisTemplate.opsForValue().decrement(key, delta) ?: 0L

    //==================================================
    //== Hash Operations
    //==================================================

    override fun setHash(key: String, hashKey: String, value: Any) {
        redisTemplate.opsForHash<String, Any>().put(key, hashKey, value)
    }

    override fun putAllToHash(key: String, map: Map<String, Any>) {
        redisTemplate.opsForHash<String, Any>().putAll(key, map)
    }

    override fun <T> getHash(key: String, hashKey: String, type: Class<T>) =
        redisTemplate.opsForHash<String, Any>().get(key, hashKey)?.let { objectMapper.convertValue(it, type) }

    override fun <T> multiGetHash(key: String, hashKeys: Collection<String>, type: Class<T>): List<T> {
        val values = redisTemplate.opsForHash<String, Any>().multiGet(key, hashKeys)
        if (values.isEmpty()) return emptyList()
        return values.mapNotNull { value -> value?.let { objectMapper.convertValue(it, type) } }
    }

    override fun getAllHash(key: String) = redisTemplate.opsForHash<Any, Any>().entries(key)

    override fun deleteHash(key: String, vararg hashKeys: Any) =
        redisTemplate.opsForHash<Any, Any>().delete(key, *hashKeys)

    override fun getHashSize(key: String) = redisTemplate.opsForHash<Any, Any>().size(key)

    override fun incrementHash(key: String, hashKey: String, delta: Long) =
        redisTemplate.opsForHash<String, Any>().increment(key, hashKey, delta)

    override fun decrementHash(key: String, hashKey: String, delta: Double) =
        redisTemplate.opsForHash<String, Any>().increment(key, hashKey, delta)

    //==================================================
    //== Set Operations
    //==================================================

    override fun addToSet(key: String, vararg values: Any) = redisTemplate.opsForSet().add(key, *values) ?: 0L

    override fun getSetMembers(key: String) = redisTemplate.opsForSet().members(key) ?: emptySet()

    override fun removeFromSet(key: String, vararg values: Any) = redisTemplate.opsForSet().remove(key, *values) ?: 0L

    override fun getSetSize(key: String) = redisTemplate.opsForSet().size(key) ?: 0L

    override fun isSetMember(key: String, value: Any) = redisTemplate.opsForSet().isMember(key, value) == true

    override fun isSetMember(key: String, vararg values: Any) =
        redisTemplate.opsForSet().isMember(key, *values) ?: emptyMap()

    //==================================================
    //== List Operations
    //==================================================

    override fun leftPushToList(key: String, value: Any) = redisTemplate.opsForList().leftPush(key, value) ?: 0L

    override fun leftPushToList(key: String, pivot: Any, value: Any) =
        redisTemplate.opsForList().leftPush(key, pivot, value) ?: 0L

    override fun leftPushIfPresentToList(key: String, value: Any) =
        redisTemplate.opsForList().leftPushIfPresent(key, value) ?: 0L

    override fun leftPushAllToList(key: String, vararg values: Any) =
        redisTemplate.opsForList().leftPushAll(key, *values) ?: 0L

    override fun leftPushAllToList(key: String, values: Collection<Any>) =
        redisTemplate.opsForList().leftPushAll(key, values) ?: 0L

    override fun rightPushToList(key: String, value: Any) = redisTemplate.opsForList().rightPush(key, value) ?: 0L

    override fun rightPushToList(key: String, pivot: Any, value: Any) =
        redisTemplate.opsForList().rightPush(key, pivot, value) ?: 0L

    override fun rightPushIfPresentToList(key: String, value: Any) =
        redisTemplate.opsForList().rightPushIfPresent(key, value) ?: 0L

    override fun rightPushAllToList(key: String, vararg values: Any) =
        redisTemplate.opsForList().rightPushAll(key, *values) ?: 0L

    override fun rightPushAllToList(key: String, values: Collection<Any>) =
        redisTemplate.opsForList().rightPushAll(key, values) ?: 0L

    override fun <T> leftPopFromList(key: String, type: Class<T>) =
        redisTemplate.opsForList().leftPop(key)?.let { objectMapper.convertValue(it, type) }

    override fun <T> leftPopFromList(key: String, count: Long, type: Class<T>) =
        redisTemplate.opsForList().leftPop(key, count)
            ?.mapNotNull { it?.let { v -> objectMapper.convertValue(v, type) } } ?: emptyList()

    override fun <T> leftPopFromList(key: String, timeout: Duration, type: Class<T>) =
        redisTemplate.opsForList().leftPop(key, timeout)?.let { objectMapper.convertValue(it, type) }

    override fun <T> leftPopFromList(key: String, timeout: Long, unit: TimeUnit, type: Class<T>) =
        redisTemplate.opsForList().leftPop(key, timeout, unit)?.let { objectMapper.convertValue(it, type) }

    override fun <T> rightPopFromList(key: String, type: Class<T>) =
        redisTemplate.opsForList().rightPop(key)?.let { objectMapper.convertValue(it, type) }

    override fun <T> rightPopFromList(key: String, count: Long, type: Class<T>) =
        redisTemplate.opsForList().rightPop(key, count)
            ?.mapNotNull { it?.let { v -> objectMapper.convertValue(v, type) } } ?: emptyList()

    override fun <T> rightPopFromList(key: String, timeout: Duration, type: Class<T>) =
        redisTemplate.opsForList().rightPop(key, timeout)?.let { objectMapper.convertValue(it, type) }

    override fun <T> rightPopFromList(key: String, timeout: Long, unit: TimeUnit, type: Class<T>) =
        redisTemplate.opsForList().rightPop(key, timeout, unit)?.let { objectMapper.convertValue(it, type) }

    override fun getListRange(key: String, start: Long, end: Long) =
        redisTemplate.opsForList().range(key, start, end) ?: emptyList()

    override fun getListSize(key: String) = redisTemplate.opsForList().size(key) ?: 0L

    //==================================================
    //== Sorted Set (ZSet) Operations
    //==================================================

    override fun addToZSet(key: String, value: Any, score: Double) =
        redisTemplate.opsForZSet().add(key, value, score) == true

    @Suppress("UNCHECKED_CAST")
    override fun addToZSet(key: String, tuples: Set<TypedTuple<Any>>) =
        redisTemplate.opsForZSet().add(key, tuples as Set<TypedTuple<Any>>) ?: 0L

    override fun addToZSetIfAbsent(key: String, value: Any, score: Double) =
        redisTemplate.opsForZSet().addIfAbsent(key, value, score) == true

    @Suppress("UNCHECKED_CAST")
    override fun addToZSetIfAbsent(key: String, tuples: Set<TypedTuple<Any>>) =
        redisTemplate.opsForZSet().addIfAbsent(key, tuples as Set<TypedTuple<Any>>) ?: 0L

    override fun incrementZSetScore(key: String, value: Any, delta: Double) =
        redisTemplate.opsForZSet().incrementScore(key, value, delta) ?: 0.0

    override fun removeFromZSet(key: String, vararg values: Any) =
        redisTemplate.opsForZSet().remove(key, *values) ?: 0L

    override fun removeRangeFromZSet(key: String, start: Long, end: Long) =
        redisTemplate.opsForZSet().removeRange(key, start, end) ?: 0L

    override fun getZSetRank(key: String, value: Any) = redisTemplate.opsForZSet().rank(key, value)

    override fun getZSetReverseRank(key: String, value: Any) = redisTemplate.opsForZSet().reverseRank(key, value)

    override fun getZSetRange(key: String, start: Long, end: Long) =
        redisTemplate.opsForZSet().range(key, start, end) ?: emptySet()

    override fun getZSetReverseRange(key: String, start: Long, end: Long) =
        redisTemplate.opsForZSet().reverseRange(key, start, end) ?: emptySet()

    @Suppress("UNCHECKED_CAST")
    override fun getZSetRangeWithScores(key: String, start: Long, end: Long) =
        redisTemplate.opsForZSet().rangeWithScores(key, start, end) as? Set<TypedTuple<Any>> ?: emptySet()

    @Suppress("UNCHECKED_CAST")
    override fun getZSetReverseRangeWithScores(key: String, start: Long, end: Long) =
        redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end) as? Set<TypedTuple<Any>> ?: emptySet()

    override fun getZSetRangeByScore(key: String, min: Double, max: Double) =
        redisTemplate.opsForZSet().rangeByScore(key, min, max) ?: emptySet()

    override fun getZSetReverseRangeByScore(key: String, min: Double, max: Double) =
        redisTemplate.opsForZSet().reverseRangeByScore(key, min, max) ?: emptySet()

    override fun getZSetRangeByScore(key: String, min: Double, max: Double, offset: Long, count: Long) =
        redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count) ?: emptySet()

    override fun getZSetReverseRangeByScore(key: String, min: Double, max: Double, offset: Long, count: Long) =
        redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count) ?: emptySet()

    override fun getZSetSize(key: String) = redisTemplate.opsForZSet().size(key) ?: 0L

    override fun popMinFromZSet(key: String): TypedTuple<Any>? =
        redisTemplate.opsForZSet().popMin(key) as? TypedTuple<Any>

    override fun popMinFromZSet(key: String, count: Long): Set<TypedTuple<Any>> =
        redisTemplate.opsForZSet().popMin(key, count) as? Set<TypedTuple<Any>> ?: emptySet()

    override fun popMaxFromZSet(key: String): TypedTuple<Any>? =
        redisTemplate.opsForZSet().popMax(key) as? TypedTuple<Any>

    override fun popMaxFromZSet(key: String, count: Long): Set<TypedTuple<Any>> =
        redisTemplate.opsForZSet().popMax(key, count) as? Set<TypedTuple<Any>> ?: emptySet()

}