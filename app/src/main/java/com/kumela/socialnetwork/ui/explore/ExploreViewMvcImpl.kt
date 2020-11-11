package com.kumela.socialnet.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.roundimageview.RoundedImageView
import com.kumela.socialnet.R
import com.kumela.socialnet.common.Constants.images
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.adapters.explore.ExploreAdapter
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.utils.load

/**
 * Created by Toko on 24,September,2020
 **/

class ExploreViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<ExploreViewMvc.Listener>(
    inflater, parent, R.layout.fragment_explore
), ExploreViewMvc {

    private val etSearch: EditText = findViewById(R.id.et_search)
    private val imageHeader: RoundedImageView = findViewById(R.id.image_header)
    private val recyclerExplore: RecyclerView = findViewById(R.id.recycler_explore)

    private val exploreAdapter = ExploreAdapter(viewMvcFactory) {
        listener?.onScrolledToBottom()
    }

    init {
        imageHeader.load(images.random())
        recyclerExplore.apply {
            adapter = exploreAdapter
            layoutManager = LinearLayoutManager(context)
        }

        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listener?.onSearchClicked()
            }
        }
    }

    override fun bindPosts(postModels: List<PostModel>) {
        exploreAdapter.bindPosts(postModels)
    }

    override fun addPosts(postModels: List<PostModel>) {
        exploreAdapter.addPosts(postModels)
    }
}