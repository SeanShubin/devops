package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.AmazonEC2AsyncClientBuilder

object Ec2Factory {
    fun create(logWaiting:(String, Int, Any)->Unit): Ec2Api {
        val client: AmazonEC2Async = AmazonEC2AsyncClientBuilder.standard().withRegion(GlobalConstants.Region).build()
        return Ec2ApiWithAmazonEC2Async(client, logWaiting)
    }
}
