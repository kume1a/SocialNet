package com.kumela.socialnetwork.network.authentication

import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Toko on 12,October,2020
 **/

class AuthCheckerUseCase(private val auth: FirebaseAuth) {
    fun isUserSignedIn() : Boolean {
        return auth.currentUser != null
    }

    fun getUId(): String? = auth.uid
}