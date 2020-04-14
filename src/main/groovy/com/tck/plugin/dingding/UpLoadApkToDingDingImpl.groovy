package com.tck.plugin.dingding

import com.tck.plugin.UpLoadApkConfigExtension
import com.tck.plugin.fir.UploadApkInfo
import com.tck.plugin.utils.JsonUtils
import com.tck.plugin.utils.YLogUtils
import groovy.json.JsonOutput
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

/**
 *
 * @author tck88*
 * @date 2020/2/22
 */
class UpLoadApkToDingDingImpl {

    OkHttpClient okHttpClient = new OkHttpClient()

    UpLoadApkConfigExtension upLoadApkConfigExtension
    List<UploadApkInfo> uploadApkInfoList

    UpLoadApkToDingDingImpl(List<UploadApkInfo> uploadApkInfoList,
                            UpLoadApkConfigExtension upLoadApkConfigExtension) {
        this.uploadApkInfoList = uploadApkInfoList
        this.upLoadApkConfigExtension = upLoadApkConfigExtension
    }

    void sendDingDing() {
        if (uploadApkInfoList == null || uploadApkInfoList.isEmpty()) {
            YLogUtils.log("要上传的apk信息为空")
            return
        }

        if (upLoadApkConfigExtension == null) {
            YLogUtils.log("UpLoadApkConfigExtension==null")
            return
        }

        if (upLoadApkConfigExtension.dingding == null || upLoadApkConfigExtension.dingding.isEmpty()) {
            YLogUtils.log("upLoadApkConfigExtension.dingding == null||upLoadApkConfigExtension.dingding.isEmpty()")
            return
        }

        YLogUtils.log("开始发送钉钉消息")

        upLoadApkConfigExtension.dingding.each {
            String group = it.key
            String groupUrl = it.value
            if (groupUrl != null && !groupUrl.isEmpty()) {
                YLogUtils.log("即将要发送到：\n钉钉群：${group}\nWebHook地址：${groupUrl}")

                uploadApkInfoList.each { uploadApkInfo ->
                    DingDingLinkMsg dingDingLinkMsg = new DingDingLinkMsg()
                    dingDingLinkMsg.msgtype = "link"

                    LinkBean linkBean = new LinkBean()
                    linkBean.messageUrl = uploadApkInfo.getApkDownloadUrl()
                    linkBean.picUrl = upLoadApkConfigExtension.appLogo
                    linkBean.title = upLoadApkConfigExtension.appName + "-安卓" + uploadApkInfo.env + "-" + uploadApkInfo.appVersionCode
                    linkBean.text = "更新内容：\n ${upLoadApkConfigExtension.getLogStr()}"
                    dingDingLinkMsg.link = linkBean
                    String message = JsonUtils.gson().toJson(dingDingLinkMsg)

                    println("消息类容：\n${message}")

                    Request request = new Request.Builder()
                            .url(groupUrl)
                            .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                                    message))
                            .build()
                    try {
                        Response response = okHttpClient.newCall(request).execute()
                        if (response.isSuccessful()) {
                            YLogUtils.log("发送钉钉消息成功:${JsonOutput.prettyPrint(response.body().string())}")
                        } else {
                            YLogUtils.log("发送钉钉消息失败:${response.body().toString()}")
                        }
                    } catch (Exception e) {
                        e.printStackTrace()
                        YLogUtils.log("发送钉钉消息失败:${e.getMessage()}")
                    }
                }
            }
        }

        YLogUtils.log("发送钉钉消息完毕")
    }

}
