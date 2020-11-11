package com.kumela.socialnet.common.listeners

/**
 * Created by Toko on 29,October,2020
 **/

fun interface OnSuccessListener<in T> {
    fun onSuccess(data: T)
}