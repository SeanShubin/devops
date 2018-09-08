package com.seanshubin.devops

fun datomicServer() {
    val datomicDir = DatomicServerDependencyInjection.datomicDir
    val ec2 = DatomicServerDependencyInjection.ec2
    ec2.listInstancesNotTerminated()
    val createShellFromHost = DatomicServerDependencyInjection.createShellFromHost
    val instanceId = ec2.createInstance(GlobalConstants.AmazonLinux2AMI, GlobalConstants.KeyName)
    println("instanceId = $instanceId")
    val stabilizer = DatomicServerDependencyInjection.stabilizer
    ec2.waitUntilReady(instanceId)
    val host = ec2.getHost(instanceId)
    println("host = $host")
    println("ssh -i \"${GlobalConstants.PrivateKeyPathName}\" ${GlobalConstants.UserName}@$host")
    val createShell: () -> SshShell = {
        val shell = createShellFromHost(host)
        shell.execute("pwd")
        shell
    }
    val ssh = stabilizer.create("create shell", createShell)
//    ssh.execute("rm -rf $datomicDir")
//    ssh.execute("rm $datomicDir.zip")
    ssh.execute("wget https://my.datomic.com/downloads/free/${GlobalConstants.DatomicVersion} -O $datomicDir.zip")
    ssh.execute("unzip $datomicDir.zip")
    ssh.execute("sudo yum -y install java")
    ssh.execute("cd $datomicDir && bin/transactor config/samples/free-transactor-template.properties")
}

fun main(args: Array<String>) {
    datomicServer()


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
