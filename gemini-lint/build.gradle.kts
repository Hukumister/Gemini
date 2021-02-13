plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compileOnly(Deps.lint.api)
    compileOnly(Deps.lint.checks)

    testImplementation(Deps.kotlinTestJunit)
    testImplementation(Deps.lint.core)
    testImplementation(Deps.lint.tests)
    testImplementation(Deps.testutils)
}

tasks.jar {
    manifest {
        attributes(
            "Lint-Registry-v3" to "com.haroncode.gemini.lint.GenimiIssueRegistry"
        )
    }
}
