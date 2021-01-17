package com.kumela.socialnetwork.ui.search

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface SearchViewMvc : ObservableViewMvc<SearchViewMvc.Listener> {
    interface Listener {
        fun onBackPressed()
        fun onQueryTextChanged(query: String)
        fun onSearchItemClicked(user: User)
    }

    fun bindSearchItems(users: List<User>)
}