package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.RunInstancesRequest
import com.amazonaws.services.ec2.model.RunInstancesResult
import com.amazonaws.services.ec2.model.TerminateInstancesRequest
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import java.util.concurrent.TimeUnit

class Ec2ApiWithAmazonEC2Async(private val client: AmazonEC2Async):Ec2Api {
    override fun listInstancesNotTerminated(): List<String> {
        val request = DescribeInstancesRequest()
        val result = client.describeInstances(request)
        val list = mutableListOf<String>()
        for (reservation in result.reservations) {
            for (instance in reservation.instances) {
                if (instance.state.name != "terminated") {
                    val id = instance.instanceId
                    list.add(id)
                }
            }
        }
        return list
    }

    override fun createInstance(imageId: String, keyName: String): String {
        val request = RunInstancesRequest()
        request.withImageId(imageId)
        request.withMinCount(1)
        request.withMaxCount(1)
        request.withKeyName(keyName)
        val result: RunInstancesResult = client.runInstances(request)
        val instanceId = exactlyOne(result.reservation.instances).instanceId
        return instanceId
    }

    override fun terminateInstance(instanceId: String) {
        val request = TerminateInstancesRequest()
        request.withInstanceIds(instanceId)
        client.terminateInstances(request)
    }

    override fun waitForInstanceToGoAway(id: String, quantity: Long, unit: TimeUnit) {
        var isGone = checkIfGone(id)
        val job = launch {
            while (!isGone) {
                delay(5, TimeUnit.SECONDS)
                isGone = checkIfGone(id)
            }
        }
        runBlocking {
            withTimeout(quantity, unit) {
                job.join()
            }
        }
    }

    private fun <T> exactlyOne(list: List<T>): T {
        when {
            list.isEmpty() -> throw RuntimeException("Expected exactly one element, list was empty")
            list.size > 1 -> throw RuntimeException("Expected exactly one element, list had ${list.size}")
            else -> return list[0]
        }
    }

    private fun checkIfGone(id:String):Boolean = !listInstancesNotTerminated().contains(id)
}
