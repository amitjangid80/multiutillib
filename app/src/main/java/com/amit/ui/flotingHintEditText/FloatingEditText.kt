package com.amit.ui.flotingHintEditText

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent

/**
 * Created by AMIT JANGID on 2019-08-06.
**/
@SuppressLint("ClickableViewAccessibility")
class FloatingEditText : AppCompatEditText
{
    private var clickable: Boolean = true

    private var onBackPressed: Runnable? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 2019 August 06 - Tuesday - 11:13 AM
     * On Back Pressed Listener method
     *
     * custom event for clicking back when on this view is active
    **/
    fun setOnBackPressedListener(onBackPressed: Runnable)
    {
        this.onBackPressed = onBackPressed
    }

    override fun setClickable(clickable: Boolean)
    {
        super.setClickable(clickable)
        this.clickable = clickable
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        return if (clickable)
        {
            super.onTouchEvent(event)
        }
        else
        {
            false
        }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean
    {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK)
        {
            this.clearFocus()
            onBackPressed?.run()
        }

        return super.onKeyPreIme(keyCode, event)
    }
}
