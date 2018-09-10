package com.seanshubin.devops

object StringUtil {
    fun String.escape(): String = this.flatMap(::escapeCharToIterable).joinToString("")

    private fun escapeCharToString(target: Char): String =
            when (target) {
                '\n' -> "\\n"
                '\b' -> "\\b"
                '\t' -> "\\t"
                '\r' -> "\\r"
                '\"' -> "\\\""
                '\'' -> "\\\'"
                '\\' -> "\\\\"
                else -> target.toString()
            }

    private fun escapeCharToIterable(target: Char): Iterable<Char> = escapeCharToString(target).asIterable()
}
