plugins {
    id("me.champeau.jmh") version "0.7.2"
    id("maven-publish")
}

group = "io.github.dunemaster"
version = "0.9-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}
tasks.jar {
    archiveFileName.set("${project.group}-${project.name}-${project.version}.jar")
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