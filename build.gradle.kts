plugins {
    kotlin("jvm") version "1.9.23"
    id("me.champeau.jmh") version "0.7.2"
    id("maven-publish")
}

group = "io.github.dunemaster.unrolleddeque"
version = "0.9-SNAPSHOT"

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

publishing {
    publications {
        create("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}