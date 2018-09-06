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
    compile("com.jcabi:jcabi-ssh:1.6")
    compile("com.amazonaws:aws-java-sdk-ec2:1.11.399")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinVersion")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
//    testCompile("org.jetbrains.kotlin:kotlin-test-common:$kotlinVersion")
    testCompile("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}