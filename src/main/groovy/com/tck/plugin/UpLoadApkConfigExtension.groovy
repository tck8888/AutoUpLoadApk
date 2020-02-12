package com.tck.plugin

class UpLoadApkConfigExtension {

    String appName
    String appPackageName
    String appVersion
    String appVersionCode
    String firToken
    String apkPath
    List<String> logs = new ArrayList<>()

    String getLogStr() {
        def builder = new StringBuilder()
        if (logs != null && !logs.isEmpty()) {
            logs.eachWithIndex { String log, int i ->
                builder.append(i + 1)
                        .append(". ")
                        .append(log)
                        .append("\n")
            }
        }

        if (builder.length() == 0) {
            builder.append("修复已知bug")
        }

        return builder.toString()
    }

    @Override
    String toString() {
        return """
上传配置如下：
    {
        "appName":"${appName}",
        "appPackageName":"${appPackageName}",
        "appVersion":"${appVersion}",
        "appVersionCode":"${appVersionCode}",
        "firToken":"${firToken}",
        "apkPath":"${apkPath}",
        "logs":${logs}
    }
               """
    }
}
