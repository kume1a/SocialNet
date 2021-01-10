package com.kumela.socialnetwork.ui.personal_info

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.kumela.socialnetwork.ui.common.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc


/**
 * Created by Toko on 24,September,2020
 **/

class PersonalInfoViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<PersonalInfoViewMvc.Listener>(
    inflater, parent, R.layout.fragment_personal_info
), PersonalInfoViewMvc {

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textClickToChoose: TextView = findViewById(R.id.text_click_to_choose)
    private val etUsername: EditText = findViewById(R.id.et_username)
    private val btnFinish: Button = findViewById(R.id.btn_finish)
    private val viewBackground: View = findViewById(R.id.view_background)
    private val progressBar: ProgressBar = findViewById(R.id.progress_bar)


    init {
        btnFinish.setOnClickListener { listener?.onFinishClicked() }
        imageProfile.setOnClickListener { listener?.onProfileImageClicked() }
        textClickToChoose.setOnClickListener { listener?.onProfileImageClicked() }
    }

    override fun showProgressIndication() {
        viewBackground.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndication() {
        viewBackground.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun showUsernameError() {
        Snackbar.make(rootView, "Username is required", Snackbar.LENGTH_LONG).show()
    }

    override fun getUsername(): String = etUsername.text.toString()

    override fun loadProfileImage(imageUri: Uri) {
        imageProfile.setImageURI(imageUri)
    }
}