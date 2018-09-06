package com.seanshubin.devops

fun indent(amount: Int): (String) -> String = { s -> " ".repeat(amount) + s }

val displayLine = { line: String -> println(line) }

fun createDisplayFunction(ec2: Ec2Api) = { caption: String ->
    println(caption)
    ec2.listInstancesNotTerminated().map(indent(2)).forEach(displayLine)
}

fun main(args: Array<String>) {
    // initial
    val ec2 = Ec2Factory.create()
    val display = createDisplayFunction(ec2)
    display("Initial state")

    // create
    val myEc2Instance = ec2.createInstance(Constants.AmazonLinux2AMI, Constants.KeyName)
    display("After created $myEc2Instance")

    // terminate
    ec2.terminateInstance(myEc2Instance)
    display("After terminated $myEc2Instance")

//    val emit = { s:String -> println(s)}
//    val logger = LineEmittingLogger(emit)
//    val clock = Clock.systemUTC()
//    val local:LocalShell = LocalShellBackedByProcess(logger, clock)
//    local.execute("ls", "-1")
//    val host = "ec2-52-91-230-218.compute-1.amazonaws.com"
//    val userName = "ec2-user"
//    val privateKeyPathName = "/Users/sshubin/aws/sean-test-ok-to-delete.pem"
//    val privateKey = PrivateKeyUtil.loadFromFileName(privateKeyPathName)
//    val aws:SshShell = JcabiShell(logger, clock, host, userName, privateKey)
//    aws.execute("pwd")

//aws --region us-east-1 ec2 describe-images --image-ids ami-04681a1dbd79675a5
//    val client: AmazonEC2Async = AmazonEC2AsyncClientBuilder.standard().withRegion(Regions.US_EAST_1).build()
//    val request = DescribeImagesRequest()
//    request.withImageIds("ami-04681a1dbd79675a5")
//    val result: DescribeImagesResult = client.describeImages(request)
//    println(result.images[0].description)

//    val imageId = "ami-04681a1dbd79675a5"
//    val keyName = "sean-test-ok-to-delete"
//    val client: AmazonEC2Async = AmazonEC2AsyncClientBuilder.standard().withRegion(Regions.US_EAST_1).build()
//    val api = Ec2Api(client)
//    api.listInstancesNotTerminated().forEach{println(it)}
//    val instanceId = api.createInstance(imageId, keyName)
//    api.terminateInstance("i-04f57c705a0af2ee8")

//    val request = RunInstancesRequest()
//    request.withImageId("ami-04681a1dbd79675a5")
//    request.withMinCount(1)
//    request.withMaxCount(1)
//    request.withKeyName("sean-test-ok-to-delete")
//    val result: RunInstancesResult = client.runInstances(request)
//    println(result)
}
/*
chmod 400 ~/aws/sean-test-ok-to-delete.pem
ssh -i "~/aws/sean-test-ok-to-delete.pem" ec2-user@ec2-52-91-230-218.compute-1.amazonaws.com

wget https://my.datomic.com/downloads/free/0.9.5703 -O datomic-free-0.9.5703.zip
unzip datomic-free-0.9.5703.zip
sudo yum -y install java
./bin/transactor config/samples/free-transactor-template.properties
 */
