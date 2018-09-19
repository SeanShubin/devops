package com.seanshubin.devops

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.nio.file.Path

class S3ApiClient(private val client: AmazonS3,
                  private val bucketName:String) : S3Api {
    override fun ensureBucketExists() {
        val buckets = client.listBuckets()
        if (!buckets.any { bucket -> bucket.name == bucketName }) {
            client.createBucket(bucketName)
        }
    }

    override fun uploadFile(name:String, path: Path) {
        val request = PutObjectRequest(
                bucketName,
                name,
                path.toFile()).
                withCannedAcl(CannedAccessControlList.PublicRead)
        client.putObject(request)
    }

    override fun enableWebsiteHosting() {
        val configuration = BucketWebsiteConfiguration("index.html", "error.html")
        client.setBucketWebsiteConfiguration(bucketName, configuration)
    }
}
