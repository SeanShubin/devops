package com.seanshubin.devops

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun trackTimeMilliseconds(block: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    block()
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    return duration
}

class Prototype : Runnable {
    val ec2 = DatomicServerDependencyInjection.ec2
    val datomicVersion = GlobalConstants.DatomicVersion
    val datomicDir = DatomicServerDependencyInjection.datomicDir
    val schulzeDatabase = GlobalConstants.SchulzeDatabase
    val createShellFromHost = DatomicServerDependencyInjection.createShellFromHost
    val stabilizer = DatomicServerDependencyInjection.stabilizer
    val localShell = DatomicServerDependencyInjection.localShell
    val schulzeJar = GlobalConstants.SchulzeJar
    val userName = GlobalConstants.UserName
    val privateKeyPathName = GlobalConstants.PrivateKeyPathName
    val emit = DatomicServerDependencyInjection.emit

    var instanceId: String? = null
    var ssh: SshShell? = null
    var host: String? = null

    override fun run() {
        val time = trackTimeMilliseconds {
            setupEc2()
            setupDatomic()
            setupSchulze()
            setupJava()
            setupSupervisor()
        }
        done(time)
    }

    fun setupEc2() {
        instanceId = ec2.createInstance(GlobalConstants.AmazonLinux2AMI, GlobalConstants.KeyName)
        emit("instanceId = $instanceId")
        ec2.waitUntilReady(instanceId!!)
        host = ec2.getHost(instanceId!!)
        emit("host = $host")
        emit("ssh -i \"$privateKeyPathName\" $userName@$host")
        val createShell: () -> SshShell = {
            val shell = createShellFromHost(host!!)
            shell.execute("pwd")
            shell
        }
        ssh = stabilizer.create("create shell", createShell)
    }

    fun setupDatomic() {
        sshExec("wget https://my.datomic.com/downloads/free/$datomicVersion -O $datomicDir.zip")
        sshExec("unzip $datomicDir.zip")
        sshExec("rm $datomicDir.zip")
        sshExec("ln -s $datomicDir datomic")
        sshExec("mkdir -p datomic/data/db")
        sshExec("mkdir -p datomic/log")
        secureCopy("schulze database", schulzeDatabase, "datomic/data/db/")
    }

    fun setupSchulze() {
        sshExec("mkdir schulze")
        secureCopy("schulze app", schulzeJar, "schulze/")
        sshExec("mkdir -p schulze/log")
    }

    fun setupJava() {
        sshExec("sudo yum -y install java")
    }

    fun setupSupervisor() {
        sshExec("sudo easy_install supervisor")
        val text = listOf(
                GlobalConstants.SupervisorConfig,
                GlobalConstants.DatomicSupervisorConfig,
                GlobalConstants.SchulzeSupervisorConfig).joinToString("\n\n")
        val tempDir = Paths.get("out", "temp")
        Files.createDirectories(tempDir)
        val supervisorConfigPath = tempDir.resolve("supervisord.conf")
        Files.write(supervisorConfigPath, text.toByteArray(StandardCharsets.UTF_8))
        secureCopy("supervisor config", supervisorConfigPath.toString(), "")
        sshExec("sudo mv supervisord.conf /etc/")
        sshExec("supervisord")
    }

    fun sshExec(command: String) {
        ssh!!.execute(command)
    }

    fun secureCopy(caption: String, from: String, to: String) {
        stabilizer.waitUntilEqual("copying $caption", 0) {
            localShell.executeAndReturnExitCode("scp", "-o", "StrictHostKeyChecking=no", "-i", privateKeyPathName, from, "$userName@$host:$to")
        }
    }

    fun done(timeInMilliseconds: Long) {
        val lines = listOf(
                "All done!",
                "",
                "$timeInMilliseconds milliseconds",
                "Navigate here to the schulze app:",
                "    $host:8080",
                "To debug the application, you will need the private key 'schulze-voter.pem'",
                "log in with:",
                "    ssh -i \"schulze-voter.pem\" $userName@$host",
                "tail the logs with:",
                "    tail -f schulze/log/out.log schulze/log/err.log datomic/log/out.log datomic/log/err.log",
                "check the status with:",
                "    supervisorctl status",
                "see how everything is configured by looking with:",
                "    cat /etc/supervisord.conf",
                "",
                "$host:8080")
        lines.forEach(emit)
    }
}

fun main(args: Array<String>) {
    Prototype().run()
}
