package com.kumela.socialnetwork.network.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kumela.socialnetwork.common.BaseObservable

/**
 * Created by Toko on 26,September,2020
 **/

class AuthUseCase(private val firebaseAuth: FirebaseAuth) :
    BaseObservable<AuthUseCase.Listener>() {

    interface Listener {
        fun onSigninCompleted(firebaseUser: FirebaseUser)
        fun onSigninFailed()

        fun onSignupCompleted(firebaseUser: FirebaseUser)
        fun onSignupFailed()
    }

    fun signinAndNotify(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    for (listener in listeners) {
                        listener.onSigninCompleted(user)
                    }
                } else {
                    for (listener in listeners) {
                        listener.onSigninFailed()
                    }
                }
            }.addOnFailureListener {
                Log.e(javaClass.simpleName, "loginAndNotify: failed", it)
                for (listener in listeners) {
                    listener.onSigninFailed()
                }
            }
    }

    fun signupAndNotify(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    for (listener in listeners) {
                        listener.onSignupCompleted(user)
                    }
                } else {
                    for (listener in listeners) {
                        listener.onSignupFailed()
                    }
                }
            }.addOnFailureListener {
                Log.e(javaClass.simpleName, "registerAndNotify: failed", it)
                for (listener in listeners) {
                    listener.onSignupFailed()
                }
            }
    }

}