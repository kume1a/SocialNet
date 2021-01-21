package com.kumela.socialnetwork.network.common

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException


/**
 * Created by Toko on 21,January,2021
 **/

class GsonConverters {
    fun getBooleanToIntAdapter(): TypeAdapter<Boolean> {
        return object : TypeAdapter<Boolean>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter?, value: Boolean?) {
                if (value == null) {
                    out?.nullValue()
                } else {
                    out?.value(value)
                }
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader?): Boolean {
                val peek: JsonToken? = `in`?.peek()
                return when (peek) {
                    JsonToken.BOOLEAN -> `in`.nextBoolean()
                    JsonToken.NULL -> {
                        `in`.nextNull()
                        false
                    }
                    JsonToken.NUMBER -> `in`.nextInt() != 0
                    JsonToken.STRING -> java.lang.Boolean.parseBoolean(`in`.nextString())
                    else -> throw IllegalStateException("Expected BOOLEAN or NUMBER but was $peek")
                }
            }
        }
    }
}
