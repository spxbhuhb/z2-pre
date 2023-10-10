rootProject.name = "z2-service-kotlin-plugin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

}

includeBuild("../../z2-commons/z2-commons-runtime")
includeBuild("../z2-service-runtime")