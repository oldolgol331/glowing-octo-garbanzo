package com.example.demo.infra.redis.dao

import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.ZSetOperations.TypedTuple
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * PackageName : com.example.demo.infra.redis.dao
 * FileName    : RedisRepository
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : Redis DAO
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
interface RedisRepository {

    //==================================================
    //== Generic / Key Operations
    //==================================================

    fun hasKey(key: String): Boolean
    fun deleteData(key: String): Boolean
    fun deleteData(keys: Collection<String>): Long
    fun expire(key: String, duration: Duration)
    fun expire(key: String, timeout: Long, unit: TimeUnit)
    fun getExpire(key: String): Long
    fun getExpire(key: String, unit: TimeUnit): Long
    fun keys(pattern: String): Set<String>
    fun scan(options: ScanOptions): Cursor<String>

    //==================================================
    //== String (Value) Operations
    //==================================================

    fun setValue(key: String, value: Any)
    fun setValue(key: String, value: Any, offset: Long)
    fun setValue(key: String, value: Any, duration: Duration)
    fun setValue(key: String, value: Any, timeout: Long, unit: TimeUnit)
    fun setValueIfAbsent(key: String, value: Any): Boolean
    fun setValueIfAbsent(key: String, value: Any, duration: Duration): Boolean
    fun setValueIfAbsent(key: String, value: Any, timeout: Long, unit: TimeUnit): Boolean
    fun setValueIfPresent(key: String, value: Any): Boolean
    fun setValueIfPresent(key: String, value: Any, duration: Duration): Boolean
    fun setValueIfPresent(key: String, value: Any, timeout: Long, unit: TimeUnit): Boolean
    fun multiSetValue(map: Map<String, Any>)
    fun multiSetValueIfAbsent(map: Map<String, Any>): Boolean
    fun <T> getValue(key: String, type: Class<T>): T?
    fun <T> multiGetValues(keys: Collection<String>, type: Class<T>): List<T>
    fun increment(key: String): Long
    fun increment(key: String, delta: Long): Long
    fun increment(key: String, delta: Double): Double
    fun decrement(key: String): Long
    fun decrement(key: String, delta: Long): Long

    //==================================================
    //== Hash Operations
    //==================================================

    fun setHash(key: String, hashKey: String, value: Any)
    fun putAllToHash(key: String, map: Map<String, Any>)
    fun <T> getHash(key: String, hashKey: String, type: Class<T>): T?
    fun <T> multiGetHash(key: String, hashKeys: Collection<String>, type: Class<T>): List<T>
    fun getAllHash(key: String): Map<Any, Any>
    fun deleteHash(key: String, vararg hashKeys: Any): Long
    fun getHashSize(key: String): Long
    fun incrementHash(key: String, hashKey: String, delta: Long): Long
    fun decrementHash(key: String, hashKey: String, delta: Double): Double

    //==================================================
    //== Set Operations
    //==================================================

    fun addToSet(key: String, vararg values: Any): Long
    fun getSetMembers(key: String): Set<Any>
    fun removeFromSet(key: String, vararg values: Any): Long
    fun getSetSize(key: String): Long
    fun isSetMember(key: String, value: Any): Boolean
    fun isSetMember(key: String, vararg values: Any): Map<Any, Boolean>

    //==================================================
    //== List Operations
    //==================================================

    fun leftPushToList(key: String, value: Any): Long
    fun leftPushToList(key: String, pivot: Any, value: Any): Long
    fun leftPushIfPresentToList(key: String, value: Any): Long
    fun leftPushAllToList(key: String, vararg values: Any): Long
    fun leftPushAllToList(key: String, values: Collection<Any>): Long
    fun rightPushToList(key: String, value: Any): Long
    fun rightPushToList(key: String, pivot: Any, value: Any): Long
    fun rightPushIfPresentToList(key: String, value: Any): Long
    fun rightPushAllToList(key: String, vararg values: Any): Long
    fun rightPushAllToList(key: String, values: Collection<Any>): Long
    fun <T> leftPopFromList(key: String, type: Class<T>): T?
    fun <T> leftPopFromList(key: String, count: Long, type: Class<T>): List<T>
    fun <T> leftPopFromList(key: String, timeout: Duration, type: Class<T>): T?
    fun <T> leftPopFromList(key: String, timeout: Long, unit: TimeUnit, type: Class<T>): T?
    fun <T> rightPopFromList(key: String, type: Class<T>): T?
    fun <T> rightPopFromList(key: String, count: Long, type: Class<T>): List<T>
    fun <T> rightPopFromList(key: String, timeout: Duration, type: Class<T>): T?
    fun <T> rightPopFromList(key: String, timeout: Long, unit: TimeUnit, type: Class<T>): T?
    fun getListRange(key: String, start: Long, end: Long): List<Any>
    fun getListSize(key: String): Long

    //==================================================
    //== Sorted Set (ZSet) Operations
    //==================================================

    fun addToZSet(key: String, value: Any, score: Double): Boolean
    fun addToZSet(key: String, tuples: Set<TypedTuple<Any>>): Long
    fun addToZSetIfAbsent(key: String, value: Any, score: Double): Boolean
    fun addToZSetIfAbsent(key: String, tuples: Set<TypedTuple<Any>>): Long
    fun incrementZSetScore(key: String, value: Any, delta: Double): Double
    fun removeFromZSet(key: String, vararg values: Any): Long
    fun removeRangeFromZSet(key: String, start: Long, end: Long): Long
    fun getZSetRank(key: String, value: Any): Long?
    fun getZSetReverseRank(key: String, value: Any): Long?
    fun getZSetRange(key: String, start: Long, end: Long): Set<Any>
    fun getZSetReverseRange(key: String, start: Long, end: Long): Set<Any>
    fun getZSetRangeWithScores(key: String, start: Long, end: Long): Set<TypedTuple<Any>>
    fun getZSetReverseRangeWithScores(key: String, start: Long, end: Long): Set<TypedTuple<Any>>
    fun getZSetRangeByScore(key: String, min: Double, max: Double): Set<Any>
    fun getZSetReverseRangeByScore(key: String, min: Double, max: Double): Set<Any>
    fun getZSetRangeByScore(key: String, min: Double, max: Double, offset: Long, count: Long): Set<Any>
    fun getZSetReverseRangeByScore(key: String, min: Double, max: Double, offset: Long, count: Long): Set<Any>
    fun getZSetSize(key: String): Long
    fun popMinFromZSet(key: String): TypedTuple<Any>?
    fun popMinFromZSet(key: String, count: Long): Set<TypedTuple<Any>>
    fun popMaxFromZSet(key: String): TypedTuple<Any>?
    fun popMaxFromZSet(key: String, count: Long): Set<TypedTuple<Any>>

}