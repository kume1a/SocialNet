package com.kumela.socialnet.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.adapters.users.UsersAdapter
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc

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

    override fun bindSearchItems(users: List<UserModel>) {
        searchAdapter.bindUsers(users)
    }
}