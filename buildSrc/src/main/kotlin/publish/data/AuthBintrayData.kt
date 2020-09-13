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
        private const val BINTRAY_API_KEY = "BINTRAY_API_KEY"

        fun from(project: Project): AuthBintrayData {
            val localProperties = LocalProperties(project)
            return AuthBintrayData(
                user = localProperties[BINTRAY_USER] ?: System.getProperty(BINTRAY_USER) ?: "",
                key = localProperties[BINTRAY_API_KEY] ?: System.getProperty(BINTRAY_API_KEY) ?: ""
            )
        }
    }
}
