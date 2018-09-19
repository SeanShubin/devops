import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    kotlin("jvm") version "1.2.61"
}

group = "com.seanshubin.devops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion = "0.24.0"

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(group = "com.jcabi", name = "jcabi-ssh", version = "1.6")
    compile(group = "com.amazonaws", name = "aws-java-sdk-ec2", version = "1.11.399")
    compile(group = "com.amazonaws", name = "aws-java-sdk-s3", version = "1.11.410")
    compile(group = "com.vladsch.flexmark", name = "flexmark-all", version = "0.34.30")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = kotlinVersion)
    testCompile(group = "junit", name = "junit", version = "4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}
