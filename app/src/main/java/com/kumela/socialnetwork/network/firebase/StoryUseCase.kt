package com.kumela.socialnet.network.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.kumela.socialnet.common.UseCase
import com.kumela.socialnet.models.firebase.FeedStoriesModel
import com.kumela.socialnet.models.firebase.FeedStoryModel
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.network.firebase.helpers.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Toko on 07,November,2020
 **/

object StoryUseCase : UseCase() {

    fun getUserStoriesRef(uid: String = UserUseCase.uid): DatabaseReference =
        DatabaseHelper.getStoriesTableRef().child(uid)

    inline fun createStory(
        uuid: UUID,
        storyModel: StoryModel,
        crossinline onSuccessListener: () -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        DatabaseHelper
            .getStoriesTableRef()
            .child(UserUseCase.uid)
            .push()
            .setValue(storyModel)
            .addOnSuccessListener {
                if (isActive(uuid)) {
                    onSuccessListener.invoke()
                }
            }
            .addOnFailureListener { exception ->
                if (isActive(uuid)) {
                    onFailureListener.invoke(exception)
                }
            }
    }

    suspend inline fun fetchFeedStoriesAndNotify(
        uuid: UUID,
        crossinline onSuccessListener: (List<FeedStoriesModel>) -> Unit,
        crossinline onFailureListener: (DatabaseError) -> Unit
    ) = withContext(Dispatchers.IO) {
        DatabaseHelper
            .getFeedStoriesTableRef()
            .child(UserUseCase.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isActive(uuid)) {
                        // map data
                        val feedStories: List<FeedStoriesModel> = snapshot.children
                            .map { rootSnapshot ->
                                val stories = rootSnapshot
                                    .children
                                    .map { feedStorySnapshot ->
                                        feedStorySnapshot.getValue<FeedStoryModel>()!!
                                    }

                                FeedStoriesModel(rootSnapshot.key!!, stories)
                            }

                        onSuccessListener.invoke(feedStories)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isActive(uuid)) onFailureListener.invoke(error)
                }
            })
    }

    fun deleteFeedStory(userId: String, storyId: String) {
        DatabaseHelper
            .getFeedStoriesTableRef()
            .child(UserUseCase.uid)
            .child(userId)
            .child(storyId)
            .setValue(null)
    }

}