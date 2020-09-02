val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.38.1")
}

tasks.register<JavaExec>("ktlint") {
    description = "Check Kotlin code style."
    group = "verification"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args(
        "src/**/*.kt",
        "--reporter=plain",
        "--reporter=checkstyle,output=${buildDir}/reports/checkstyle/ktlint-report.xml"
    )
}

tasks.register<JavaExec>("ktlintFormat") {
    description = "Fix Kotlin code style deviations."
    group = "formatting"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args(
        "-F",
        "src/**/*.kt"
    )
}
