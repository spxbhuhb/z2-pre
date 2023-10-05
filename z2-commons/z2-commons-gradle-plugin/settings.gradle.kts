rootProject.name = "z2-commons-gradle-plugin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("../z2-commons-runtime")