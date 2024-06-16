plugins {
    kotlin("jvm") version "1.9.23"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.dunemaster.unrolledList"
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
kotlin {
    jvmToolchain(17)
}