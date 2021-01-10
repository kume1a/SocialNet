package com.kumela.socialnetwork.network.firebase.helpers

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

/**
 * Created by Toko on 11,November,2020
 **/

object StorageHelper {

    private const val STORAGE_PROFILE_IMAGES = "profile_images"
    private const val STORAGE_POST_IMAGES = "post_images"
    private const val STORAGE_STORY_IMAGES = "stories"

    fun getRandomUid(): String = UUID.randomUUID().toString()

    private val firebaseStorage = Firebase.storage.also {
        it.maxUploadRetryTimeMillis = 10000 // 10 sec
    }

    fun getUserProfileImagesBuckedReference(): StorageReference =
        firebaseStorage.reference.child(STORAGE_PROFILE_IMAGES)

    fun getUserPostImagesBuckedReference(): StorageReference =
        firebaseStorage.reference.child(STORAGE_POST_IMAGES)

    fun getStoryImagesBuckedReference(): StorageReference =
        firebaseStorage.reference.child(STORAGE_STORY_IMAGES)
}