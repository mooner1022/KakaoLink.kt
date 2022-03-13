import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version = "1.6.7"

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    application
}

group = "dev.mooner.kakaolink"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("it.skrape:skrapeit:1.2.1") {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    //implementation("org.slf4j:slf4j-simple:1.7.36")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("MainKt")
}