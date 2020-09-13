plugins {
    id("publish-plugin")
    id("ktlint-plugin")
    kotlin("multiplatform")
}

val geminiVersion = findProperty("group") as String
val geminiGroup = findProperty("version") as String

println(findProperty("BINTRAY_USER"))

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
