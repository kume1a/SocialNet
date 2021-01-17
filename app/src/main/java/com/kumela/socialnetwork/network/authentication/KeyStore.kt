package com.kumela.socialnetwork.network.authentication

import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * Created by Toko on 13,January,2021
 **/

class KeyStore(private val sharedPreferences: SharedPreferences) {

    fun getUserId(): Int {
        val token = readKey() ?: return -1

        val encodedPayload = token.split(tokenRegex)[1]

        val decodedPayloadBytes: ByteArray = Base64.decode(encodedPayload, Base64.URL_SAFE)
        val decodedPayload = String(decodedPayloadBytes, Charset.defaultCharset())

        return JSONObject(decodedPayload).getInt("userId")
    }

    fun saveKey(key: String) {
        sharedPreferences.edit().putString(KEY, key).apply()
    }

    fun readKey(): String? {
        return sharedPreferences.getString(KEY, null)
    }

    fun deleteKey() {
        sharedPreferences.edit().remove(KEY).apply()
    }

    companion object {
        private const val KEY = "key"

        private val tokenRegex = Regex("\\.")
    }
}