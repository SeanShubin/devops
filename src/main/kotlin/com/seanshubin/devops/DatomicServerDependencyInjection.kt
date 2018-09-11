package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.AmazonEC2AsyncClientBuilder
import java.time.Clock
import java.time.Instant

object DatomicServerDependencyInjection {
    private val clock:Clock = Clock.systemUTC()
    private val now: Instant = clock.instant()
    private val emitConsole:(String)->Unit = ::println
    private val emitFile:(String)->Unit = LogFileEmitter(now)
    val emit:(String)->Unit = CompositeEmitter(emitConsole, emitFile)
    val logger:Logger = LineEmittingLogger(emit)
    private val privateKey:String = PrivateKeyUtil.loadFromFileName(GlobalConstants.PrivateKeyPathName)
    val createShellFromHost: (String) -> SshShell = { host: String -> JcabiShell(logger, clock, host, GlobalConstants.UserName, privateKey) }
    const val datomicDir:String = "datomic-free-${GlobalConstants.DatomicVersion}"
    private val client: AmazonEC2Async = AmazonEC2AsyncClientBuilder.standard().withRegion(GlobalConstants.Region).build()
    val stabilizer:Stabilizer = StabilizerWithStatusFunction(emit)
    val ec2:Ec2Api = Ec2ApiWithAmazonEC2Async(client, stabilizer)
    val localShell:LocalShell = LocalShellBackedByProcess(logger, clock)
}
