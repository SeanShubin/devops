package com.seanshubin.devops

import com.jcabi.ssh.Ssh
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.Clock

class JcabiShell(
        private val logger:Logger,
        private val clock: Clock,
        private val host: String,
        userName: String,
        privateKey: String) : SshShell {
    private val port = 22
    private val unsafeShell = Ssh(host, port, userName, privateKey)
    override fun execute(command: String) {
        val startTime = clock.instant()
        val outputStream = logger.captureOutputStream(host, "output", command)
        val errorStream = logger.captureOutputStream(host, "error", command)
        val inputStream = ByteArrayInputStream(ByteArray(0))
        logger.sshStart(host, startTime, command)
        val exitCode = unsafeShell.exec(command, inputStream, outputStream, errorStream)
        val endTime = clock.instant()
        logger.sshEnd(host, startTime, endTime, command, exitCode)
    }
}
