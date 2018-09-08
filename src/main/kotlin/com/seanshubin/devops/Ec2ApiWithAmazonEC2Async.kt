package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.model.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import org.apache.commons.lang3.ThreadUtils
import java.util.concurrent.TimeUnit
import java.util.function.BiPredicate

class Ec2ApiWithAmazonEC2Async(private val client: AmazonEC2Async,
                               private val logWaiting:(String, Int, Any)->Unit):Ec2Api {
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

    override fun waitUntilGone(id: String, quantity: Long, unit: TimeUnit) {
        waitUntilEqual("gone", true){
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
        waitUntilEqual("ready", "running"){
            getState(id)
        }
//        waitUntilValidHost(id)
    }

    private fun waitUntilEqual(caption:String, target:Any, checkValue: () -> Any){
        var value = checkValue()
        var attempt = 1
        logWaiting(caption, attempt, value)
        val job = launch {
            while (value != target) {
                delay(5, TimeUnit.SECONDS)
                value = checkValue()
                attempt++
                logWaiting(caption, attempt, value)
            }
        }
        runBlocking {
            withTimeout(2, TimeUnit.MINUTES) {
                job.join()
            }
        }
    }

//    private fun waitUntilValidHost(id:String){
//        var value = getHost(id)
//        var attempt = 1
//        logWaiting("host", attempt, value)
//        val job = launch {
//            while (value.isEmpty()) {
//                delay(5, TimeUnit.SECONDS)
//                value = getHost(id)
//                attempt++
//                logWaiting("host", attempt, value)
//            }
//        }
//        runBlocking {
//            withTimeout(2, TimeUnit.MINUTES) {
//                job.join()
//            }
//        }
//    }
//
    private fun getState(id: String): String {
        val request = DescribeInstancesRequest()
        request.withInstanceIds(id)
        val result = client.describeInstances(request)
        val state = result.reservations.exactlyOne().instances.exactlyOne().state.name
        return state
    }

    private fun checkIfGone(id:String):Boolean = !listInstancesNotTerminated().contains(id)
}
