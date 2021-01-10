package com.kumela.socialnetwork.ui.common.controllers

/**
 * Created by Toko on 27,September,2020
 **/

interface RequestResultDispatcher {
    fun registerRequestResultListener(listener: ActivityResultListener)
    fun unregisterRequestResultListener(listener: ActivityResultListener)
}