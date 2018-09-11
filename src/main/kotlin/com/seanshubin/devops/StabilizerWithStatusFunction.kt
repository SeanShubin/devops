package com.seanshubin.devops

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import java.time.Duration

class StabilizerWithStatusFunction(private val statusFunction: (String) -> Unit):Stabilizer {
    override fun <T> create(caption: String, createFunction: () -> T): T {
        val logger = StabilizerLogger<T>(caption, statusFunction)
        val verify = {_:T -> true}
        return stabilize(logger, verify, createFunction)
    }

    override fun <T> waitUntilEqual(caption: String, target:T, checkValue: () -> T) {
        val logger = StabilizerLogger<T>(caption, statusFunction)
        val verify = {value:T -> value == target}
        stabilize(logger, verify, checkValue)
    }

    private fun <T> stabilize(log:StabilizerLogger<T>, verify:(T) -> Boolean, execute:() -> T):T {
        var attempt = 1
        var done = false
        var resultToReturn: T? = null
        val job = launch {
            while (!done && attempt <= attemptLimit) {
                try {
                    log.startAttempt(attempt)
                    val result = execute()
                    log.gotResult(attempt, result)
                    if(verify(result)){
                        log.validResult(attempt, result)
                        resultToReturn = result
                        done = true
                    } else {
                        log.invalidResult(attempt, result)
                    }
                } catch (ex: Exception) {
                    log.exception(attempt, ex)
                } finally {
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

    companion object {
        private const val attemptLimit = 20
        private val timeBetweenAttempts = Duration.ofSeconds(5)
        private val totalTimeLimit = Duration.ofMinutes(2)

        class StabilizerLogger<T>(private val caption:String,
                                  private val emit:(String)->Unit){
            fun startAttempt(attempt:Int){
                emit("${prefix(attempt)} start")
            }
            fun gotResult(attempt:Int, result:T){
                emit("${prefix(attempt)} result($result)")
            }
            fun validResult(attempt:Int, result:T){
                emit("${prefix(attempt)} valid($result)")
            }
            fun invalidResult(attempt:Int, result:T){
                emit("${prefix(attempt)} invalid($result)")
            }
            fun exception(attempt:Int, ex:Exception){
                emit("${prefix(attempt)} exception(${ex.message})")
            }
            private fun prefix(attempt:Int)= "$caption($attempt/$attemptLimit)"
        }
    }
}
