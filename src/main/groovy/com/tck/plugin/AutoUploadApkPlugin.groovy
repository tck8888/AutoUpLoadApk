package com.tck.plugin

import com.tck.plugin.fir.UpLoadApkToFirTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoUploadApkPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("upLoadApkConfigExtension", UpLoadApkConfigExtension)

        project.tasks
                .create('uploadApkTask', UpLoadApkToFirTask)
                .dependsOn("assembleDebug", "assembleUat")

    }
}
