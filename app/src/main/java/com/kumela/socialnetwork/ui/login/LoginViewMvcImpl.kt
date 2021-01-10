package com.kumela.socialnetwork.ui.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc

/**
 * Created by Toko on 10,September,2020
 **/

class LoginViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<LoginViewMvc.Listener>(
    inflater, parent, R.layout.fragment_login
), LoginViewMvc {

    private var btnSignUp: MaterialButton = findViewById(R.id.btn_sign_up)
    private val etEmail: TextInputLayout = findViewById(R.id.et_email)
    private val etPassword: TextInputLayout = findViewById(R.id.et_password)
    private val btnForgotPassword: TextView = findViewById(R.id.tv_forgot_password)
    private val btnSignIn: MaterialButton = findViewById(R.id.btn_sign_in)
    private val btnGoogle: CardView = findViewById(R.id.btn_google)
    private val btnFacebook: CardView = findViewById(R.id.btn_facebook)
    private val viewBackground: View = findViewById(R.id.view_background)
    private val progressBar: ProgressBar = findViewById(R.id.progress_bar)

    init {
        btnSignUp.setOnClickListener { listener?.onSignUpClicked() }
        btnForgotPassword.setOnClickListener { listener?.onForgotPasswordClicked() }
        btnSignIn.setOnClickListener { listener?.onSignInClicked() }
        btnGoogle.setOnClickListener { listener?.onSignInWithGoogleClicked() }
        btnFacebook.setOnClickListener { listener?.onSignInWithFacebookClicked() }
    }

    override fun showProgressIndication() {
        viewBackground.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissProgressIndication() {
        viewBackground.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun getEmail(): String = etEmail.editText!!.text.toString()

    override fun getPassword(): String = etPassword.editText!!.text.toString()

    override fun showEmailError() {
        etEmail.isErrorEnabled = true
        etEmail.error = "Invalid email"
    }

    override fun showPasswordError() {
        etPassword.isErrorEnabled = true
        etPassword.error = "Invalid password"
    }

    override fun removeEmailError() {
        etEmail.error = null
        etEmail.isErrorEnabled = false
    }

    override fun removePasswordError() {
        etPassword.error = null
        etPassword.isErrorEnabled = false
    }
}