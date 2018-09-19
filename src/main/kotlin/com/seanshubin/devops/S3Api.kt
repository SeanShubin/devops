package com.seanshubin.devops

import java.nio.file.Path

interface S3Api {
    fun ensureBucketExists()
    fun uploadFile(name:String, path: Path)
    fun enableWebsiteHosting()
}
