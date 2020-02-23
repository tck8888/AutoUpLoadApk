package com.tck.plugin.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 *
 * @author tck88*
 * @date 2020/2/22
 */
class JsonUtils {

    static Gson gson

    static Gson gson() {
        if (gson == null) {
            return new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
        }
        return gson
    }
}
