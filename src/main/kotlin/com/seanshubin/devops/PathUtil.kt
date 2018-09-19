package com.seanshubin.devops

import java.nio.file.Files
import java.nio.file.Path

object PathUtil {
    fun findFilesInDirectory(directory: Path):List<Path> {
            val collectFilesVisitor = CollectFilesVisitor()
            Files.walkFileTree(directory, collectFilesVisitor)
            return collectFilesVisitor.files
    }
}