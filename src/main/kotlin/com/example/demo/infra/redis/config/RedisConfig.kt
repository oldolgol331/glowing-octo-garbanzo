package com.example.demo.infra.redis.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * PackageName : com.example.demo.infra.redis.config
 * FileName    : RedisConfig
 * Author      : oldolgol331
 * Date        : 26. 2. 13.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 13.    oldolgol331          Initial creation
 */
@Profile("!test")
@Configuration
class RedisConfig(
    private val objectMapper: ObjectMapper,
    @Value("\${spring.data.redis.host}") private val host: String,
    @Value("\${spring.data.redis.port}") private val port: Int,
    @Value("\${spring.data.redis.password}") private val _password: String?
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(
        RedisStandaloneConfiguration(host, port).apply {
            if (!_password.isNullOrBlank()) this.password = RedisPassword.of(_password)
        }
    )

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            setConnectionFactory(connectionFactory)

            keySerializer = StringRedisSerializer()
            hashKeySerializer = StringRedisSerializer()

            val jsonRedisSerializer = GenericJackson2JsonRedisSerializer(
                objectMapper.copy().apply {
                    activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder().allowIfBaseType(Any::class.java).build(), NON_FINAL
                    )
                }
            )

            valueSerializer = jsonRedisSerializer
            hashValueSerializer = jsonRedisSerializer
        }

    @Bean
    fun redissonClient(): RedissonClient = Redisson.create(
        Config().apply {
            useSingleServer().apply {
                address = "redis://$host:$port"
                connectionPoolSize = 64
                if (!_password.isNullOrBlank()) setPassword(_password)
            }
        }
    )

}