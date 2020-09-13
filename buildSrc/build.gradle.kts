plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation(Deps.bintray)
    compileOnly(Deps.androidGradlePlugin)
}

kotlin {
    // Add Deps to compilation, so it will become available in main project
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}

gradlePlugin {
    plugins.register("publish-plugin") {
        id = "publish-plugin"
        implementationClass = "publish.PublishPlugin"
    }
}

gradlePlugin {
    plugins.register("ktlint-plugin") {
        id = "ktlint-plugin"
        implementationClass = "ktlint.KtlintPlugin"
    }
}
