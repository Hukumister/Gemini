package publish.data

import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.useToRun
import java.io.FileInputStream
import java.util.*

data class AuthBintrayData(
    val user: String,
    val key: String
) {

    companion object {

        fun from(project: Project): AuthBintrayData {
            return readFromLocal(project) ?: AuthBintrayData(
                user = System.getProperty("BINTRAY_USER"),
                key = System.getProperty("BINTRAY_API_KEY")
            )
        }

        private fun readFromLocal(project: Project): AuthBintrayData? {
            val properties = Properties()
            val file = project.rootProject.file("local.properties")
            if (file.exists()) {
                FileInputStream(file).useToRun { properties.load(this) }
            }
            val user = properties.getProperty("BINTRAY_USER") ?: return null
            val key = properties.getProperty("BINTRAY_API_KEY") ?: return null
            return AuthBintrayData(
                user = user,
                key = key
            )
        }
    }
}
