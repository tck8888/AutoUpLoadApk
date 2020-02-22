package com.tck.plugin.fir

import com.google.gson.annotations.SerializedName

class FirUpLoadKeyBean {

    public String id
    public String type
    @SerializedName("short")
    public String shorts
    public CertBean cert

    static class CertBean {
        public IconBean icon
        public BinaryBean binary
        static class IconBean {
            public String key
            public String token
            public String upload_url
        }

        static class BinaryBean {
            public String key
            public String token
            public String upload_url
        }
    }
}
