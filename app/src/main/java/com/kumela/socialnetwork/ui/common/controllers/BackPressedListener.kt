package com.kumela.socialnet.ui.common.controllers

/**
 * Created by Toko on 24,September,2020
 **/

interface BackPressedListener {
    /**
     * @return true if the listener handled the back press; false otherwise
     */
    fun onBackPressed(): Boolean
}