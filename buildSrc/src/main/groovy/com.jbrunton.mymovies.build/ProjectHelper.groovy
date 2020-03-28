package com.jbrunton.mymovies.build

import org.gradle.api.Project

class ProjectHelper {
    // Determines whether a project is an Android project based on whether it has an Android plugin
    // applied. Note that it has to be used after plugins have been applied, i.e. either in
    // afterEvaluate or during actual task execution.
    static Boolean isAndroidProject(Project project) {
        return project.plugins.hasPlugin('com.android.application') ||
                project.plugins.hasPlugin('com.android.library')
    }
}
