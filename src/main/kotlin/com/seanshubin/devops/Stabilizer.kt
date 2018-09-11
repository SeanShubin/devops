package com.seanshubin.devops

interface Stabilizer {
    fun <T> create(caption: String, createFunction: () -> T): T

    fun <T> waitUntilEqual(caption: String, target: T, checkValue: () -> T)
}
