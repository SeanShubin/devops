package com.seanshubin.devops

import java.io.InputStream
import java.io.OutputStream
import java.time.Instant

interface Logger {
    fun processStart(startTime: Instant,
                     command: List<String>,
                     standardOutput: InputStream,
                     standardError: InputStream)

    fun sshStart(host:String,
                 startTime: Instant,
                 command: String)

    fun processEnd(startTime: Instant,
                   endTime: Instant,
                   command: List<String>,
                   exitCode: Int)

    fun sshEnd(source:String,
               startTime: Instant,
               endTime: Instant,
               command: String,
               exitCode: Int)

    fun captureOutputStream(source:String, type: String, prefix: String): OutputStream
    fun waitingOn(caption:String, attempt:Int, done:Any)
    fun update(caption:String, status:String, attempt:Int)
}
