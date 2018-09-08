package com.seanshubin.devops

import com.amazonaws.services.ec2.AmazonEC2Async
import com.amazonaws.services.ec2.AmazonEC2AsyncClientBuilder
import java.time.Clock

object DatomicServerDependencyInjection {
    private val clock = Clock.systemUTC()
    private val now = clock.instant()
    private val emitConsole:(String)->Unit = ::println
    private val emitFile = LogFileEmitter(now)
    private val emit = CompositeEmitter(emitConsole, emitFile)
    private val logger = LineEmittingLogger(emit)
    private val privateKey = PrivateKeyUtil.loadFromFileName(GlobalConstants.PrivateKeyPathName)
    val createShellFromHost: (String) -> SshShell = { host: String -> JcabiShell(logger, clock, host, GlobalConstants.UserName, privateKey) }
    val datomicDir = "datomic-free-${GlobalConstants.DatomicVersion}"
    private val client: AmazonEC2Async = AmazonEC2AsyncClientBuilder.standard().withRegion(GlobalConstants.Region).build()
    val ec2 = Ec2ApiWithAmazonEC2Async(client, logger::waitingOn)
    val stabilizer = Stabilizer(logger::update)
}
