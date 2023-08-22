plugins {
    kotlin("multiplatform")
    id("convention.publication")
}

val geminiGroup = findProperty("group") as String
val geminiVersion = findProperty("version") as String

version = geminiVersion
group = geminiGroup

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutines)
            }
        }
    }
}
