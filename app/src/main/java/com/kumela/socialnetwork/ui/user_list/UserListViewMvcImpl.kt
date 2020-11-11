package com.kumela.socialnet.ui.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.adapters.users.UsersAdapter
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.toolbar.ToolbarViewMvc

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

    override fun bindUsers(users: List<UserModel>) {
        userAdapter.bindUsers(users)
    }

    override fun addUser(userModel: UserModel) {
        userAdapter.addUser(userModel)
    }
}