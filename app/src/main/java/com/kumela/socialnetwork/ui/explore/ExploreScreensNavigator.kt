package com.kumela.socialnetwork.ui.explore

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 18,October,2020
 **/

class ExploreScreensNavigator @Inject constructor(private val navController: NavController) {
    fun toSearch() {
        val action = ExploreFragmentDirections.actionExploreFragmentToSearchFragment()
        navController.navigate(action)
    }
}