package com.kumela.socialnetwork.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.adapters.explore.ExploreAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc

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
    private val recyclerExplore: RecyclerView = findViewById(R.id.recycler_explore)

    private val exploreAdapter = ExploreAdapter(viewMvcFactory) {
        listener?.onScrolledToBottom()
    }

    init {
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

    override fun addPosts(posts: List<Post>) {
        recyclerExplore.post {
            exploreAdapter.addPosts(posts)
        }
    }
}