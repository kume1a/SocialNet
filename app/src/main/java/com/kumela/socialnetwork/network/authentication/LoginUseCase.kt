package com.kumela.socialnet.network.authentication

import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kumela.socialnet.common.BaseObservable

/**
 * Created by Toko on 26,September,2020
 **/

class LoginUseCase(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase
) : BaseObservable<LoginUseCase.Listener>(),
    GoogleSignInUseCase.Listener,
    FacebookSignInUseCase.Listener {

    interface Listener {
        fun onSignInCompleted(firebaseUser: FirebaseUser)
        fun onSignInFailed()

        fun onFacebookSignInCompleted(firebaseUser: FirebaseUser)
        fun onGoogleSignInCompleted(firebaseUser: FirebaseUser)
    }

    init {
        googleSignInUseCase.registerListener(this)
        facebookSignInUseCase.registerListener(this)
    }

    fun loginAndNotify(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    notifySuccess(user)
                } else notifyFailure()
            }.addOnFailureListener {
                Log.e(javaClass.simpleName, "loginAndNotify: failed", it)
                notifyFailure()
            }
    }

    fun signInWithGoogleAndNotify() {
        googleSignInUseCase.signInWithGoogle()
    }

    fun onGoogleSignInResult(data: Intent?) {
        googleSignInUseCase.onGoogleSignInResult(data)
    }

    fun signInWithFacebookAndNotify() {
        facebookSignInUseCase.signInWithFacebook()
    }

    fun onFacebookSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookSignInUseCase.onFacebookSignInResult(requestCode, resultCode, data)
    }

    // callbacks from facebook and google use cases
    override fun onGoogleSignInSuccess(firebaseUser: FirebaseUser) {
        for (listener in listeners) {
            listener.onGoogleSignInCompleted(firebaseUser)
        }
    }

    override fun onFacebookSignInSuccess(firebaseUser: FirebaseUser) {
        for (listener in listeners) {
            listener.onFacebookSignInCompleted(firebaseUser)
        }
    }

    override fun onFailure() {
        notifyFailure()
    }

    private fun notifySuccess(firebaseUser: FirebaseUser) {
        for (listener in listeners) {
            listener.onSignInCompleted(firebaseUser)
        }
    }

    private fun notifyFailure() {
        for (listener in listeners) {
            listener.onSignInFailed()
        }
    }
}