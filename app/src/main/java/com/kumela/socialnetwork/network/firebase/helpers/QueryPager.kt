package com.kumela.socialnet.network.firebase.helpers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kumela.socialnet.common.UseCase
import com.kumela.socialnet.common.listeners.OnFailureListener
import com.kumela.socialnet.common.listeners.OnSuccessListener
import java.util.*

/**
 * Created by Toko on 31,October,2020
 **/

class QueryPager<T : Any> constructor(
    private val pageSize: Int,
    private val dataClass: Class<T>?,
) : UseCase() {

    private var mLastKey: String? = null
    private var mAllDataFetched: Boolean = false
    private var mWaiting = false
    private var pendingCall = false

    fun fetchNextPageAndNotify(
        uuid: UUID,
        reference: DatabaseReference,
        onSuccessListener: OnSuccessListener<List<T>?>,
        onFailureListener: OnFailureListener<DatabaseError>,
    ) {
        if (mWaiting) {
            pendingCall = true
            return
        }

        mWaiting = true
        if (mAllDataFetched) {
            if (isActive(uuid)) {
                onSuccessListener.onSuccess(null)
            }
            mWaiting = false
            pendingCall = false
            return
        }

        val query = if (mLastKey == null) {
            reference.orderByKey().limitToLast(pageSize + 1)
        } else {
            reference.orderByKey().endAt(mLastKey).limitToLast(pageSize + 1)
        }


        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isActive(uuid)) {
                    @Suppress("UNCHECKED_CAST")
                    val data = snapshot.children
                        .map { getValue(it) }
                        .takeLast(pageSize) as List<T>

                    val lastKey = snapshot.children.firstOrNull()?.key
                    val lastKeyIsSame = mLastKey == lastKey
                    val dataSizeIsNotPageSize = data.size < pageSize
                    mAllDataFetched = lastKeyIsSame || dataSizeIsNotPageSize
                    mLastKey = lastKey

                    if (!(data.size == 1 && lastKeyIsSame)) {
                        onSuccessListener.onSuccess(data.reversed())
                    }
                }
                mWaiting = false
                if (pendingCall) {
                    pendingCall = false
                    fetchNextPageAndNotify(uuid, reference, onSuccessListener, onFailureListener)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isActive(uuid)) {
                    onFailureListener.onFailure(error)
                }
                mWaiting = false
                pendingCall = false
            }
        })
    }

    private fun getValue(snapshot: DataSnapshot): Any? {
        return if (this@QueryPager.dataClass != null) {
            if (dataClass == Map::class.java) {
                snapshot.value as Map<*, *>
            } else {
                snapshot.getValue(this@QueryPager.dataClass)!!
            }
        } else {
            snapshot.key
        }
    }
}
