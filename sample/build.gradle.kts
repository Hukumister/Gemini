plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.haroncode.gemini.sample"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.haroncode.gemini.sample"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.ktx)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.material)
    implementation(libs.constraint)

    implementation(libs.toothpick)
    kapt(libs.toothpick.compiler)

    implementation(libs.cicerone)
    implementation(libs.timber)
    implementation(project(":gemini-binder"))
}
