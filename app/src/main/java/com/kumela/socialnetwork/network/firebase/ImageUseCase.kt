package com.kumela.socialnet.network.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.ktx.storageMetadata
import com.kumela.socialnet.common.UseCase
import com.kumela.socialnet.network.firebase.helpers.StorageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*


/**
 * Created by Toko on 10,October,2020
 **/

enum class ImageType {
    PROFILE, POST, STORY
}

object ImageUseCase : UseCase() {

    /**
     * try to upload image to firebase storage and notify result with downloadUri
     */
    inline fun uploadImageAndNotify(
        uuid: UUID,
        imageType: ImageType,
        imageUri: Uri,
        crossinline onSuccessListener: (String) -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        val storageRef = when (imageType) {
            ImageType.PROFILE -> StorageHelper.getUserProfileImagesBuckedReference()
            ImageType.POST -> StorageHelper.getUserPostImagesBuckedReference()
            ImageType.STORY -> StorageHelper.getStoryImagesBuckedReference()
        }

        val metadata = storageMetadata {
            cacheControl = "max-age=7776000, Expires=7776000, public, must-revalidate"
        }

        val imageRef = storageRef.child(StorageHelper.getRandomUid())

        imageRef
            .putFile(imageUri, metadata)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    if (isActive(uuid)) {
                        onSuccessListener.invoke(downloadUri.toString())
                    }
                }.addOnFailureListener { exception ->
                    if (isActive(uuid)) {
                        onFailureListener.invoke(exception)
                    }
                }
            }.addOnFailureListener { exception ->
                if (isActive(uuid)) {
                    onFailureListener.invoke(exception)
                }
            }
    }

    suspend fun uploadImageAndNotify(
        imageType: ImageType,
        imageBitmap: Bitmap
    ): Result<String, Exception> = withContext(Dispatchers.IO) {
        try {
            val storageRef = when (imageType) {
                ImageType.PROFILE -> StorageHelper.getUserProfileImagesBuckedReference()
                ImageType.POST -> StorageHelper.getUserPostImagesBuckedReference()
                ImageType.STORY -> StorageHelper.getStoryImagesBuckedReference()
            }

            val metadata = storageMetadata {
                cacheControl = "max-age=7776000, Expires=7776000, public, must-revalidate"
            }

            val imageRef = storageRef.child(StorageHelper.getRandomUid())

            val bao = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
            imageRef
                .putBytes(bao.toByteArray(), metadata)
                .await()

            val downloadUri = imageRef.downloadUrl.await().toString()

            return@withContext Result.Success(downloadUri)
        } catch (e: Exception) {
            return@withContext Result.Failure(e)
        }
    }
}
