package publish.data

import org.gradle.api.Project
import publish.util.LocalProperties

data class AuthBintrayData(
    val user: String,
    val key: String
) {

    val isEmpty: Boolean
        get() = user.isEmpty() && key.isEmpty()

    companion object {

        private const val BINTRAY_USER = "BINTRAY_USER"
        private const val BINTRAY_KEY = "BINTRAY_KEY"

        fun from(project: Project): AuthBintrayData {
            val localProperties = LocalProperties(project)
            return AuthBintrayData(
                user = localProperties[BINTRAY_USER] ?: project.findProperty(BINTRAY_USER)?.toString().orEmpty(),
                key = localProperties[BINTRAY_KEY] ?: project.findProperty(BINTRAY_KEY)?.toString().orEmpty()
            )
        }
    }
}
