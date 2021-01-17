package com.kumela.socialnetwork.ui.adapters.users

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 18,October,2020
 **/

class UsersAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (User) -> Unit,
    private val onLastItemBound: (() -> Unit)? = null
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>(), UserItemViewMvc.Listener {

    private val items = ArrayList<User>()

    fun bindUsers(users: List<User>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

    fun addUser(user: User) {
        items.add(user)
        notifyItemInserted(items.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val viewMvc = viewMvcFactory.newInstance(UserItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return UserViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (position == items.size - 1) {
            onLastItemBound?.invoke()
        }

        holder.viewMvc.bindUser(items[position])
    }

    override fun getItemCount(): Int = items.size

    class UserViewHolder(val viewMvc: UserItemViewMvc) : RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onItemClicked(user: User) {
        listener.invoke(user)
    }
}