package com.seanshubin.devops

import java.io.InputStream
import java.io.OutputStream
import java.time.Instant

class LineEmittingLogger(val emit: (String) -> Unit) : Logger {
    override fun processStart(startTime: Instant, command: List<String>, standardOutput: InputStream, standardError: InputStream) {
        val commandString = command.joinToString(" ")
        val line = LineFormat.composeStartLine(Source.Companion.Local, startTime, commandString)
        emit(line)
        CaptureInputStream(emit, "output", command, standardOutput).start()
        CaptureInputStream(emit, "error", command, standardError).start()
    }

    override fun sshStart(host: String, startTime: Instant, command: String) {
        val line = LineFormat.composeStartLine(Source.Companion.Ssh(host), startTime, command)
        emit(line)
    }

    override fun processEnd(startTime: Instant, endTime: Instant, command: List<String>, exitCode: Int) {
        val commandString = command.joinToString(" ")
        val line = LineFormat.composeEndLine(startTime, endTime, Source.Companion.Local, commandString, exitCode)
        emit(line)
    }

    override fun sshEnd(source: String, startTime: Instant, endTime: Instant, command: String, exitCode: Int) {
        val line = LineFormat.composeEndLine(startTime, endTime, Source.Companion.Ssh(source),command, exitCode)
        emit(line)
    }

    override fun captureOutputStream(source: String, type: String, prefix: String): OutputStream =
            CaptureOutputStream(emit, source, type, prefix)
}
