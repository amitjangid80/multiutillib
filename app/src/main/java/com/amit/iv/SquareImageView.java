package com.amit.iv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/*
 * Created by AMIT JANGID on 20-Mar-18.
 */

public class SquareImageView extends AppCompatImageView
{
    public SquareImageView(Context context)
    {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); // Snam to Width
    }
}
