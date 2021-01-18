package com.kumela.socialnetwork.network.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import com.kumela.socialnetwork.network.common.StorageHelper
import com.kumela.socialnetwork.network.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

/**
 * Created by Toko on 18,January,2021
 **/

enum class ImageType {
    PROFILE, POST, STORY
}

class ImageRepository {
    suspend fun uploadImage(
        imageType: ImageType,
        imageUri: Uri,
    ): Result<String, Exception> = withContext(Dispatchers.IO) {
        try {
            val imageRef = getStorageBucket(imageType).child(StorageHelper.getRandomUid())

            imageRef
                .putFile(imageUri, META_DATA)
                .await()

            val downloadUri = imageRef.downloadUrl.await().toString()
            return@withContext Result.Success(downloadUri)
        } catch (e: Exception) {
            return@withContext Result.Failure(e)
        }
    }

    suspend fun uploadImage(
        imageType: ImageType,
        imageBitmap: Bitmap,
    ): Result<String, Exception> = withContext(Dispatchers.IO) {
        try {
            val imageRef = getStorageBucket(imageType).child(StorageHelper.getRandomUid())

            val bao = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
            imageRef
                .putBytes(bao.toByteArray(), META_DATA)
                .await()

            val downloadUri = imageRef.downloadUrl.await().toString()
            return@withContext Result.Success(downloadUri)
        } catch (e: Exception) {
            return@withContext Result.Failure(e)
        }
    }

    private fun getStorageBucket(
        imageType: ImageType
    ): StorageReference = when (imageType) {
        ImageType.PROFILE -> StorageHelper.getUserProfileImagesBuckedReference()
        ImageType.POST -> StorageHelper.getUserPostImagesBuckedReference()
        ImageType.STORY -> StorageHelper.getStoryImagesBuckedReference()
    }

    companion object {
        private val META_DATA = storageMetadata {
            cacheControl = "max-age=7776000, Expires=7776000, public, must-revalidate"
        }
    }
}