subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenLocal()
        maven("https://repo.codemc.io/repository/nms/")
        mavenCentral()
    }
}
