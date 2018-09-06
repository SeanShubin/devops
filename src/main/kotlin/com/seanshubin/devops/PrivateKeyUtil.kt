package com.seanshubin.devops

import java.nio.file.Files
import java.nio.file.Paths

object PrivateKeyUtil {
    fun loadFromFileName(fileName: String): String {
        val path = Paths.get(fileName)
        val bytes = Files.readAllBytes(path)
        val privateKey = IoUtil.bytesToString(bytes)
        return privateKey
    }
}