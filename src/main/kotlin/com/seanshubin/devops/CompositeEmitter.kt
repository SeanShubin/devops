package com.seanshubin.devops

class CompositeEmitter(private vararg val emitters:(String)->Unit):(String)->Unit {
    override fun invoke(line: String) {
        for (emitter in emitters) {
            emitter(line)
        }
    }
}
