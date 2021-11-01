plugins { java }

subprojects {
    apply(plugin = "java-library")

    tasks {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(8))
            }
        }
    }

    repositories {
        mavenLocal()
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://maven.enginehub.org/repo/")
        mavenCentral()
    }
}
