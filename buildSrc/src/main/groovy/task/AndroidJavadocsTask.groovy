package task


import org.gradle.api.tasks.javadoc.Javadoc

class AndroidJavadocsTask extends Javadoc {

    @SuppressWarnings("GroovyAccessibility")
    AndroidJavadocsTask() {
        def androidExtension = project.extensions.getByName('android')

        // Append also the classpath and files for release library variants. This fixes the javadoc warnings.
        // Got it from here - https://github.com/novoda/bintray-release/pull/39/files
        def releaseVariantCompileProvider = androidExtension.libraryVariants.last().javaCompileProvider
        dependsOn(releaseVariantCompileProvider)
        if (!project.plugins.hasPlugin("org.jetbrains.kotlin.android")) {
            setSource(androidExtension.sourceSets.getByName("main").java.srcDirs)
        }
    }
}