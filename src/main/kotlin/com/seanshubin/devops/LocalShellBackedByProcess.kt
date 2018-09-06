package com.seanshubin.devops

import java.time.Clock

class LocalShellBackedByProcess(private val logger:Logger,
                                private val clock: Clock):LocalShell {
    override fun execute(vararg command: String) {
        val startTime = clock.instant()
        val process: Process = ProcessBuilder().command(*command).start()
        process.errorStream
        logger.processStart(
                startTime,
                command.toList(),
                process.inputStream,
                process.errorStream)
        val exitCode = process.waitFor()
        val endTime = clock.instant()
        logger.processEnd(
                startTime,
                endTime,
                command.toList(),
                exitCode)
    }
}
