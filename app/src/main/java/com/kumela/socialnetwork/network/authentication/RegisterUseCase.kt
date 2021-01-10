package com.kumela.socialnetwork.network.authentication

import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kumela.socialnetwork.common.BaseObservable

/**
 * Created by Toko on 26,September,2020
 **/

class RegisterUseCase(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase
) : BaseObservable<RegisterUseCase.Listener>(), GoogleSignInUseCase.Listener,
    FacebookSignInUseCase.Listener {

    interface Listener {
        fun onRegisterCompleted(firebaseUser: FirebaseUser)
        fun onRegisterFailed()

        fun onGoogleSignInCompleted(firebaseUser: FirebaseUser)
        fun onFacebookSignInCompleted(firebaseUser: FirebaseUser)
    }

    init {
        googleSignInUseCase.registerListener(this)
        facebookSignInUseCase.registerListener(this)
    }

    fun registerAndNotify(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Log.d(javaClass.simpleName, "registerAndNotify: registration completed")

                val user = authResult.user
                if (user != null) {
                    for (listener in listeners) {
                        listener.onRegisterCompleted(user)
                    }
                } else notifyFailure()
            }.addOnFailureListener {
                Log.e(javaClass.simpleName, "registerAndNotify: failed", it)
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

    // callbacks from google and facebook use cases
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

    private fun notifyFailure() {
        for (listener in listeners) {
            listener.onRegisterFailed()
        }
    }
}