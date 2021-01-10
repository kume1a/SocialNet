package com.kumela.socialnetwork.ui.common.controllers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.Fragment
import com.kumela.socialnetwork.common.di.presentation.PresentationModule
import com.kumela.socialnetwork.ui.common.main.MainActivity


/**
 * Created by Toko on 08,September,2020
 **/

abstract class BaseFragment : Fragment() {
//    private val appComponent get() = (requireActivity().application as App).appComponent

//    private val activityComponent by lazy {
//        appComponent.newActivityComponentBuilder()
//            .activity(activity as AppCompatActivity)
//            .build()
//    }

    protected val injector by lazy {
        (activity as MainActivity)
            .activityComponent
            .newPresentationComponent(PresentationModule(this))
    }

    @Suppress("DEPRECATION")
    protected fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}