package com.folioreader.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MovableImageView : AppCompatImageView, View.OnTouchListener {

    private var downRawX: Float = 0.toFloat()
    private var downRawY: Float = 0.toFloat()
    private var dX: Float = 0.toFloat()
    private var dY: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
        animate().alpha(0.5f).setDuration(10000).start()

    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        val layoutParams = view.getLayoutParams() as ViewGroup.MarginLayoutParams

        val action = motionEvent.action
        if (action == MotionEvent.ACTION_DOWN) {
            animate().alpha(1f).setDuration(0).start()

            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.getX() - downRawX
            dY = view.getY() - downRawY
            alpha = 1f

            return true // Consumed

        } else if (action == MotionEvent.ACTION_MOVE) {

            val viewWidth = view.getWidth()
            val viewHeight = view.getHeight()

            val viewParent = view.getParent() as View
            val parentWidth = viewParent.getWidth()
            val parentHeight = viewParent.getHeight()

            var newX = motionEvent.rawX + dX
            newX = Math.max(
                layoutParams.leftMargin.toFloat(),
                newX
            ) // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(
                (parentWidth - viewWidth - layoutParams.rightMargin).toFloat(),
                newX
            ) // Don't allow the FAB past the right hand side of the parent

            var newY = motionEvent.rawY + dY
            newY = Math.max(
                layoutParams.topMargin.toFloat(),
                newY
            ) // Don't allow the FAB past the top of the parent
            newY = Math.min(
                (parentHeight - viewHeight - layoutParams.bottomMargin).toFloat(),
                newY
            ) // Don't allow the FAB past the bottom of the parent

            view.animate().x(newX).y(newY).setDuration(0).start()

            return true // Consumed

        } else if (action == MotionEvent.ACTION_UP) {

            animate().alpha(0.5f).setDuration(5000).start()

            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY

            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY

            return if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                performClick()
            } else { // A drag
                true // Consumed
            }

        } else {
            return super.onTouchEvent(motionEvent)
        }

    }

    companion object {

        private val CLICK_DRAG_TOLERANCE =
            10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    }

}