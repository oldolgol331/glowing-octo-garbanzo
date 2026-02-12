package com.example.demo.common.config

import com.example.demo.common.config.formatter.P6SpySqlFormatter
import com.p6spy.engine.spy.P6SpyOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * PackageName : com.example.demo.common.config
 * FileName    : P6SpyConfig
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
@Profile("!prod")
@Configuration
class P6SpyConfig {

    @PostConstruct
    private fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6SpySqlFormatter::class.java.name
    }

}