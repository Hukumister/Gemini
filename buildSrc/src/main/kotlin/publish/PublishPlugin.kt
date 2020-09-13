package publish


import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import publish.data.AuthBintrayData
import publish.data.PublishingData
import java.util.*

class PublishPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply<MavenPublishPlugin>()
        project.apply<BintrayPlugin>()

        val publishingData = PublishingData.from(project)
        val authData = AuthBintrayData.from(project)

        setupBintrayPublishingInformation(project, publishingData, authData)
        setupBintrayPublishing(project)
    }

    private fun setupBintrayPublishingInformation(
        target: Project,
        publishingData: PublishingData,
        authData: AuthBintrayData
    ) {
        target.extensions.getByType(BintrayExtension::class.java).apply {
            user = authData.user
            key = authData.key
            pkg.apply {
                repo = publishingData.repoName
                name = target.name
                vcsUrl = publishingData.vcsUrl
                githubRepo = publishingData.githubRepo
                userOrg = publishingData.organization
                issueTrackerUrl = publishingData.issueTrackerUrl
                version.released = Date().toString()
                version.name = target.version as String
                setLicenses(publishingData.licenseName)
            }
        }
    }

    private fun setupBintrayPublishing(target: Project) {
        target.tasks.named(BintrayUploadTask.getTASK_NAME(), BintrayUploadTask::class.java) {
            doFirst {
                val publishing = project.extensions.getByType(PublishingExtension::class)
                // https://github.com/bintray/gradle-bintray-plugin/issues/229
                publishing.publications
                    .filterIsInstance<MavenPublication>()
                    .forEach { publication ->
                        val moduleFile = project.buildDir
                            .resolve("publications/${publication.name}/module.json")
                        if (moduleFile.exists()) {
                            publication.artifact(object : FileBasedMavenArtifact(moduleFile) {
                                override fun getDefaultExtension() = "module"
                            })
                        }
                    }
                // https://github.com/bintray/gradle-bintray-plugin/issues/256
                val publications = publishing.publications
                    .filterIsInstance<MavenPublication>()
                    .map {
                        logger.warn("Uploading artifact '${it.groupId}:${it.artifactId}:${it.version}' from publication '${it.name}'")
                        it.name
                    }
                    .toTypedArray()
                setPublications(*publications)
            }
        }
    }

}


