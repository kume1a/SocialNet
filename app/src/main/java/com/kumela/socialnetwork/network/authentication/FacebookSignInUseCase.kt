package com.kumela.socialnet.network.authentication

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kumela.socialnet.common.BaseObservable

/**
 * Created by Toko on 27,September,2020
 **/

class FacebookSignInUseCase(
    private val activity: AppCompatActivity,
    private val firebaseAuth: FirebaseAuth,
    private val loginManager: LoginManager,
    private val callbackManager: CallbackManager
) : BaseObservable<FacebookSignInUseCase.Listener>() {

    interface Listener {
        fun onFacebookSignInSuccess(firebaseUser: FirebaseUser)
        fun onFailure()
    }

    fun signInWithFacebook() {
        loginManager.logInWithReadPermissions(activity, listOf("email", "public_profile"))
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d(javaClass.simpleName, "onSuccess: onSuccess result = $result")
                if (result != null) {
                    firebaseAuthWithFacebook(result.accessToken)
                } else notifyFailure()
            }

            override fun onCancel() {
                Log.w(javaClass.simpleName, "onCancel: cancelled")
            }

            override fun onError(error: FacebookException?) {
                Log.e(javaClass.simpleName, "onError: called", error)
                notifyFailure()
            }
        })
    }

    private fun firebaseAuthWithFacebook(token: AccessToken) {
        Log.d(javaClass.simpleName, "firebaseAuthWithFacebook: idToken = $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "signInWithCredential:success")

                val user = firebaseAuth.currentUser
                if (user != null) {
                    notifySuccess(user)
                } else notifyFailure()
            }.addOnFailureListener {
                Log.e(javaClass.simpleName, "firebaseAuthWithFacebook: failed", it)
                notifyFailure()
            }
    }

    fun onFacebookSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun notifySuccess(firebaseUser: FirebaseUser) {
        for (listener in listeners) {
            listener.onFacebookSignInSuccess(firebaseUser)
        }
    }

    private fun notifyFailure() {
        for (listener in listeners) {
            listener.onFailure()
        }
    }
}