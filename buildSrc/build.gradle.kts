buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.1")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

plugins {
    `kotlin-dsl`
    groovy
    `java-library`
    `java-gradle-plugin`
    "com.jfrog.bintray"
    "com.github.dcendents.android-maven"
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    implementation("com.github.dcendents:android-maven-gradle-plugin:2.1")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.1")

    compileOnly("com.android.tools.build:gradle:4.0.1")
}
