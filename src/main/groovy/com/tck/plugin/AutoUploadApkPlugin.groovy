package com.tck.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project

import com.tck.plugin.fir.UpLoadApkToFirTask

class AutoUploadApkPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("upLoadApkConfigExtension", UpLoadApkConfigExtension)

        //assembleUat assembleDebug
//        project.tasks
//                .create('uploadApkTask', UpLoadApkToFirTask.class)
//                .dependsOn("assembleDebug")
        project.tasks
                .create('uploadApkTask', UpLoadApkToFirTask.class)
    }
}
