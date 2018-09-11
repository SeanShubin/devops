package com.seanshubin.devops

interface Ec2Api {
    fun listInstancesNotTerminated(): List<String>
    fun createInstance(imageId: String, keyName: String): String
    fun terminateInstance(instanceId: String)
    fun waitUntilGone(id: String)
    fun getHost(id: String): String
    fun waitUntilReady(id: String)
}
