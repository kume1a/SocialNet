package com.kumela.socialnetwork.ui.common.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.navigation.findNavController
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.common.permissions.PermissionHelper
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavViewMvc
import com.kumela.socialnetwork.ui.common.controllers.*


class MainActivity : BaseActivity(),
    BottomNavViewMvc.Listener,
    BackPressDispatcher, RequestResultDispatcher,
    BottomNavHelper, IntentDispatcher {

    private lateinit var mViewMvc: BottomNavViewMvc
    private lateinit var mScreensNavigator: MainScreensNavigator

    private lateinit var mPermissionHelper: PermissionHelper

    private val mBackPressedListeners = HashSet<BackPressedListener>(1)
    private val mActivityResultListeners = HashSet<ActivityResultListener>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mViewMvc =
            presentationComponent.getViewMvcFactory().newInstance(BottomNavViewMvc::class, null)
        setContentView(mViewMvc.rootView)

        mScreensNavigator = MainScreensNavigator(findNavController(R.id.frame_content))
        mPermissionHelper = presentationComponent.getPermissionHelper()
    }

    override fun onBackPressed() {
        if (mViewMvc.isFabMenuShowing() && !mViewMvc.isAnimationInProgress()) {
            mViewMvc.closeFabMenu()
            return
        }

        var isBackPressConsumedByAnyListener = false
        for (listener in mBackPressedListeners) {
            if (listener.onBackPressed()) {
                isBackPressConsumedByAnyListener = true
            }
        }
        if (isBackPressConsumedByAnyListener) return

        // TODO: 11/8/2020 change bottom nav implementation to change activeIndex programmatically
//        val bottomNavDest = mScreensNavigator.getCurrentBottomNavDest()
//        if (bottomNavDest != null && bottomNavDest != MainScreensNavigator.BottomNavDest.HOME) {
//            mScreensNavigator.toHome()
//            return
//        }

        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        mViewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mViewMvc.unregisterListener()
    }

    override fun registerBackPressedListener(listener: BackPressedListener) {
        mBackPressedListeners.add(listener)
    }

    override fun unregisterBackPressedListener(listener: BackPressedListener) {
        mBackPressedListeners.remove(listener)
    }

    override fun registerRequestResultListener(listener: ActivityResultListener) {
        mActivityResultListeners.add(listener)
    }

    override fun unregisterRequestResultListener(listener: ActivityResultListener) {
        mActivityResultListeners.remove(listener)
    }

    override fun showBottomNav() {
        mViewMvc.showBottomNav()
    }

    override fun hideBottomNav() {
        mViewMvc.hideBottomNav()
    }

    override fun resetIndex() {
        mViewMvc.resetBottomNavIndex()
    }

    override fun onFabClicked() {
        if (mViewMvc.isAnimationInProgress()) return

        if (mViewMvc.isFabMenuShowing()) {
            mViewMvc.closeFabMenu()
        } else mViewMvc.openFabMenu()
    }

    override fun onFabMenuImageClicked() {
        dispatchTakeImageIntent()
        mViewMvc.closeFabMenu()
    }

    override fun onFabMenuFileClicked() {
        dispatchImagePickerIntent(REQUEST_PICK_IMAGE)
        mViewMvc.closeFabMenu()
    }

    override fun onFabMenuVideoClicked() {
        mViewMvc.closeFabMenu()
    }

    override fun onHomeClicked() {
        mScreensNavigator.toHome()
    }

    override fun onExploreClicked() {
        mScreensNavigator.toExplore()
    }

    override fun onMessagesClicked() {
        mScreensNavigator.toMessages()
    }

    override fun onProfileClicked() {
        mScreensNavigator.toProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    mScreensNavigator.toPostImage(data.dataString!!)
                }
                REQUEST_TAKE_IMAGE -> {
                    if (data.extras != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap
                        mScreensNavigator.toPostImage(imageBitmap)
                    }
                }
                else -> {
                    mActivityResultListeners.forEach { listener ->
                        listener.onRequestResultReceived(requestCode, resultCode, data)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus

            if (v is EditText) {

                // get current rect of focus
                val l = IntArray(2)
                v.getLocationOnScreen(l)
                val rect = Rect(l[0], l[1], l[0] + v.width, l[1] + v.height)

                // check if touch event is not inside the focus
                if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {

                    // dirty way to check if touch event is on sendButton in ChatFragment
                    if (v.id == R.id.et_input_message &&
                        event.rawY <= rect.bottom &&
                        event.rawY >= rect.top &&
                        event.rawX >= rect.right
                    ) return super.dispatchTouchEvent(event)

                    // clear focus and hide soft keyboard
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun dispatchImagePickerIntent(requestCode: Int) {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, requestCode)
    }

    override fun dispatchTakeImageIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_TAKE_IMAGE)
    }

    companion object {
        private const val REQUEST_PICK_IMAGE = 7890
        private const val REQUEST_TAKE_IMAGE = 9032
    }
}