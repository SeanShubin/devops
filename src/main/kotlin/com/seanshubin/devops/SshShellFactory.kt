package com.seanshubin.devops

import java.time.Clock

object SshShellFactory {
    fun create(host: String): SshShell {
        val emit = { s: String -> println(s) }
        val logger = LineEmittingLogger(emit)
        val clock = Clock.systemUTC()
        val privateKey = PrivateKeyUtil.loadFromFileName(GlobalConstants.PrivateKeyPathName)
        val shell: SshShell = JcabiShell(logger, clock, host, GlobalConstants.UserName, privateKey)
        return shell
    }
}
