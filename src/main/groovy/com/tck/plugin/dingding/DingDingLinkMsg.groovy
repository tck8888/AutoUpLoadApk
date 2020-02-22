package com.tck.plugin.dingding

/**
 *
 * @author tck88*
 * @date 2020/2/21
 */
class DingDingLinkMsg {

    public String msgtype
    public LinkBean link
    static class LinkBean {
        public String text
        public String title
        public String picUrl
        public String messageUrl
    }
}
