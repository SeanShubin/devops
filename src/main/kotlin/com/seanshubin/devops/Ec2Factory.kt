package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.AmazonEC2AsyncClientBuilder

object Ec2Factory {
    fun create(): Ec2Api {
        val client: AmazonEC2Async = AmazonEC2AsyncClientBuilder.standard().withRegion(Constants.Region).build()
        return Ec2ApiWithAmazonEC2Async(client)
    }
}
