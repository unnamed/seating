plugins {
    id("com.github.johnrengelman.shadow") version("7.0.0")
}

dependencies {
    api(project(":adapt"))
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")


    arrayOf("1_16_R3", "1_8_R3").forEach {
        runtimeOnly(project(":adapt:adapt-v$it"))
    }
    if (project.property("java") == "16") {
        arrayOf("1_17_R1").forEach {
            runtimeOnly(project(":adapt:adapt-v$it"))
        }
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