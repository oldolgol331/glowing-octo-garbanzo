package com.example.demo.infra.redis.lock

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * PackageName : com.example.demo.infra.redis.lock
 * FileName    : CustomSpELParser
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
object CustomSpELParser {
    fun getDynamicValue(parameterNames: Array<String>, args: Array<Any>, key: String): Any? {
        val parser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        for (i in parameterNames.indices) context.setVariable(parameterNames[i], args[i])

        return parser.parseExpression(key).getValue(context, Any::class.java)
    }
}