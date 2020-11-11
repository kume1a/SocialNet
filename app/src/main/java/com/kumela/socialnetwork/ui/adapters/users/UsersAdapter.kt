package com.kumela.socialnet.ui.adapters.users

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.ViewMvcFactory

/**
 * Created by Toko on 18,October,2020
 **/

class UsersAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (UserModel) -> Unit,
    private val onLastItemBound: (() -> Unit)? = null
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>(), UserItemViewMvc.Listener {

    private val items = ArrayList<UserModel>()

    fun bindUsers(userModels: List<UserModel>) {
        items.clear()
        items.addAll(userModels)
        notifyDataSetChanged()
    }

    fun addUser(userModel: UserModel) {
        items.add(userModel)
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

    override fun onItemClicked(userModel: UserModel) {
        listener.invoke(userModel)
    }
}