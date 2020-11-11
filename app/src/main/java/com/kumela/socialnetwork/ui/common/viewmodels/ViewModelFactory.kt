@file:Suppress("UNCHECKED_CAST")

package com.kumela.socialnet.ui.common.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Toko on 23,October,2020
 **/

class ViewModelFactory @Inject constructor(
    private val providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = providerMap[modelClass]
        val viewModel =
            provider?.get() ?: throw RuntimeException("unsupported view model type: $modelClass")

        return viewModel as T
    }
}