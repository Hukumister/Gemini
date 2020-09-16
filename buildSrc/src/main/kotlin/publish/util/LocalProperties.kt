package publish.util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.useToRun
import java.io.FileInputStream
import java.util.*

class LocalProperties(project: Project) {

    private val properties: Properties = Properties()

    init {
        val file = project.rootProject.file("local.properties")
        if (file.exists()) {
            FileInputStream(file).useToRun { properties.load(this) }
        }
    }

    operator fun get(key: String): String? = properties.getProperty(key)
}
