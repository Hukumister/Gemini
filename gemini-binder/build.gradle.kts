plugins {
    id("ktlint-plugin")
    id("publish-plugin")
    kotlin("multiplatform")
    id("com.android.library")
}

val geminiVersion: String by project
val geminiGroup: String by project

version = geminiVersion
group = geminiGroup

tasks.register<Jar>("javadocJar") {
    archiveVersion.set(geminiVersion)
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.set("javadocJar")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("debug") {
                from(components["debug"])
                artifact(tasks.getByName("javadocJar"))
            }

            create<MavenPublication>("release") {
                from(components["release"])
                artifact(tasks.getByName("javadocJar"))
            }
        }
    }
}

kotlin {
    android()
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx.coroutines)
                api(project(":gemini-core"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.androidx.appCompat)
                implementation(Deps.androidx.lifecycleKtx)
            }
        }
    }
}

android {
    compileSdkVersion(Versions.android.compileSdk)
    buildToolsVersion(Versions.android.buildTools)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdkVersion(Versions.android.minSdk)
        targetSdkVersion(Versions.android.targetSdk)
    }
}

dependencies {
    implementation(Deps.kotlinx.coroutines)
    api(project(":gemini-core"))
}
