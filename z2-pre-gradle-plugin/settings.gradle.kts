rootProject.name = "z2-pre-gradle-plugin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("../z2-pre-core")