package com.seanshubin.devops

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import java.nio.file.Paths

object StaticWebsiteDependencyInjection{
    val markdownDirectory = Paths.get("src", "main", "resources", "s3data")
    val htmlDirectory = Paths.get("out", "s3data")
    val bucketName = "com.cj.sshubin.test"
    val s3Client= AmazonS3ClientBuilder.standard().withRegion(GlobalConstants.Region).build()
    val s3Api = S3ApiClient(s3Client, bucketName)
    val htmlGenerator = HtmlGenerator(markdownDirectory, htmlDirectory)
    val uploader = Uploader(s3Api, htmlDirectory)
    val url = "http://$bucketName.s3-website-us-east-1.amazonaws.com"
}
