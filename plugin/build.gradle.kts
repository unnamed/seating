dependencies {
    api(project(":runtime"))
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    arrayOf("1_8_R3", "1_16_R3", "1_17_R1").forEach {
        runtimeOnly(project(":runtime:adapt-v$it"))
    }
}

tasks {
    processResources {
        filesMatching("**/*.yml") {
            filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf("version" to project.version)
            )
        }
    }

    register<Jar>("jar16") {
        description = "Builds this project using Java 16"
        archiveClassifier.set("all-java16")
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(file("src/main/resources").listFiles())

        java {
            toolchain {
                // use java 16 in this case
                languageVersion.set(JavaLanguageVersion.of(16))
            }
        }
    }

    register<Jar>("jar8") {
        description = "Builds this project using Java 8"
        archiveClassifier.set("all-java8")
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(file("src/main/resources").listFiles())

        java {
            toolchain {
                // use java 8 in this case
                languageVersion.set(JavaLanguageVersion.of(8))
            }
        }
    }
}