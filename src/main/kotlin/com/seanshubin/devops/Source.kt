package com.seanshubin.devops

interface Source{
    companion object {
        object Local:Source {
            override fun toString(): String = "local"
        }
        class Ssh(private val host:String):Source {
            override fun toString(): String = "ssh $host"
        }
    }
}
