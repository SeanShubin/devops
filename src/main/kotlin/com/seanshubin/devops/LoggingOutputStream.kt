package com.seanshubin.devops

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter

class LoggingOutputStream(val emit:(String)->Unit): OutputStream() {
    private val CR = '\r'
    private val LF = '\n'
    private val stringBuilder = StringBuilder()
    override fun write(b: Int) {
        if(isNewLine(b)){
            emitLine()
        } else {
            appendChar(b)
        }
    }

    override fun flush() {
        emitLine()
        super.flush()
    }

    override fun close() {
        emitLine()
        super.close()
    }

    private fun isNewLine(b:Int):Boolean = b == CR.toInt() || b == LF.toInt()
    private fun emitLine() {
        if(!stringBuilder.isEmpty()){
            emit(stringBuilder.toString())
            stringBuilder.setLength(0)
        }
    }
    private fun appendChar(b:Int){
        stringBuilder.append(b.toChar())
    }
}
