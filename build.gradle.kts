plugins { java }

subprojects {
    apply(plugin = "java-library")

    tasks {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(16))
            }
        }
    }

    repositories {
        mavenLocal()
        maven("https://repo.unnamed.team/repository/unnamed-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://maven.enginehub.org/repo/")
        maven("https://repo.viaversion.com")
        mavenCentral()
    }
}
