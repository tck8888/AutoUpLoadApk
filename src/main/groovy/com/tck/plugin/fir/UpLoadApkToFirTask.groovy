package com.tck.plugin.fir

import com.tck.plugin.UpLoadApkConfigExtension
import com.tck.plugin.dingding.UpLoadApkToDingDingImpl
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UpLoadApkToFirTask extends DefaultTask {

    UpLoadApkToFirTask() {
        group = 'tck'
        description = 'upload apk to fir'
    }

    @TaskAction
    void upLoadApkToFir() {
        println "****************************************************************"
        println "****************************************************************"
        println "                                                                "
        println "                                                                "
        println "                欢迎使用apk上传到fir插件                    "
        println "                                                                "
        println "                                                                "
        println "****************************************************************"
        println "****************************************************************"

        UpLoadApkConfigExtension upLoadApkConfigExtension = project.extensions.upLoadApkConfigExtension

        def isValid = checkUpLoadApkConfigExtension(upLoadApkConfigExtension)

        if (!isValid) {
            return
        }
        def firTaskImpl = new UpLoadApkToFirTaskImpl(upLoadApkConfigExtension)
        //上传到fir
        List<UploadApkInfo> uploadApkInfoList = firTaskImpl.startUpload()

        if (uploadApkInfoList != null && !uploadApkInfoList.isEmpty()) {
            //发送钉钉消息
            if (upLoadApkConfigExtension.dingding != null && !upLoadApkConfigExtension.dingding.isEmpty()) {
                new UpLoadApkToDingDingImpl(uploadApkInfoList, upLoadApkConfigExtension).sendDingDing()
            }
        }
    }

    static boolean checkUpLoadApkConfigExtension(UpLoadApkConfigExtension extension) {
        if (extension == null) {
            println("upLoadApkConfigExtension 配置参数不存在")
            return false
        }

        def appName = extension.appName
        def appPackageName = extension.appPackageName
        def appVersion = extension.appVersion
        def appVersionCode = extension.appVersionCode

        def firToken = extension.firToken
        def apkPath = extension.apkPath

        if (appName == null || appName.isEmpty()) {
            println("appName为空")
            return false
        }

        if (appPackageName == null || appPackageName.isEmpty()) {
            println("appPackageName为空")
            return false
        }

        if (appVersion == null || appVersion.isEmpty()) {
            println("firToken为空")
            return false
        }
        if (appVersionCode == null || appVersionCode.isEmpty()) {
            println("appVersionCode为空")
            return false
        }

        if (firToken == null || firToken.isEmpty()) {
            println("firToken为空")
            return false
        }

        if (apkPath == null || apkPath.isEmpty()) {
            println("apkPath为空")
            return false
        }

        return true
    }


}
