import org.jreleaser.model.Active


plugins {
    id("me.champeau.jmh") version "0.7.2"
    id("maven-publish")
    id("org.jreleaser") version "1.13.1"
}

group = "io.github.dunemaster"
version = "0.10"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

ext.set("allArchivesBaseName", "${project.group}-${project.name}")

tasks.jar {
    archiveBaseName.set(ext.get("allArchivesBaseName") as String)
}
tasks.getByName<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    archiveBaseName.set(ext.get("allArchivesBaseName") as String)
    from(sourceSets["main"].allSource)
}

tasks.getByName<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    archiveBaseName.set(ext.get("allArchivesBaseName") as String)
    from(tasks["javadoc"])
}

publishing {
    publications {
        create("mavenJava", MavenPublication::class) {
            from(components["java"])

            pom {
                name.set("unrolledDeque")
                description.set("Unrolled linked deque implementation")
                url.set("https://github.com/Dunemaster/unrolledListDeque")
                inceptionYear.set("2024")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/license/mit")
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
        // This one is needed for the JReleaser as a sources repository
        maven {
            name = "staging-deploy"
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

jreleaser {

    signing {
        active.set(Active.ALWAYS)
        armored = true
    }
    release {
        github {
            enabled = true
            repoOwner = "dunemaster"
            name = "unrolledListDeque"
            changelog {
                enabled = false
            }
        }
    }
    deploy {
        maven {
            enabled = true
            mavenCentral {
                enabled = true
                create("mavenCentral")  {
                    enabled = true
                    snapshotSupported = true
                    active.set(Active.ALWAYS)
                    stagingRepository("build/staging-deploy")
                    url.set("https://central.sonatype.com/api/v1/publisher")
                }

            }
        }
    }
}


