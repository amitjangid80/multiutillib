package com.amit.socialbtn;

import android.content.Context;
import android.util.AttributeSet;

import com.amit.R;

public class FacebookButton extends BaseButton
{
    public FacebookButton(Context context, AttributeSet attrs)
    {
        super(context,attrs,R.color.facebook, R.drawable.fb_logo);
    }

    public FacebookButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs,defStyle,R.color.facebook, R.drawable.fb_logo);
    }

    public FacebookButton(Context context)
    {
        super(context);
    }
}
