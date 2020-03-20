import task.AndroidJavadocsJarTask
import task.AndroidJavadocsTask
import task.AndroidSourcesJarTack
import data.AuthData
import data.PublishData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import task.JavadocsJarTask
import task.SourceJarTask

class GeminiPublishingPlugin implements Plugin<Project> {

    void apply(Project project) {

        def extension = project.extensions.create('GeminiPublishing', GeminiPublishingPluginExtension)

        project.plugins.apply("com.jfrog.bintray")
        project.plugins.apply("com.github.dcendents.android-maven")

        configure(project)

        project.afterEvaluate {

            def authData = AuthData.fromProject(project)
            def publishData = PublishData.fromProject(project)

            project.version = extension.version
            project.group = publishData.groupId

            project.install {
                repositories.mavenInstaller {

                    // This generates POM.xml with proper parameters
                    pom.project {
                        groupId publishData.groupId
                        artifactId extension.artifactId
                    }
                }
            }

            project.bintray {

                user = authData.user
                key = authData.key

                configurations = [Dependency.ARCHIVES_CONFIGURATION]

                pkg {
                    name = extension.artifactId
                    repo = publishData.repository
                    licenses = [publishData.licenseName]
                    vcsUrl = publishData.vcsUrl
                    issueTrackerUrl = publishData.issueTrackerUrl

                    publish = true

                    version {
                        name = extension.version
                        released = new Date()
                    }
                }
            }
        }

    }

    private static def configure(Project project) {
        if (project.plugins.hasPlugin("java-library")) {

            def sourcesJar = project.tasks.register("sourcesJar", SourceJarTask.class)
            def javadocsJar = project.tasks.register("javadocsJar", JavadocsJarTask.class)

            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, sourcesJar)
            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, javadocsJar)
        } else if (project.plugins.hasPlugin("com.android.library")) {

            project.tasks.register("androidJavadocs", AndroidJavadocsTask.class)

            def androidJavadocsJar = project.tasks.register("androidJavadocsJar", AndroidJavadocsJarTask.class)
            def androidSourcesJar = project.tasks.register("androidSourcesJar", AndroidSourcesJarTack.class)

            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, androidJavadocsJar)
            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, androidSourcesJar)
        }
    }
}