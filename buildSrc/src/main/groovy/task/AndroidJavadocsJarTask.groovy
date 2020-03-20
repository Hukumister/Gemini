package task

import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

class AndroidJavadocsJarTask extends Jar {

    @SuppressWarnings("GroovyAccessibility")
    AndroidJavadocsJarTask() {
        archiveClassifier.set("javadoc")

        def javadocTask = project.tasks.getByName("androidJavadocs") as Javadoc
        dependsOn(javadocTask)
        from(javadocTask.destinationDir)
    }
}