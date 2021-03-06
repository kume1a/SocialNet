package com.kumela.socialnetwork.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.adapters.users.UsersAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

class SearchViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<SearchViewMvc.Listener>(
    inflater, parent, R.layout.fragment_search
), SearchViewMvc {

    private val buttonBack: ImageButton = findViewById(R.id.button_back)
    private val etInput: EditText = findViewById(R.id.et_input)
    private val recyclerSearch: RecyclerView = findViewById(R.id.recycler_search)

    private val searchAdapter = UsersAdapter(viewMvcFactory,
        listener = { userModel ->
            listener?.onSearchItemClicked(userModel)
        })

    init {
        buttonBack.setOnClickListener { listener?.onBackPressed() }
        etInput.addTextChangedListener(
            onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                if (charSequence != null) {
                    listener?.onQueryTextChanged(charSequence.toString())
                }
            })

        recyclerSearch.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    override fun bindSearchItems(users: List<User>) {
        searchAdapter.bindUsers(users)
    }
}