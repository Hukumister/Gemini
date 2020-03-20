package data

import org.gradle.api.Project

class PublishData {

    final String groupId
    final String licenseName
    final String vcsUrl
    final String issueTrackerUrl
    final String description
    final String repository

    static PublishData fromProject(final Project project) {
        return new PublishData(
                project.findProperty("GROUP"),
                project.findProperty("POM_LICENCE_NAME"),
                project.findProperty("POM_VSC_URL"),
                project.findProperty("POM_ISSUE_TRACKER_URL"),
                project.findProperty("POM_DESCRIPTION"),
                project.findProperty("POM_REPOSITORY_NAME"),
        )
    }

    PublishData(groupId, licenseName, vcsUrl, issueTrackerUrl, description, repository) {
        this.groupId = groupId
        this.licenseName = licenseName
        this.vcsUrl = vcsUrl
        this.issueTrackerUrl = issueTrackerUrl
        this.description = description
        this.repository = repository
    }
}