package com.kumela.socialnetwork.network.authentication

import android.content.Context

/**
 * Created by Toko on 13,January,2021
 **/

class KeyStore(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun saveKey(key: String) {
        sharedPreferences.edit().putString(KEY, key).apply()
    }

    fun readKey(): String? {
        return sharedPreferences.getString(KEY, null)
    }

    companion object {
        private const val NAME = "token"
        private const val KEY = "key"
    }
}