package task

import org.gradle.jvm.tasks.Jar

class JavadocsJarTask extends Jar {

    @SuppressWarnings("GroovyAccessibility")
    JavadocsJarTask() {
        archiveClassifier.set("javadoc")

        def javadocTask = project.tasks.getByName("javadoc")

        dependsOn javadocTask
        from javadocTask.destinationDir
    }
}