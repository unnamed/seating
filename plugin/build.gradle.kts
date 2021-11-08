plugins {
    id("com.github.johnrengelman.shadow") version("7.0.0")
}

dependencies {
    api(project(":runtime"))
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    arrayOf("1_8_R3", "1_16_R3").forEach {
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
}