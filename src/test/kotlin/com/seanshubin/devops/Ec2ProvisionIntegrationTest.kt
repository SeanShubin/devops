package com.seanshubin.devops

import org.junit.Test
import java.util.concurrent.TimeUnit

class Ec2ProvisionIntegrationTest{
    @Test
    fun provisionEc2Instance() {
        // create the instance and make sure it is there
        val ec2 = Ec2Factory.create()
        val myEc2Instance = ec2.createInstance(Constants.AmazonLinux2AMI, Constants.KeyName)
        val instancesAfterCreate = ec2.listInstancesNotTerminated()
        assert(instancesAfterCreate.contains(myEc2Instance))

        // terminate the instance and make sure it goes away
        ec2.terminateInstance(myEc2Instance)
        ec2.waitUntilGone(myEc2Instance, 2, TimeUnit.MINUTES)
        val instancesAfterTerminate = ec2.listInstancesNotTerminated()
        assert(!instancesAfterTerminate.contains(myEc2Instance))
    }
}
