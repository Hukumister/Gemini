plugins {
    id("com.android.library")
    kotlin("android")
    id("ktlint-plugin")
    id("publish-plugin")
}

android {
    compileSdkVersion(Versions.android.compileSdk)
    buildToolsVersion(Versions.android.buildTools)

    defaultConfig {
        minSdkVersion(Versions.android.minSdk)
    }

    libraryVariants.all {
        generateBuildConfigProvider.configure { enabled = false }
    }
}

dependencies {
    implementation(Deps.kotlinx.coroutines)
    implementation(Deps.androidx.viewModel)
    api(project(":gemini-core"))
}

val geminiGroup = findProperty("group") as String
val geminiVersion = findProperty("version") as String

version = geminiVersion
group = geminiGroup

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("gemini-store-keeper") {
                groupId = geminiGroup
                artifactId = "gemini-store-keeper"
                version = geminiVersion

                from(components["release"])

                artifact(sourcesJar)
            }
        }
    }
}
