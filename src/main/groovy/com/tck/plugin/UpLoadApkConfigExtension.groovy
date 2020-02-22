package com.tck.plugin

class UpLoadApkConfigExtension implements Serializable{

    String appName
    String appPackageName
    String appVersion
    String appVersionCode
    String firToken
    String apkPath
    List<String> logs = new ArrayList<>()

    Map<String, String> dingding = new HashMap<>()

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

//
//    @Override
//     String toString() {
//        return "\n{" +
//                "appName:" + appName + '\n' +
//                "appPackageName:" + appPackageName + '\n' +
//                "appVersion:" + appVersion + '\n' +
//                "appVersionCode:" + appVersionCode + '\n' +
//                "firToken:" + firToken + '\n' +
//                "apkPath:" + apkPath + '\n' +
//                "logs:" + logs +'\n'+
//                "dingding:" + dingding +
//                '\n}'
//    }

    @Override
    public String toString() {
        return """
{
    "appName":"$appName", 
    "appPackageName":"$appPackageName", 
    "appVersion":"$appVersion", 
    "appVersionCode":"$appVersionCode", 
    "firToken":"$firToken", 
    "apkPath":"$apkPath", 
    "logs":$logs, 
    "dingding":$dingding
}"""
    }
}
