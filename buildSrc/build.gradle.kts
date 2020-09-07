plugins {
    groovy
    `kotlin-dsl`
    id("java-library")
    id("java-gradle-plugin")
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    compileOnly("com.android.tools.build:gradle:4.0.1")
}

gradlePlugin {
    plugins.register("publish-plugin") {
        id = "publish-plugin"
        implementationClass = "publish.PublishPlugin"
    }
}

gradlePlugin {
    plugins.register("ktlint-plugin") {
        id = "ktlint-plugin"
        implementationClass = "ktlint.KtlintPlugin"
    }
}
