package com.seanshubin.devops

fun main(args: Array<String>) {
    println(MyPatterns.markdownLink)
}

object MyPatterns{
    val openParen = """\("""
    val closeParen = """\)"""
    val notCloseParen = """[^)]"""
    val dot = """\."""
    fun capture(s:String):String = "($s)"
    fun zeroOrMore(s:String):String = "$s*"
    val markdownLink = openParen + capture(zeroOrMore(notCloseParen)) + dot + "md" + closeParen
}
