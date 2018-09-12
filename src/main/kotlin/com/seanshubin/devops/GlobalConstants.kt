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
    const val SchulzeDatabase = "$UserHomeDirName/backup/datomic-schulze/datomic.h2.db"

    val SchulzeSupervisorConfig = """
        [program:schulze]
        directory=/home/ec2-user/schulze/
        command=java -Djava.io.tmpdir=/tmp -cp schulze.jar com.seanshubin.schulze.server.ServerApplication 8080 datomic:free://localhost:4334/schulze
        stdout_logfile=/home/ec2-user/schulze/log/out.log
        stderr_logfile=/home/ec2-user/schulze/log/err.log
        autostart=true
    """.trimIndent()

    val DatomicSupervisorConfig = """
        [program:datomic]
        directory=/home/ec2-user/datomic/
        command=/home/ec2-user/datomic/bin/transactor /home/ec2-user/datomic/config/samples/free-transactor-template.properties
        stdout_logfile=/home/ec2-user/datomic/log/out.log
        stderr_logfile=/home/ec2-user/datomic/log/err.log
        autostart=true
    """.trimIndent()

    val SupervisorConfig = """
        [unix_http_server]
        file=/tmp/supervisor.sock   ; the path to the socket file

        [supervisord]
        logfile=/tmp/supervisord.log ; main log file; default ${'$'}CWD/supervisord.log
        logfile_maxbytes=50MB        ; max main logfile bytes b4 rotation; default 50MB
        logfile_backups=10           ; # of main logfile backups; 0 means none, default 10
        loglevel=info                ; log level; default info; others: debug,warn,trace
        pidfile=/tmp/supervisord.pid ; supervisord pidfile; default supervisord.pid
        nodaemon=false               ; start in foreground if true; default false
        minfds=1024                  ; min. avail startup file descriptors; default 1024
        minprocs=200                 ; min. avail process descriptors;default 200

        [rpcinterface:supervisor]
        supervisor.rpcinterface_factory = supervisor.rpcinterface:make_main_rpcinterface

        [supervisorctl]
        serverurl=unix:///tmp/supervisor.sock ; use a unix:// URL  for a unix socket
    """.trimIndent()
}
