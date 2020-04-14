package com.tck.plugin.fir

import com.tck.plugin.UpLoadApkConfigExtension

/**
 *
 * @author tck88*
 * @date 2020/2/12
 */
class UploadApkInfo {

    String appName
    String appPackageName
    String appVersion
    String appVersionCode

    String apkDownloadUrl
    String env

    UploadApkInfo(String apkFileName, UpLoadApkConfigExtension versionInfoBasicBean) {
        if (apkFileName.contains("uat")) {
            appPackageName = versionInfoBasicBean.getAppPackageName() + ".uat.v" + versionInfoBasicBean.getAppVersionCode();
            appName = versionInfoBasicBean.getAppName() + "-uat-" + versionInfoBasicBean.getAppVersionCode()
            env = "uat"
        } else if (apkFileName.contains("debug")) {
            appPackageName = versionInfoBasicBean.getAppPackageName() + ".test.v" + versionInfoBasicBean.getAppVersionCode()
            appName = versionInfoBasicBean.getAppName() + "-test-" + versionInfoBasicBean.getAppVersionCode()
            env = "test"
        } else {
            appPackageName = versionInfoBasicBean.getAppPackageName()
            appName = versionInfoBasicBean.getAppName()
            env = "release"
        }

        appVersion = versionInfoBasicBean.getAppVersion()
        appVersionCode = versionInfoBasicBean.getAppVersionCode()

    }

    void setApkDownloadUrl(String apkDownloadUrl) {
        this.apkDownloadUrl = "http://d.zqapps.com/" + apkDownloadUrl;
    }
}
