package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.RunInstancesRequest
import com.amazonaws.services.ec2.model.RunInstancesResult
import com.amazonaws.services.ec2.model.TerminateInstancesRequest
import java.util.concurrent.TimeUnit

class Ec2ApiWithAmazonEC2Async(private val client: AmazonEC2Async,
                               private val stabilizer: Stabilizer) : Ec2Api {
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
        request.withSecurityGroups(GlobalConstants.SecurityGroup)
        val result: RunInstancesResult = client.runInstances(request)
        val instanceId = result.reservation.instances.exactlyOne().instanceId
        return instanceId
    }

    override fun terminateInstance(instanceId: String) {
        val request = TerminateInstancesRequest()
        request.withInstanceIds(instanceId)
        client.terminateInstances(request)
    }

    override fun waitUntilGone(id: String) {
        stabilizer.waitUntilEqual("ec2 instance $id gone (or terminated)", true) {
            checkIfGone(id)
        }
    }

    override fun getHost(id: String): String {
        val request = DescribeInstancesRequest()
        request.withInstanceIds(id)
        val result = client.describeInstances(request)
        val host = result.reservations.exactlyOne().instances.exactlyOne().publicDnsName
        return host
    }

    override fun waitUntilReady(id: String) {
        stabilizer.waitUntilEqual("ec2 instance $id ready", "running") {
            getState(id)
        }
    }

    private fun getState(id: String): String {
        val request = DescribeInstancesRequest()
        request.withInstanceIds(id)
        val result = client.describeInstances(request)
        val state = result.reservations.exactlyOne().instances.exactlyOne().state.name
        return state
    }

    private fun checkIfGone(id: String): Boolean = !listInstancesNotTerminated().contains(id)
}
