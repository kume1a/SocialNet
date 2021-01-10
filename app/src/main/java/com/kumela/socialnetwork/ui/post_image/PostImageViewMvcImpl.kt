package com.kumela.socialnetwork.ui.post_image

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.kumela.socialnetwork.ui.common.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.views.LockableScrollView

/**
 * Created by Toko on 24,September,2020
 **/

class PostImageViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<PostImageViewMvc.Listener>(
    inflater, parent, R.layout.fragment_post_image
), PostImageViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val scrollView: LockableScrollView = findViewById(R.id.scroll_view)
    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val imagePost: RoundedImageView = findViewById(R.id.image_post)
    private val textUsername: TextView = findViewById(R.id.text_username)
    private val etHeader: EditText = findViewById(R.id.et_header)
    private val etDescription: EditText = findViewById(R.id.et_description)
    private val progressIndication: Group = findViewById(R.id.progress_indicator)

    init {
        toolbarViewMvc.enableUpButtonAndListen { listener?.onBackPressed() }
        toolbarViewMvc.enableRightButtonAndListen("Post") { listener?.onPostClicked() }
        toolbarViewMvc.setTitle("Post Image")
        toolbar.addView(toolbarViewMvc.rootView)
    }

    override fun bindProfileImage(imageUri: String) {
        imageProfile.load(imageUri)
    }

    override fun bindPostImage(imageUri: Uri) {
        imagePost.setImageURI(imageUri)
    }

    override fun bindPostImage(imageBitmap: Bitmap) {
        imagePost.setImageBitmap(imageBitmap)
    }

    override fun bindUsername(username: String) {
        textUsername.text = username
    }

    override fun getHeader(): String = etHeader.text.toString()

    override fun getDescription(): String = etDescription.text.toString()

    override fun showProgressIndicatorAndDisableInteractivity() {
        progressIndication.visibility = View.VISIBLE
        toolbarViewMvc.disableButtons()
        scrollView.scrollable = false
    }

    override fun hideProgressIndicatorAndEnableInteractivity() {
        progressIndication.visibility = View.GONE
        toolbarViewMvc.enableButtons()
        scrollView.scrollable = true
    }
}