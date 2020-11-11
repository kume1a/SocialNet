package com.kumela.socialnet.ui.common.controllers

/**
 * Created by Toko on 24,September,2020
 **/

interface BackPressDispatcher {
    fun registerBackPressedListener(listener: BackPressedListener)
    fun unregisterBackPressedListener(listener: BackPressedListener)
}