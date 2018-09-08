package com.seanshubin.devops

import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.nio.file.StandardOpenOption.*

class LogFileEmitter(now: Instant): (String) -> Unit {
    private val logDirectory = Paths.get("out","logs")
    private val fileName = now.toString().toSafeFileName()
    private val logFile = logDirectory.resolve(fileName)
    init {
        Files.createDirectories(logDirectory)
    }
    override fun invoke(line: String) {
        withPrintWriter(logFile){
            printWriter -> printWriter.println(line)
        }
    }
    companion object {
        fun String.toSafeFileName(): String = this.replace(Regex(":"), "-")

        fun withPrintWriter(path: Path, block: (PrintWriter) -> Unit): Unit {
            val writer = Files.newBufferedWriter(path, CREATE, APPEND)
            val printWriter = PrintWriter(writer)
            block(printWriter)
            printWriter.close()
        }
    }
}
