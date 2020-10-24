rootProject.name = "mrt"

include(":mrt-core")
include(":mrt-web")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}
