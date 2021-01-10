package com.kumela.socialnetwork.ui.adapters.friends

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 04,October,2020
 **/

class FriendsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (UserModel) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>(), FriendsItemViewMvc.Listener {

    private val items = arrayListOf<UserModel>()

    fun bindUsers(users: List<UserModel>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val viewMvc = viewMvcFactory.newInstance(FriendsItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return FriendsViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.viewMvc.bindUserModel(items[position])
    }

    override fun getItemCount(): Int = items.size

    class FriendsViewHolder(val viewMvc: FriendsItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onFriendItemClicked(userModel: UserModel) {
        listener.invoke(userModel)
    }
}