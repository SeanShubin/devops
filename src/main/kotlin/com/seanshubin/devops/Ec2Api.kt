package com.seanshubin.devops

import java.util.concurrent.TimeUnit

interface Ec2Api {
    fun listInstancesNotTerminated(): List<String>
    fun createInstance(imageId: String, keyName: String): String
    fun terminateInstance(instanceId: String)
    fun waitForInstanceToGoAway(id: String, quantity: Long, unit: TimeUnit)
}
