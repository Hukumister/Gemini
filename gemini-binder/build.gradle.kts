plugins {
    kotlin("multiplatform")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle")

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx.coroutines)
                api(project(":gemini-core"))
            }
        }
    }
}
