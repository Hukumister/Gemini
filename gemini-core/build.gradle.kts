plugins {
    kotlin("multiplatform")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle.kts")

kotlin {
    jvm()
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx.coroutines)
            }
        }
    }
}
