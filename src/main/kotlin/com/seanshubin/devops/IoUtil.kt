package com.seanshubin.devops

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

object IoUtil {
    private val charset: Charset = GlobalConstants.Charset
    fun inputStreamToLines(inputStream:InputStream):List<String>{
        val inputStreamReader = InputStreamReader(inputStream, charset)
        val bufferedReader = BufferedReader(inputStreamReader)
        var line = bufferedReader.readLine()
        val lines = mutableListOf<String>()
        while(line != null){
            lines.add(line)
            line = bufferedReader.readLine()
        }
        return lines
    }
    fun bytesToLines(bytes:ByteArray):List<String> {
        val inputStream = ByteArrayInputStream(bytes)
        return inputStreamToLines(inputStream)
    }
    fun bytesToString(bytes:ByteArray):String {
        return String(bytes, charset)
    }
}
