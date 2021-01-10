package com.kumela.socialnetwork.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.mvc.BaseViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

class SplashViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater, parent, R.layout.fragment_splash
), SplashViewMvc