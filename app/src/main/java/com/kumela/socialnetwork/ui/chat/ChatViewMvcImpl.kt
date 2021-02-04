package com.kumela.socialnetwork.ui.chat

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.MessageList
import com.kumela.socialnetwork.ui.adapters.messages.MessageAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnetwork.ui.views.ChatUploadGroupView

/**
 * Created by Toko on 24,September,2020
 **/

class ChatViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<ChatViewMvc.Listener>(
    inflater, parent, R.layout.fragment_chat
), ChatViewMvc, MessageAdapter.Listener {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val recyclerMessages: RecyclerView = findViewById(R.id.recycler_messages)
    private val etMessage: EditText = findViewById(R.id.et_input_message)
    private val buttonPlus: ImageButton = findViewById(R.id.button_plus)
    private val buttonMinus: ImageButton = findViewById(R.id.button_minus)
    private val buttonSend: ImageButton = findViewById(R.id.button_send)
    private val viewUploadGroupBackground: ChatUploadGroupView =
        findViewById(R.id.upload_ground_background)
    private val buttonPicture: ImageButton = findViewById(R.id.button_picture)
    private val buttonVideo: ImageButton = findViewById(R.id.button_video)
    private val buttonAudio: ImageButton = findViewById(R.id.button_audio)

    private val chatAdapter = MessageAdapter(viewMvcFactory, this)

    init {
        // initialize toolbar
        toolbarViewMvc.setBackgroundColor(R.color.primary)
        toolbarViewMvc.setTitleStyle(22f, R.color.white, true, TextView.TEXT_ALIGNMENT_CENTER)
        toolbarViewMvc.tintIcons(R.color.white)
        toolbarViewMvc.enableUpButtonAndListen { listener?.onBackPressed() }
        toolbar.addView(toolbarViewMvc.rootView)

        recyclerMessages.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerMessages.postDelayed({ listener?.onKeyboardUp() }, 100)
            }
        }
        buttonPlus.setOnClickListener { listener?.onPlusClicked() }
        buttonMinus.setOnClickListener { listener?.onMinusClicked() }
        buttonPicture.setOnClickListener { listener?.onPictureClicked() }
        buttonVideo.setOnClickListener { listener?.onVideoClicked() }
        buttonAudio.setOnClickListener { listener?.onAudioClicked() }

        buttonSend.setOnClickListener { listener?.onSendClicked() }

        // initialize recycler view
        recyclerMessages.apply {
            val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            lm.stackFromEnd = true
            layoutManager = lm
            adapter = chatAdapter
            setHasFixedSize(true)
        }
    }

    override fun bindToolbarText(text: String) {
        toolbarViewMvc.setTitle(text)
    }

    override fun scrollToBottom() {
        recyclerMessages.postDelayed(
            { recyclerMessages.smoothScrollToPosition(0) },
            100
        )
    }

    override fun showUploadOptions() {
        buttonPlus.animate()
            .alpha(0f)
            .start()

        buttonMinus.visibility = View.VISIBLE
        buttonMinus.animate().alpha(.8f).start()

        viewUploadGroupBackground.visibility = View.VISIBLE
        viewUploadGroupBackground.animate().alpha(1f).start()

        buttonPicture.visibility = View.VISIBLE
        buttonVideo.visibility = View.VISIBLE
        buttonAudio.visibility = View.VISIBLE
        ValueAnimator.ofFloat(0f, context.resources.getDimension(R.dimen.chat_group_radius)).apply {
            duration = 300
            addUpdateListener {
                val radius = (animatedValue as Float).toInt()
                val scale = (animatedFraction + 1) * .5f
                val alpha = animatedFraction
                animateUploadGroupItem(buttonPicture, radius, alpha, scale)
                animateUploadGroupItem(buttonVideo, radius, alpha, scale)
                animateUploadGroupItem(buttonAudio, radius, alpha, scale)
            }
            start()
        }
    }

    override fun hideUploadOptions() {
        buttonPlus.animate().alpha(1f).start()

        buttonMinus.animate()
            .alpha(0f)
            .withEndAction { buttonMinus.visibility = View.GONE }
            .start()

        viewUploadGroupBackground.animate()
            .alpha(0f)
            .withEndAction { viewUploadGroupBackground.visibility = View.GONE }
            .start()

        ValueAnimator.ofFloat(context.resources.getDimension(R.dimen.chat_group_radius),0f).apply {
            duration = 300
            addUpdateListener {
                val radius = (animatedValue as Float).toInt()
                val scale = (animatedFraction + 1) * .5f
                val alpha = animatedFraction
                animateUploadGroupItem(buttonPicture, radius, alpha, scale)
                animateUploadGroupItem(buttonVideo, radius, alpha, scale)
                animateUploadGroupItem(buttonAudio, radius, alpha, scale)
            }
            doOnEnd {
                buttonPicture.visibility = View.GONE
                buttonVideo.visibility = View.GONE
                buttonAudio.visibility = View.GONE
            }
            start()
        }
    }

    private fun animateUploadGroupItem(v: View, radius: Int, alpha: Float, scale: Float) {
        val layoutParams = v.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.circleRadius = radius
        v.layoutParams = layoutParams
        v.alpha = alpha
        v.scaleX = scale
        v.scaleY =scale
    }

    override fun addMessages(messages: List<MessageList>) {
        chatAdapter.addMessages(messages)
    }

    override fun updateMessage(message: MessageList) {
        chatAdapter.updateMessage(message)
    }

    override fun getMessage(): String = etMessage.text.toString()

    override fun clearInputField() {
        etMessage.text = null
    }

    // chat recycler callbacks
    override fun onScrolledToTop() {
        listener?.onScrolledToTop()
    }

    override fun onMessageLongClick(message: MessageList) {
        listener?.onMessageLongClick(message)
    }
}