dependencies {
    api(project(":runtime"))
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    arrayOf("1_8_R3", "1_16_R3", "1_17_R1").forEach {
        runtimeOnly(project(":runtime:adapt-v$it"))
    }
}

tasks {
    register<Jar>("jar16") {
        filesMatching("**/*.yml") {
            filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf("version" to project.version)
            )
        }

        description = "Builds this project using Java 16"
        archiveClassifier.set("all-java16")
        from(project.sourceSets.main.get().output)
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

        java {
            toolchain {
                // use java 16 in this case
                languageVersion.set(JavaLanguageVersion.of(16))
            }
        }
    }

    register<Jar>("jar8") {
        filesMatching("**/*.yml") {
            filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf("version" to project.version)
            )
        }

        description = "Builds this project using Java 8"
        archiveClassifier.set("all-java8")
        from(project.sourceSets.main.get().output)
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

        java {
            toolchain {
                // use java 8 in this case
                languageVersion.set(JavaLanguageVersion.of(8))
            }
        }
    }
}