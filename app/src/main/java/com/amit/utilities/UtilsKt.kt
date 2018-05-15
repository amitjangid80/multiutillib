package com.amit.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build

public class UtilsKt
{
    @SuppressWarnings("deprecation")
    companion object
    {
        fun getColorWrapper(context: Context, id: Int) : Int
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                return context.getColor(id)
            }
            else
            {
                return context.getResources().getColor(id)
            }
        }
    }

    @SuppressWarnings("deprecation")
    fun getDrawable(context: Context, id: Int) : Drawable
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            return context.getDrawable(id)
        }
        else
        {
            return context.getResources().getDrawable(id)
        }
    }
}
