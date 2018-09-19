package com.seanshubin.devops

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import java.nio.charset.StandardCharsets
import java.nio.file.*
import com.seanshubin.devops.PathUtil.findFilesInDirectory

class HtmlGenerator(private val markdownDirectory:Path,
                    private val htmlDirectory:Path){
    val charset = StandardCharsets.UTF_8

    fun loadFileContents(file: Path): String = Files.readAllBytes(file).toString(charset)

    fun storeFileContents(file: Path, contents: String) {
        val parent = file.parent
        Files.createDirectories(parent)
        Files.write(file, contents.toByteArray(charset))
    }


    fun generateHtml(files: List<Path>, updatePath: (Path) -> Path) {
        val options = MutableDataSet()
        val parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()
        for (file in files) {
            val contents = loadFileContents(file)
            println("Old contents")
            println(contents)
            val replacedContents = updateLinks(contents)
            println("New contents")
            println(replacedContents)
            println()
            val parsed = parser.parse(replacedContents)
            val rendered = renderer.render(parsed)
            val newPath = updatePath(file)
            storeFileContents(newPath, rendered)
        }
    }

    fun updateLinks(text:String):String {
        val regex = Regex(MyPatterns.markdownLink)
        fun transform(matchResult:MatchResult):CharSequence {
            val groups = matchResult.groups
            val group: MatchGroup? = groups[1]
            val value = group!!.value
            return "($value.html)"
        }
        return text.replace(regex, ::transform)
    }

    fun createUpdatePathFunction(source: Path, destination: Path): (Path) -> Path {
        fun updatePathFunction(path: Path): Path {
            val relative = source.relativize(path)
            val newLocation = destination.resolve(relative)
            val newExtension = replaceExtension(newLocation, "md", "html")
            return newExtension
        }
        return ::updatePathFunction
    }
    fun replaceExtension(name:String, oldExtension:String, newExtension:String):String {
        if(name.endsWith(".$oldExtension")){
            val bare = name.substring(0, name.lastIndexOf(oldExtension)-1)
            val withNewExtension = "$bare.$newExtension"
            return withNewExtension
        }else {
            throw RuntimeException("Expected file $name to end with extension $oldExtension")
        }
    }

    fun replaceExtension(path: Path, oldExtension:String, newExtension:String): Path {
        val name = path.fileName.toString()
        val newPath = path.parent.resolve(replaceExtension(name, oldExtension, newExtension))
        return newPath
    }

    fun generateHtml() {
        val files = findFilesInDirectory(markdownDirectory)
        val updatePathFunction = createUpdatePathFunction(markdownDirectory, htmlDirectory)
        generateHtml(files, updatePathFunction)
    }

}