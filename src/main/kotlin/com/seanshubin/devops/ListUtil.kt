package com.seanshubin.devops

fun <T> List<T>.exactlyOne():T = when {
    this.isEmpty() -> throw RuntimeException("Expected exactly one element, list was empty")
    this.size > 1 -> throw RuntimeException("Expected exactly one element, list had ${this.size}")
    else -> this[0]
}
