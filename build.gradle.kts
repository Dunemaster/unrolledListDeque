plugins {
    kotlin("jvm") version "1.9.23"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.dunemaster"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
tasks.jar {
    archiveFileName.set("${project.group}-${project.name}-${project.version}.jar")
}

kotlin {
    jvmToolchain(8)
}