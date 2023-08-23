rootProject.name = "z2-schematic-gradle-plugin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("../z2-schematic-runtime")