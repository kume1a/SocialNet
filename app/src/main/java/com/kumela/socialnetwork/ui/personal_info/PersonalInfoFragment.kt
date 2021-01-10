package com.kumela.socialnetwork.ui.personal_info

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.permissions.PermissionHelper
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.network.firebase.ImageType
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.ActivityResultListener
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.controllers.IntentDispatcher
import com.kumela.socialnetwork.ui.common.controllers.RequestResultDispatcher
import com.kumela.socialnetwork.ui.common.dialogs.DialogEventBus
import com.kumela.socialnetwork.ui.common.dialogs.DialogManager
import com.kumela.socialnetwork.ui.common.dialogs.alertdialog.AlertDialog
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class PersonalInfoFragment : BaseFragment(), PersonalInfoViewMvc.Listener,
    PermissionHelper.Listener,
    DialogEventBus.Listener,
    PersonalInfoViewModel.Listener,
    ActivityResultListener {

    private lateinit var mViewMvc: PersonalInfoViewMvc
    private lateinit var mViewModel: PersonalInfoViewModel

    private var imageUri: Uri? = null

    private lateinit var argUid: String

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: PersonalInfoScreensNavigator
    @Inject lateinit var mPermissionHelper: PermissionHelper
    @Inject lateinit var mDialogManager: DialogManager
    @Inject lateinit var mDialogEventBus: DialogEventBus
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mRequestResultDispatcher: RequestResultDispatcher
    @Inject lateinit var mIntentDispatcher: IntentDispatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(PersonalInfoViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(PersonalInfoViewModel::class.java)

        argUid = requireArguments().getString("uid").toString()

        super.onStart()
        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)
        mPermissionHelper.registerListener(this)
        mDialogEventBus.registerListener(this)
        mRequestResultDispatcher.registerRequestResultListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
        mPermissionHelper.unregisterListener(this)
        mDialogEventBus.unregisterListener(this)
        mRequestResultDispatcher.unregisterRequestResultListener(this)
    }

    override fun onFinishClicked() {
        val username = mViewMvc.getUsername()

        if (username.isNotBlank()) {
            if (!isInternetAvailable()) {
                mDialogManager.showNetworkUnavailableDialog()
                return
            }

            mViewMvc.showProgressIndication()

            if (imageUri != null) {
                mViewModel.uploadImageAndNotify(imageUri!!, ImageType.PROFILE)
            } else {
                val user =
                    UserModel(argUid, mViewMvc.getUsername(), Constants.DEFAULT_IMAGE_URI, System.currentTimeMillis())
                mViewModel.createUserAndNotify(user)
            }
        } else {
            mViewMvc.showUsernameError()
        }
    }

    override fun onProfileImageClicked() {
        if (mPermissionHelper.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            mIntentDispatcher.dispatchImagePickerIntent(RC_PICK_IMAGE)
        } else {
            mDialogManager.showAlertDialog(
                null,
                "${getString(R.string.app_name)} needs storage permission to access photos on device",
                "Ok",
                null
            )
        }
    }

    // permission helper
    override fun onPermissionGranted(permission: String, requestCode: Int) {
        mIntentDispatcher.dispatchImagePickerIntent(RC_PICK_IMAGE)
    }

    override fun onPermissionDeclined(permission: String, requestCode: Int) {
        Toast.makeText(context, "Permission declined", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionDeclinedDontAskAgain(permission: String, requestCode: Int) {
        Toast.makeText(context, "You can turn on permission from settings", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestResultReceived(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(
            javaClass.simpleName,
            "onRequestResultReceived() called with: requestCode = $requestCode, resultCode = $resultCode, data = $data"
        )
        if (resultCode == Activity.RESULT_OK && requestCode == RC_PICK_IMAGE && data != null) {
            imageUri = Uri.parse(data.dataString)
            mViewMvc.loadProfileImage(Uri.parse(data.dataString))
        }
    }

    override fun onDialogEvent(event: Any) {
        when (event as? AlertDialog.Button) {
            AlertDialog.Button.POSITIVE -> {
                mPermissionHelper.requestPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    RC_PERMISSION_READ_EXTERNAL_STORAGE
                )
            }
            AlertDialog.Button.NEGATIVE -> throw NotImplementedError("negative button is not implemented for this fragment")
        }
    }

    override fun onUserCreated() {
        mViewMvc.hideProgressIndication()
        mScreensNavigator.toHome()
    }

    override fun onUserCreateFailed(e: Exception) {
        Log.e(javaClass.simpleName, "onUserCreateFailed: failed to create user in database", e)
        mDialogManager.showInfoDialog("Error occurred", "Please try again later")
        mViewMvc.hideProgressIndication()
    }

    override fun onImageUploaded(uri: String) {
        val user = UserModel(argUid, mViewMvc.getUsername(), uri, System.currentTimeMillis())
        mViewModel.createUserAndNotify(user)
    }

    override fun onImageUploadFailed(e: Exception) {
        Log.e(javaClass.simpleName, "onImageUploadFailed: Failed to upload image", e)
        mDialogManager.showInfoDialog("Failed to upload image", "Please try again later")
        mViewMvc.hideProgressIndication()
    }

    companion object {
        private const val RC_PICK_IMAGE = 1
        private const val RC_PERMISSION_READ_EXTERNAL_STORAGE = 2
    }
}