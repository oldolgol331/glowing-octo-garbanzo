package com.example.demo.common.config.formatter

import com.p6spy.engine.logging.Category.STATEMENT
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle.BASIC
import org.hibernate.engine.jdbc.internal.FormatStyle.DDL

/**
 * PackageName : com.example.demo.common.config.formatter
 * FileName    : P6SpySqlFormatter
 * Author      : oldolgol331
 * Date        : 26. 2. 12.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 12.    oldolgol331          Initial creation
 */
class P6SpySqlFormatter : MessageFormattingStrategy {

    companion object {
        private const val CREATE = "create"
        private const val ALTER = "alter"
        private const val COMMENT = "comment"
        private const val PACKAGE = "io.p6spy"
    }

    override fun formatMessage(
        connectionId: Int,
        now: String?,
        elapsed: Long,
        category: String?,
        prepared: String?,
        sql: String?,
        url: String?
    ) = sqlFormatToUpper(sql, category, getMessage(connectionId, elapsed, getStackBuilder()))

    private fun sqlFormatToUpper(sql: String?, category: String?, message: String) =
        if (sql.isNullOrBlank()) ""
        else buildString {
            appendLine()
            append(sqlFormatToUpper(sql, category))
            append(message)
        }

    private fun sqlFormatToUpper(sql: String, category: String?) =
        if (isStatementDDL(sql, category)) DDL.formatter.format(sql).uppercase().replace("+0900", "").trim()
        else BASIC.formatter.format(sql).uppercase().replace("+0900", "").trim()

    private fun isStatementDDL(sql: String, category: String?) = isStatement(category) && isDDL(sql.trim().lowercase())

    private fun isStatement(category: String?) = STATEMENT.name == category

    private fun isDDL(lowerSql: String) =
        lowerSql.startsWith(CREATE) || lowerSql.startsWith(ALTER) || lowerSql.startsWith(COMMENT)

    private fun getMessage(connectionId: Int, elapsed: Long, callStackBuilder: CharSequence) = """
        |
        |   Connection ID: $connectionId
        |   Execution time: $elapsed ms
        |
        |   Call Stack (number 1 is entry point): $callStackBuilder
        |
        |---------------------------------------------------------------------------------------------------------------------
    """.trimMargin()

    private fun getStackBuilder() = Throwable().stackTrace
        .asSequence()
        .map { it.toString() }
        .filter { isLoggable(it) }
        .toList()
        .reversed()
        .mapIndexed { index, trace -> "\n\t\t${index + 1}. $trace" }
        .joinToString("")

    private fun isLoggable(trace: String) = !trace.startsWith(PACKAGE)

}