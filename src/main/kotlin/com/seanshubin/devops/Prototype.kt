package com.seanshubin.devops

fun datomicServer() {
    val schulzeJar = GlobalConstants.SchulzeJar
    val userName = GlobalConstants.UserName
    val privateKeyPathName = GlobalConstants.PrivateKeyPathName
    val datomicVersion = GlobalConstants.DatomicVersion
    val datomicDir = DatomicServerDependencyInjection.datomicDir
    val ec2 = DatomicServerDependencyInjection.ec2
    val log = DatomicServerDependencyInjection.logger
    val localShell = DatomicServerDependencyInjection.localShell
    ec2.listInstancesNotTerminated()
    val createShellFromHost = DatomicServerDependencyInjection.createShellFromHost
    val instanceId = ec2.createInstance(GlobalConstants.AmazonLinux2AMI, GlobalConstants.KeyName)
    log.emit("instanceId = $instanceId")
    val stabilizer = DatomicServerDependencyInjection.stabilizer
    ec2.waitUntilReady(instanceId)
    val host = ec2.getHost(instanceId)
    log.emit("host = $host")
    log.emit("ssh -i \"$privateKeyPathName\" $userName@$host")
    val createShell: () -> SshShell = {
        val shell = createShellFromHost(host)
        shell.execute("pwd")
        shell
    }
    val ssh = stabilizer.create("create shell", createShell)
    ssh.execute("wget https://my.datomic.com/downloads/free/$datomicVersion -O $datomicDir.zip")
    ssh.execute("unzip $datomicDir.zip")

    stabilizer.waitUntilEqual("copying schulze app", 0){
        localShell.executeAndReturnExitCode("scp", "-o", "StrictHostKeyChecking=no", "-i", privateKeyPathName, schulzeJar, "$userName@$host:")
    }

    ssh.execute("sudo yum -y install java")
    ssh.execute("cd $datomicDir && bin/transactor config/samples/free-transactor-template.properties")
    ssh.execute("java -jar schulze.jar 8080 datomic:free://localhost:4334/schulze")
}

fun main(args: Array<String>) {
    datomicServer()
}
