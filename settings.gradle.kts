rootProject.name = "seating"

arrayOf("plugin", "api", "adapt").forEach {
    include(it)
}

arrayOf(
    "1_8_R3", "1_16_R3", "1_17_R1"
).forEach {
    include(":adapt:adapt-v$it")
}
