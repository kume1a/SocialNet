package com.kumela.socialnet.common.di.presentation

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.savedstate.SavedStateRegistryOwner
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.dialogs.DialogManager
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(
    private val fragment: Fragment? = null
) {
    @Provides
    fun providesViewMvcFactory(inflater: LayoutInflater) = ViewMvcFactory(inflater)

    @Provides
    fun providesFragment(): Fragment? = fragment

    @Provides
    fun providesContext(activity: AppCompatActivity): Context = activity

    @Provides
    fun providesNavController(fragment: Fragment?): NavController =
        fragment!!.findNavController()

    @Provides
    fun providesDialogManager(
        context: Context,
        fragmentManager: FragmentManager
    ): DialogManager = DialogManager(context, fragmentManager)
}