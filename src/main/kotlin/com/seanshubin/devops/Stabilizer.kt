package com.seanshubin.devops

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import java.time.Duration

class Stabilizer(val statusFunction: (String, String, Int) -> Unit) {
    private val attemptLimit = 20
    private val timeBetweenAttempts = Duration.ofSeconds(5)
    private val totalTimeLimit = Duration.ofMinutes(2)

    fun <T> create(caption: String, createFunction: () -> T): T {
        var attempt = 1
        var done = false
        var resultToReturn: T? = null
        val job = launch {
            while (!done && attempt <= attemptLimit) {
                try {
                    statusFunction(caption, "trying", attempt)
                    val result = createFunction()
                    resultToReturn = result
                    done = true
                } catch (ex: Exception) {
                    statusFunction(caption, "failed", attempt)
                    attempt++
                    delay(timeBetweenAttempts.toMillis())
                }
            }
        }
        runBlocking {
            withTimeout(totalTimeLimit.toMillis()) {
                job.join()
            }
        }
        return resultToReturn!!
    }
}
