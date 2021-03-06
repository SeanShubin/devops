package com.seanshubin.devops

import java.nio.file.Path
import com.seanshubin.devops.PathUtil.findFilesInDirectory

class Uploader(private val s3Api: S3Api,
               private val htmlDirectory: Path) {
    fun upload(){
        val files = findFilesInDirectory(htmlDirectory)
        files.map(::uploadFile)

    }
    private fun uploadFile(path:Path){
        val name = htmlDirectory.relativize(path).toString()
        s3Api.uploadFile(name, path)
    }
}
