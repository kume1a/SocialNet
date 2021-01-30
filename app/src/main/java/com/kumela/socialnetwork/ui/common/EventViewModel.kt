package com.kumela.socialnetwork.ui.common

import androidx.lifecycle.ViewModel
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.models.list.Post

class EventViewModel: ViewModel() {
    private val newPosts = ArrayList<Post>()
    private val newCommentsCountForPosts = HashMap<Int, Int>()
    private val newReplies = ArrayList<Reply>()

    fun addNewPost(post: Post) = newPosts.add(post)
    fun getNewPosts(): List<Post> {
        val clone = ArrayList(newPosts)
        newPosts.clear()
        return clone
    }

    fun newCommentAdded(postId: Int) {
        newCommentsCountForPosts[postId] = (newCommentsCountForPosts[postId] ?: 0) + 1
    }

    fun getNewComments(): HashMap<Int, Int> {
        val clone = HashMap(newCommentsCountForPosts)
        newCommentsCountForPosts.clear()
        return clone
    }

    fun addNewReply(reply: Reply) = newReplies.add(reply)
    fun getNewReplies(): List<Reply> {
        val clone = ArrayList(newReplies)
        newReplies.clear()
        return clone
    }
}