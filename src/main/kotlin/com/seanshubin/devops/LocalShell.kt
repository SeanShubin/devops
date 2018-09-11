package com.seanshubin.devops

interface LocalShell {
    fun execute(vararg command:String)
    fun executeAndReturnExitCode(vararg command:String):Int
}
