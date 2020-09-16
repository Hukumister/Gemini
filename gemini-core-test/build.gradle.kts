plugins {
    kotlin("multiplatform")
}

apply(from = "${project.rootDir}/сodequality/ktlint.gradle.kts")

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx.coroutines)
                implementation(project(":gemini-core"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Deps.kotlinTestJunit)
                implementation(Deps.kotlinx.coroutinesTest)
            }
        }
    }
}
