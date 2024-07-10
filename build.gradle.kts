plugins {
    kotlin("jvm") version "1.9.23"
    id("me.champeau.jmh") version "0.7.2"
    id("maven-publish")
}

group = "io.github.dunemaster"
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


            pom {
                name.set("Unrolled Deque")
                description.set("Unrolled linked deque implementation")
                url.set("https://github.com/Dunemaster/unrolledListDeque")
                inceptionYear.set("2024")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://spdx.org/licenses/Apache-2.0.html")
                    }
                }
                developers {
                    developer {
                        name.set("Vasilii Kudriavtsev")
                    }
                }
                scm {
                    url.set("https://github.com/Dunemaster/unrolledListDeque")
                }
            }

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