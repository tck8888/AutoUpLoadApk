package com.tck.plugin.fir

import com.google.gson.Gson
import com.tck.plugin.UpLoadApkConfigExtension
import groovy.json.JsonOutput
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

/**
 *
 * @author tck88*
 * @date 2020/2/12
 */
class UpLoadApkToFirTaskImpl {

    static String UPLOAD_URL = "http://api.fir.im/apps"
    static MediaType mediaType = MediaType.parse("application/json")

    static Gson gson = new Gson()
    static OkHttpClient okHttpClient = new OkHttpClient()

    List<UploadApkInfo> uploadApkInfos = new ArrayList<>()
    List<File> apkFiles = new ArrayList<>()
    UpLoadApkConfigExtension upLoadApkConfigExtension

    UpLoadApkToFirTaskImpl(UpLoadApkConfigExtension upLoadApkConfigExtension) {
        this.upLoadApkConfigExtension = upLoadApkConfigExtension
        println upLoadApkConfigExtension.toString()
    }

    public void startUpload() {
        initApkFiles(new File(upLoadApkConfigExtension.apkPath))
        if (apkFiles.isEmpty()) {
            log("目录:${upLoadApkConfigExtension.apkPath} 不存在apk")
            return
        }

        apkFiles.each {

            println "准备上传:${it.getAbsolutePath()}"

            UploadApkInfo uploadApkInfo = new UploadApkInfo(it.getName(), upLoadApkConfigExtension)

            println "请求开始：获取fir上传token"
            FirUpLoadKeyBean uploadKey = getUploadKey(uploadApkInfo.getAppPackageName(), upLoadApkConfigExtension.getFirToken())
            println "请求结束：获取fir上传token"

            if (uploadKey != null) {
                println "请求开始：上传apk"
                int code = uploadApk(uploadKey, uploadApkInfo, it, upLoadApkConfigExtension.getLogStr())
                println "请求结束：上传apk"
                if (code == 1) {
                    uploadApkInfo.setApkDownloadUrl(uploadKey.shorts)
                    uploadApkInfos.add(uploadApkInfo)
                }
            }
        }


        if (!uploadApkInfos.isEmpty()) {
            println "fir上传结束，结果如下："
            println JsonOutput.prettyPrint(JsonOutput.toJson(uploadApkInfos))
        } else {
            println "fir上传结束，结果如下：失败"
        }

    }

    void initApkFiles(File file) {
        if (file == null || !file.exists()) {
            return
        }

        if (file.isFile()) {
            def apk = isApk(file)
            if (apk) {
                apkFiles.add(file)
            }
        } else {
            def listFiles = file.listFiles()
            if (listFiles != null && listFiles.length > 0) {
                listFiles.each {
                    if (it.isDirectory()) {
                        initApkFiles(it)
                    } else {
                        def apk = isApk(it)
                        if (apk) {
                            apkFiles.add(it)
                        }
                    }
                }
            }
        }
    }

    static boolean isApk(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false
        }
        if (file.length() == 0) {
            return false
        }
        return file.getName().endsWith(".apk")
    }

    static FirUpLoadKeyBean getUploadKey(String packageName, String firToken) {
        Map<String, String> uploadKey = new HashMap<String, String>()
        uploadKey.put("type", "android")
        uploadKey.put("bundle_id", packageName)
        uploadKey.put("api_token", firToken)

        def requestParameter = gson.toJson(uploadKey)

        println("请求地址：${UPLOAD_URL}")
        println("请求参数：${JsonOutput.prettyPrint(requestParameter)}")

        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(RequestBody.create(mediaType, requestParameter))
                .build()
        try {
            Response response = okHttpClient.newCall(request).execute()
            if (response.code() == 201) {
                if (response.body() != null) {
                    def json = response.body().string()

                    println JsonOutput.prettyPrint(json)

                    return gson.fromJson(json, FirUpLoadKeyBean.class)
                } else {
                    println "获取不到数据"
                }
            } else {
                println "firToken过期"
            }
        } catch (IOException e) {
            println "网络异常:${e.getMessage()}"
        }
    }

    static int uploadApk(FirUpLoadKeyBean uploadKey,
                         UploadApkInfo uploadApkInfo,
                         File file,
                         String logs) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", uploadKey.cert.binary.key)
                .addFormDataPart("token", uploadKey.cert.binary.token)
                .addFormDataPart("x:name", uploadApkInfo.getAppName())
                .addFormDataPart("x:version", uploadApkInfo.getAppVersion())
                .addFormDataPart("x:build", uploadApkInfo.getAppVersionCode())
                .addFormDataPart("x:release_type", "")
                .addFormDataPart("x:changelog", logs)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build()

        println "请求地址：${uploadKey.cert.binary.upload_url}"

        Request request = new Request.Builder()
                .url(uploadKey.cert.binary.upload_url)
                .post(multipartBody)
                .build()

        try {
            Response response = okHttpClient.newCall(request).execute()
            if (response.code() == 200) {
                println "上传成功：" + JsonOutput.prettyPrint(response.body().string())
                return 1
            } else {
                println "上传失败:" + response.toString()
            }
        } catch (IOException e) {
            println "网络异常:${e.getMessage()}"
        }
        return -1
    }

    static void log(String str) {
        println "****************************************************************"
        println str
        println "****************************************************************"
    }
}
