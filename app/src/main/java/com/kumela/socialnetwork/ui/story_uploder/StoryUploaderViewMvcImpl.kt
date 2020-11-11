package com.kumela.socialnet.ui.story_uploder

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.kumela.socialnet.R
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc

/**
 * Created by Toko on 06,November,2020
 **/

class StoryUploaderViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<StoryUploaderViewMvc.Listener>(
    inflater, parent, R.layout.fragment_story_uploader
), StoryUploaderViewMvc {

    private val buttonClose: ImageButton = findViewById(R.id.button_close)
    private val image: ImageView = findViewById(R.id.image)
    private val buttonUpload: ExtendedFloatingActionButton = findViewById(R.id.button_upload)
    private val progressIndicator: Group = findViewById(R.id.progress_indicator)

    init {
        buttonClose.setOnClickListener { listener?.onCloseClicked() }
        buttonUpload.setOnClickListener { listener?.onUploadClicked() }
    }

    override fun bindImage(imageUri: Uri) {
        image.setImageURI(imageUri)
    }

    override fun showProgressIndicationAndDisableButtons() {
        progressIndicator.visibility = View.VISIBLE
        buttonClose.isEnabled = false
        buttonUpload.isEnabled = false
    }

    override fun hideProgressIndicationAndEnableButtons() {
        progressIndicator.visibility = View.GONE
        buttonClose.isEnabled = true
        buttonUpload.isEnabled = true
    }
}