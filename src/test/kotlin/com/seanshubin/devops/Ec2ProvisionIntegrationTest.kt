package com.seanshubin.devops

import org.junit.Test

class Ec2ProvisionIntegrationTest{
    @Test
    fun provisionEc2Instance() {
        // create the instance and make sure it is there
        val statusFunction = {s:String -> println(s)}
        val stabilizer = StabilizerWithStatusFunction(statusFunction)
        val ec2 = Ec2Factory.create(stabilizer)
        val myEc2Instance = ec2.createInstance(GlobalConstants.AmazonLinux2AMI, GlobalConstants.KeyName)
        val instancesAfterCreate = ec2.listInstancesNotTerminated()
        assert(instancesAfterCreate.contains(myEc2Instance))

        // terminate the instance and make sure it goes away
        ec2.terminateInstance(myEc2Instance)
        ec2.waitUntilGone(myEc2Instance)
        val instancesAfterTerminate = ec2.listInstancesNotTerminated()
        assert(!instancesAfterTerminate.contains(myEc2Instance))
    }
}
