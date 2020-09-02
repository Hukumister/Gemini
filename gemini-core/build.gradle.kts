plugins {
    kotlin("multiplatform")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle")

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
