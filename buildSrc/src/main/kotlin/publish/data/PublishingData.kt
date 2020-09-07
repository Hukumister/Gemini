package publish.data

import org.gradle.api.Project

data class PublishingData(
    val repoName: String,
    val vcsUrl: String,
    val githubRepo: String?,
    val issueTrackerUrl: String,

    val licenseName: String?,
    val organization: String?,
    val versionName: String?
) {

    companion object {

        fun from(project: Project): PublishingData = PublishingData(
            repoName = project.findProperty("repository.name")?.toString() ?: "",
            vcsUrl = project.findProperty("vsc.url")?.toString() ?: "",
            issueTrackerUrl = project.findProperty("issue.tracker.url")?.toString() ?: "",
            licenseName = project.findProperty("licence.name")?.toString(),
            githubRepo = project.findProperty("github.repository")?.toString(),
            versionName = project.findProperty("github.repository")?.toString(),
            organization = project.findProperty("organization")?.toString()
        )
    }

}
