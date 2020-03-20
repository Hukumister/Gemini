package task

import org.gradle.jvm.tasks.Jar
import com.android.build.gradle.LibraryExtension

class AndroidSourcesJarTack extends Jar {

    @SuppressWarnings("GroovyAccessibility")
    AndroidSourcesJarTack() {
        archiveClassifier.set("sources")

        def androidExtension = project.extensions.getByName('android')
        from androidExtension.sourceSets.getByName("main").java.srcDirs
    }
}