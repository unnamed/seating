rootProject.name = "chairs"

arrayOf("plugin", "api", "runtime").forEach {
    include(it)
}

arrayOf(
    "1_8_R3", "1_16_R3", "1_17_R1"
).forEach {
    include(":runtime:adapt-v$it")
}
