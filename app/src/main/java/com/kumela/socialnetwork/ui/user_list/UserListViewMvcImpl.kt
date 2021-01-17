package com.kumela.socialnetwork.ui.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.adapters.users.UsersAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc

/**
 * Created by Toko on 05,November,2020
 **/

class UserListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<UserListViewMvc.Listener>(
    inflater, parent, R.layout.fragment_user_list
), UserListViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val recyclerUsers: RecyclerView = findViewById(R.id.recycler_users)

    private val userAdapter = UsersAdapter(viewMvcFactory,
        listener = { userModel ->
            listener?.onUserClicked(userModel)
        },
        onLastItemBound = {
            listener?.onLastUserBound()
        })

    init {
        toolbarViewMvc.enableUpButtonAndListen { listener?.onNavigateUpClicked() }

        toolbar.addView(toolbarViewMvc.rootView)

        recyclerUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    override fun bindToolbarHeader(text: String) {
        toolbarViewMvc.setTitle(text)
    }

    override fun bindUsers(users: List<User>) {
        userAdapter.bindUsers(users)
    }

    override fun addUser(user: User) {
        userAdapter.addUser(user)
    }
}