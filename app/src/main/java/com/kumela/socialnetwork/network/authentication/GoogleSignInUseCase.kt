package com.kumela.socialnet.network.authentication

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kumela.socialnet.R
import com.kumela.socialnet.common.BaseObservable
import com.kumela.socialnet.ui.register.RegisterFragment.Companion.RC_GOOGLE_SIGN_IN


/**
 * Created by Toko on 27,September,2020
 **/

class GoogleSignInUseCase(
    private val activity: AppCompatActivity,
    private val firebaseAuth: FirebaseAuth
) : BaseObservable<GoogleSignInUseCase.Listener>() {

    interface Listener {
        fun onGoogleSignInSuccess(firebaseUser: FirebaseUser)
        fun onFailure()
    }

    fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.oauth_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {authResult ->
                Log.d(javaClass.simpleName, "firebaseAuthWithGoogle: success")

                val user = authResult.user
                if (user != null) {
                    notifySuccess(user)
                } else notifyFailure()
            }.addOnFailureListener {
                Log.e(javaClass.simpleName, "firebaseAuthWithGoogle: failed", it)
                notifyFailure()
            }
    }

    fun onGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d(javaClass.simpleName, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(javaClass.simpleName, "Google sign in failed", e)
            notifyFailure()
        }
    }

    private fun notifySuccess(firebaseUser: FirebaseUser) {
        for (listener in listeners) {
            listener.onGoogleSignInSuccess(firebaseUser)
        }
    }

    private fun notifyFailure() {
        for (listener in listeners) {
            listener.onFailure()
        }
    }
}
