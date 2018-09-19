package com.seanshubin.devops


fun main(args: Array<String>) {
    val di = StaticWebsiteDependencyInjection
    di.s3Api.ensureBucketExists()
    di.htmlGenerator.generateHtml()
    di.uploader.upload()
    di.s3Api.enableWebsiteHosting()
    println(di.url)
}
