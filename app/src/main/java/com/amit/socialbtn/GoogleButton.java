package com.amit.socialbtn;

import android.content.Context;
import android.util.AttributeSet;

import com.amit.R;

public class GoogleButton extends BaseButton
{
    public GoogleButton(Context context, AttributeSet attrs)
    {
        super(context, attrs, R.color.google, R.drawable.google_logo);
    }

    public GoogleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle, R.color.google, R.drawable.google_logo);
    }

    public GoogleButton(Context context)
    {
        super(context);
    }
}
