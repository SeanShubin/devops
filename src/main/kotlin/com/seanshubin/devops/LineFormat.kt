package com.seanshubin.devops

import java.time.Instant

object LineFormat {
    fun composeStartLine(source:Source, startTime:Instant, command:String):String {
        return "START: $source $startTime $command"
    }
    fun composeOutputLine(source:Source, type:String, command:String, line:String):String {
        return "$source $type $command> $line"
    }
    fun composeEndLine(startTime: Instant, endTime: Instant, source:Source, command:String, exitCode:Int):String {
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        return "END: startTime = $startTime endTime = $endTime duration = $duration milliseconds source = $source command = $command exitCode = $exitCode"
    }
}
