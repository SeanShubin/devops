package com.seanshubin.devops

import com.amazonaws.regions.Regions
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object GlobalConstants {
    val Charset: Charset = StandardCharsets.UTF_8
    val Region = Regions.US_EAST_1
    const val AmazonLinux2AMI = "ami-04681a1dbd79675a5"
    const val KeyName = "schulze-voter"
    const val UserName = "ec2-user"
    private const val UserHomeDirName = "/Users/sshubin"
    const val PrivateKeyPathName = "$UserHomeDirName/aws/schulze-voter.pem"
    const val DatomicVersion = "0.9.5703"
    const val SecurityGroup = "ssh-8080"
    const val SchulzeJar = "$UserHomeDirName/github/sean/schulze/server/target/schulze.jar"
}
