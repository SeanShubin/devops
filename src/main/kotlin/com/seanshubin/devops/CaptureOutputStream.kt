package com.seanshubin.devops

import java.io.OutputStream

class CaptureOutputStream(private val emit: (String) -> Unit,
                          private val host:String,
                          private val type: String,
                          private val command: String) : OutputStream() {
    private val CR = '\r'
    private val LF = '\n'
    private val stringBuilder = StringBuilder()
    override fun write(b: Int) {
        if (isNewLine(b)) {
            emitLine()
        } else {
            appendChar(b)
        }
    }

    override fun flush() {
        emitLine()
        super.flush()
    }

    override fun close() {
        emitLine()
        super.close()
    }

    private fun isNewLine(b: Int): Boolean = b == CR.toInt() || b == LF.toInt()
    private fun emitLine() {
        if (!stringBuilder.isEmpty()) {
            val line = stringBuilder.toString()
            val outputLine = LineFormat.composeOutputLine(Source.Companion.Ssh(host), type, command, line)
            emit(outputLine)
            stringBuilder.setLength(0)
        }
    }

    private fun appendChar(b: Int) {
        stringBuilder.append(b.toChar())
    }
}
