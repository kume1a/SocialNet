package com.kumela.socialnet.common.di.presentation

import com.kumela.socialnet.common.di.annotations.PresentationScope
import com.kumela.socialnet.common.permissions.PermissionHelper
import com.kumela.socialnet.ui.chat.ChatFragment
import com.kumela.socialnet.ui.comments.CommentsFragment
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.dialogs.alertdialog.AlertDialog
import com.kumela.socialnet.ui.explore.ExploreFragment
import com.kumela.socialnet.ui.home.HomeFragment
import com.kumela.socialnet.ui.login.LoginFragment
import com.kumela.socialnet.ui.messages.MessagesFragment
import com.kumela.socialnet.ui.personal_info.PersonalInfoFragment
import com.kumela.socialnet.ui.post_image.PostImageFragment
import com.kumela.socialnet.ui.profile.ProfileFragment
import com.kumela.socialnet.ui.register.RegisterFragment
import com.kumela.socialnet.ui.search.SearchFragment
import com.kumela.socialnet.ui.splash.SplashFragment
import com.kumela.socialnet.ui.story_presenter.StoryPresenterFragment
import com.kumela.socialnet.ui.story_uploder.StoryUploaderFragment
import com.kumela.socialnet.ui.user_list.UserListFragment
import com.kumela.socialnet.ui.user_profile.UserProfileFragment
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
    fun inject(fragment: LoginFragment)
    fun inject(fragment: MessagesFragment)
    fun inject(fragment: PersonalInfoFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: PostImageFragment)
    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: UserListFragment)
    fun inject(fragment: StoryUploaderFragment)
    fun inject(fragment: StoryPresenterFragment)

    fun inject(dialogFragment: AlertDialog)
}