pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "gemini"
include(":gemini-core")
include(":gemini-core-test")
include(":gemini-binder")
include(":gemini-store-keeper")
include(":sample")

if (startParameter.projectProperties.containsKey("check_publication")) {
    include(":check-publication")
}

