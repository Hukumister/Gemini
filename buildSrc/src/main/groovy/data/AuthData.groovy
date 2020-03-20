package data

import org.gradle.api.Project

class AuthData {

    final String user
    final String key

    static AuthData fromProject(final Project project) {
        def fileProp = tryToReadFromFile(project)
        String user
        String key
        if (fileProp.isEmpty()) {
            user = System.getenv('BINTRAY_USER')
            key = System.getenv('BINTRAY_API_KEY')

        } else {
            user = fileProp.getProperty('BINTRAY_USER')
            key = fileProp.getProperty('BINTRAY_API_KEY')
        }
        return new AuthData(user, key)
    }

    AuthData(user, key) {
        this.user = user
        this.key = key
    }

    private static def tryToReadFromFile(final Project project) {
        Properties properties = new Properties()
        def file = project.rootProject.file('local.properties')
        if (file.exists()) {
            properties.load(file.newInputStream())
        }
        return properties
    }
}