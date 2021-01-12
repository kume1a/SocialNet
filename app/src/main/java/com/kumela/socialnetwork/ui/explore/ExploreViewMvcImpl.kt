package com.kumela.socialnetwork.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.common.Constants.images
import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.ui.adapters.explore.ExploreAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load

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