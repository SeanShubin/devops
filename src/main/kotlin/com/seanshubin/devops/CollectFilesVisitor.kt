package com.seanshubin.devops

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

class CollectFilesVisitor : FileVisitor<Path> {
    val files = mutableListOf<Path>()
    override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult = FileVisitResult.CONTINUE

    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        files.add(file!!)
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
        throw RuntimeException("visitFileFailed file = $file, exc = $exc", exc)
    }

    override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult = FileVisitResult.CONTINUE
}
