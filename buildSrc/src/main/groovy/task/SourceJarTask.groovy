package task

import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar

class SourceJarTask extends Jar {

    @SuppressWarnings("GroovyAccessibility")
    SourceJarTask() {
        archiveClassifier.set("sources")

        def javaPlugin = project.convention.getPlugin(JavaPluginConvention.class)
        from javaPlugin.sourceSets.getByName("main").allSource
    }
}