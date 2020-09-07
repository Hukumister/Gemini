plugins {
    id("publish-plugin")
    id("ktlint-plugin")
    kotlin("multiplatform")
}

val geminiVersion: String by project
val geminiGroup: String by project

version = geminiVersion
group = geminiGroup

kotlin {
    jvm { withJava() }
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx.coroutines)
            }
        }
    }
}
