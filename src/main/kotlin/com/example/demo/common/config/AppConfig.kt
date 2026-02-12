package com.example.demo.common.config

import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * PackageName : com.example.demo.common.config
 * FileName    : AppConfig
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@Configuration
class AppConfig {

    @Bean
    fun jackson2ObjectMapperBuilderCustomizer() = Jackson2ObjectMapperBuilderCustomizer { builder ->
        builder.modules(JavaTimeModule())
        builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
    }

}