buildscript {

    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.androidGradlePlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
}
