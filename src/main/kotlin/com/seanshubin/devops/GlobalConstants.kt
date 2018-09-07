package com.seanshubin.devops

import com.amazonaws.regions.Regions
import java.nio.charset.StandardCharsets

object GlobalConstants {
    val Charset = StandardCharsets.UTF_8
    val AmazonLinux2AMI = "ami-04681a1dbd79675a5"
    val Region = Regions.US_EAST_1
    val KeyName = "sean-test-ok-to-delete"
    val UserName = "ec2-user"
    val PrivateKeyPathName = "/Users/sshubin/aws/sean-test-ok-to-delete.pem"
    val DatomicVersion = "0.9.5703"
}
