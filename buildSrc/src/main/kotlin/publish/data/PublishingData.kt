package publish.data

import org.gradle.api.Project

data class PublishingData(
    val repoName: String,
    val vcsUrl: String,
    val githubRepo: String?,
    val issueTrackerUrl: String,
    val licenseName: String?,
    val organization: String?
) {

    companion object {

        fun from(project: Project): PublishingData = PublishingData(
            repoName = project.findProperty("publish.repository.name")?.toString().orEmpty(),
            vcsUrl = project.findProperty("publish.vsc.url")?.toString().orEmpty(),
            issueTrackerUrl = project.findProperty("publish.issue.tracker.url")?.toString().orEmpty(),
            licenseName = project.findProperty("publish.licence.name")?.toString(),
            githubRepo = project.findProperty("publish.github.repository")?.toString(),
            organization = project.findProperty("publish.organization")?.toString()
        )
    }
}
