plugins {
    id("ktlint-plugin")
    kotlin("multiplatform")
}

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
