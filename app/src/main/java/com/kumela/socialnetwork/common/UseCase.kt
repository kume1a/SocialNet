package com.kumela.socialnetwork.common

import java.util.*
import kotlin.collections.HashSet

/**
 * Created by Toko on 28,October,2020
 **/

abstract class UseCase {
    private val registry = HashSet<UUID>()

    fun registerListener(uuid: UUID) {
        registry.add(uuid)
    }

    fun unregisterListener(uuid: UUID) {
        registry.remove(uuid)
    }

    fun isActive(uuid: UUID): Boolean {
        return registry.contains(uuid)
    }
}