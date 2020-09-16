package ktlint

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.register

class KtlintPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val ktlint: Configuration by target.configurations.creating
        target.dependencies {
            ktlint("com.pinterest:ktlint:0.38.1")
        }
        target.tasks.register<JavaExec>("ktlint") {
            description = "Check Kotlin code style."
            group = "verification"
            classpath = ktlint
            main = "com.pinterest.ktlint.Main"
            args(
                "src/**/*.kt",
                "--reporter=plain",
                "--reporter=checkstyle,output=${target.buildDir}/reports/checkstyle/ktlint-report.xml"
            )
        }
        target.tasks.register<JavaExec>("ktlintFormat") {
            description = "Fix Kotlin code style deviations."
            group = "formatting"
            classpath = ktlint
            main = "com.pinterest.ktlint.Main"
            args(
                "-F",
                "src/**/*.kt"
            )
        }
    }
}

