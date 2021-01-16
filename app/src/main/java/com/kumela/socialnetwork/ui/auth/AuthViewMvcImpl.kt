package com.kumela.socialnetwork.ui.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.FloatRange
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.views.AuthBackgroundInk

/**
 * Created by Toko on 10,September,2020
 **/

class AuthViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<AuthViewMvc.Listener>(
    inflater, parent, R.layout.fragment_login
), AuthViewMvc {

    private val backgroundInk: AuthBackgroundInk = findViewById(R.id.background_ink)
    private val btnGo: ImageButton = findViewById(R.id.btn_go)

    private val textWelcomeBack: TextView = findViewById(R.id.text_welcome_back)
    private val etSigninEmail: TextInputLayout = findViewById(R.id.et_signin_email)
    private val etSigninPassword: TextInputLayout = findViewById(R.id.et_signin_password)
    private val textSigninSignin: TextView = findViewById(R.id.text_signin_signin)
    private val textSigninSignUp: TextView = findViewById(R.id.text_signin_signup)
    private val btnForgotPassword: TextView = findViewById(R.id.text_signin_forgot_password)

    private val textCreateAccount: TextView = findViewById(R.id.text_create_account)
    private val etSignupName: TextInputLayout = findViewById(R.id.et_signup_name)
    private val etSignupEmail: TextInputLayout = findViewById(R.id.et_signup_email)
    private val etSignupPassword: TextInputLayout = findViewById(R.id.et_signup_password)
    private val textSignupSignup: TextView = findViewById(R.id.text_signup_signup)
    private val textSignupSignin: TextView = findViewById(R.id.text_signup_signin)

    private val viewBackground: View = findViewById(R.id.view_background)
    private val progressBar: ProgressBar = findViewById(R.id.progress_bar)

    init {
        btnGo.setOnClickListener { listener?.onAuthClicked() }

        textSigninSignUp.setOnClickListener { listener?.onGoToSignUpClicked() }
        btnForgotPassword.setOnClickListener { listener?.onForgotPasswordClicked() }

        textSignupSignin.setOnClickListener { listener?.onGoToSignInClicked() }
    }

    override fun showSnackBar(stringResId: Int, duration: Int) {
        Snackbar.make(rootView, stringResId, duration).show()
    }

    override fun showProgressIndication() {
        viewBackground.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissProgressIndication() {
        viewBackground.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun getSigninEmail(): String = etSigninEmail.editText!!.text.toString()
    override fun getSigninPassword(): String = etSigninPassword.editText!!.text.toString()
    override fun showSigninEmailError() = showTILError(etSigninEmail, "Invalid email")
    override fun showSigninPasswordError() = showTILError(etSigninPassword, "Weak password")
    override fun removeSigninEmailError() = removeTILError(etSigninEmail)
    override fun removeSigninPasswordError() = removeTILError(etSigninPassword)

    override fun getSignupName(): String = etSignupName.editText!!.text.toString()
    override fun getSignupEmail(): String = etSignupEmail.editText!!.text.toString()
    override fun getSignupPassword(): String = etSignupPassword.editText!!.text.toString()
    override fun showSignupNameError() = showTILError(etSignupName, "Invalid name")
    override fun showSignupEmailError() = showTILError(etSignupEmail, "Invalid Email")
    override fun showSignupPasswordError() = showTILError(etSignupPassword, "Weak Password")
    override fun removeSignupNameError() = removeTILError(etSignupName)
    override fun removeSignupEmailError() = removeTILError(etSignupEmail)
    override fun removeSignupPasswordError() = removeTILError(etSignupPassword)

    override fun forwardBackgroundAnimation() = backgroundInk.forward()
    override fun reverseBackgroundAnimation() = backgroundInk.reverse()


    override fun showLoginGroup() {
        translateAndFade(
            0f, 1f,
            textWelcomeBack,
            etSigninEmail,
            etSigninPassword,
            textSigninSignin,
            textSigninSignUp,
            btnForgotPassword,
        )
    }

    override fun hideLoginGroup() {
        translateAndFade(
            100f, 0f,
            textWelcomeBack,
            etSigninEmail,
            etSigninPassword,
            textSigninSignin,
            textSigninSignUp,
            btnForgotPassword,
        )
    }

    override fun showSignUpGroup() {
        translateAndFade(
            0f, 1f,
            textCreateAccount,
            etSignupName,
            etSignupEmail,
            etSignupPassword,
            textSignupSignup,
            textSignupSignin,
        )
    }

    override fun hideSignUpGroup() {
        translateAndFade(
            -100f, 0f,
            textCreateAccount,
            etSignupName,
            etSignupEmail,
            etSignupPassword,
            textSignupSignup,
            textSignupSignin,
        )
    }

    private fun showTILError(til: TextInputLayout, error: String) {
        til.isErrorEnabled = true
        til.error = error
    }

    private fun removeTILError(til: TextInputLayout) {
        til.error = null
        til.isErrorEnabled = false
    }

    private fun translateAndFade(
        dy: Float,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float,
        vararg views: View
    ) {
        for (view in views) {
            view.visibility = View.VISIBLE

            val animation = view.animate()
                .setDuration(400)
                .alpha(alpha)
                .translationY(dy)

            if (alpha == 0f) {
                animation.withEndAction {
                    view.visibility = View.GONE
                }
            }

            animation.start()
        }
    }
}