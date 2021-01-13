package com.kumela.socialnetwork.common.di.presentation

import com.kumela.socialnetwork.common.di.annotations.PresentationScope
import com.kumela.socialnetwork.common.permissions.PermissionHelper
import com.kumela.socialnetwork.ui.chat.ChatFragment
import com.kumela.socialnetwork.ui.comments.CommentsFragment
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.dialogs.alertdialog.AlertDialog
import com.kumela.socialnetwork.ui.explore.ExploreFragment
import com.kumela.socialnetwork.ui.home.HomeFragment
import com.kumela.socialnetwork.ui.auth.AuthFragment
import com.kumela.socialnetwork.ui.messages.MessagesFragment
import com.kumela.socialnetwork.ui.post_image.PostImageFragment
import com.kumela.socialnetwork.ui.profile.ProfileFragment
import com.kumela.socialnetwork.ui.search.SearchFragment
import com.kumela.socialnetwork.ui.splash.SplashFragment
import com.kumela.socialnetwork.ui.story_presenter.StoryPresenterFragment
import com.kumela.socialnetwork.ui.story_uploder.StoryUploaderFragment
import com.kumela.socialnetwork.ui.user_list.UserListFragment
import com.kumela.socialnetwork.ui.user_profile.UserProfileFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(
    modules = [
        PresentationModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
        QueryPagerModule::class
    ]
)
interface PresentationComponent {
    fun getViewMvcFactory(): ViewMvcFactory
    fun getPermissionHelper(): PermissionHelper

    fun inject(fragment: ChatFragment)
    fun inject(fragment: CommentsFragment)
    fun inject(fragment: ExploreFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: MessagesFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: PostImageFragment)
    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: UserListFragment)
    fun inject(fragment: StoryUploaderFragment)
    fun inject(fragment: StoryPresenterFragment)

    fun inject(dialogFragment: AlertDialog)
}