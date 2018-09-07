package com.seanshubin.devops

import kotlinx.coroutines.experimental.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class CaptureInputStream(private val emit:(String)->Unit,
                         private val type:String,
                         private val command:List<String>,
                         private val inputStream: InputStream){
    fun start(){
        launch {
            val inputStreamReader = InputStreamReader(inputStream, GlobalConstants.Charset)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line = bufferedReader.readLine()
            while(line != null){
                val commandString = command.joinToString(" ")
                val outputLine = LineFormat.composeOutputLine(Source.Companion.Local, type, commandString, line)
                emit(outputLine)
                line = bufferedReader.readLine()
            }
        }
    }
}
