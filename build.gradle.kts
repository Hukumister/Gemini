buildscript {

    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.androidGradlePlugin)

        // For the library uploading to the Bintray
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.1")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
}
