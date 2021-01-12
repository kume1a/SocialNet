package com.kumela.socialnetwork.ui.adapters.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load

/**
 * Created by Toko on 02,October,2020
 **/

class StoryItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<StoryItemViewMvc.Listener>(
    inflater, parent, R.layout.item_story
) {

    interface Listener {
        fun onStoryClicked(userModel: UserModel)
    }

    private var userModel: UserModel? = null

    private val imageStory: RoundedImageView = findViewById(R.id.image)
    private val imageAdd: ImageView = findViewById(R.id.image_add)

    init {
        rootView.setOnClickListener { listener?.onStoryClicked(userModel!!) }
    }

    fun bindStoryModel(userModel: UserModel) {
        this.userModel = userModel

        imageStory.load(userModel.imageUri)
    }

//    override fun startAnimation() {
//        val animation = loadAnimation(context, R.anim.slide_in_right)
//        rootView.startAnimation(animation)
//    }

    fun revealAddIcon() {
        imageAdd.visibility = View.VISIBLE
    }

    fun hideAddIcon() {
        imageAdd.visibility = View.GONE
    }
}