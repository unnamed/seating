rootProject.name = "chairs"

arrayOf("plugin", "api", "runtime").forEach {
    include(it)
}

arrayOf(
    "1_8_R3"
).forEach {
    include(":runtime:adapt-v$it")
}
