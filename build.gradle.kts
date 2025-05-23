import org.jreleaser.model.Active
import org.jreleaser.model.Signing

plugins {
    `java-library`
    `maven-publish`
    signing
    id("org.jreleaser") version "1.18.0"
}

group = "io.github.mictaege"
version = "2025.3-rc1"

tasks.wrapper {
    gradleVersion = "8.7"
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("jitter-api")
                description.set("This repository contains the _jitter_ API. For using _jitter_ see the https://github.com/mictaege/jitter_plugin documentation.")
                url.set("https://github.com/mictaege/jitter-api")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("mictaege")
                        name.set("Michael Taege")
                        email.set("mictaege@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mictaege/jitter-api.git")
                    url.set("https://github.com/mictaege/jitter-api")
                }
            }
        }
    }
    repositories {
        maven {
            name = "staging"
            url = uri(layout.buildDirectory.dir("staging").get().asFile.toURI())
        }
    }
}

jreleaser {
    project {
        copyright.set("Michael Taege")
        description.set("This repository contains the _jitter_ API. For using _jitter_ see the https://github.com/mictaege/jitter_plugin documentation.")
    }
    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
        checksums.set(true)
        mode.set(Signing.Mode.FILE)
        passphrase.set(if (hasProperty("centralPortalKeyPwd")) property("centralPortalKeyPwd") as String else "")
        publicKey.set(if (hasProperty("centralPortalPublicKey")) property("centralPortalPublicKey") as String else "")
        secretKey.set(if (hasProperty("centralPortalSecretKey")) property("centralPortalSecretKey") as String else "")
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.ALWAYS)
                    url = "https://central.sonatype.com/api/v1/publisher"
                    username.set(if (hasProperty("centralPortalUsr")) property("centralPortalUsr") as String else "")
                    password.set(if (hasProperty("centralPortalPwd")) property("centralPortalPwd") as String else "")
                    stagingRepository(layout.buildDirectory.dir("staging").get().asFile.path)
                }
            }
        }
    }
    release {
        github {
            enabled.set(false)
        }
    }
}