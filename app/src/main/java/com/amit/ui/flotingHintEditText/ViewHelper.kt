package com.amit.ui.flotingHintEditText

import android.content.Context

/**
 * Created by AMIT JANGID on 2019-08-06.
**/
internal object ViewHelper
{
    internal fun getDp(context: Context, int: Int) : Int
    {
        val scale = context.resources.displayMetrics.density

        return (int * scale + 0.5f).toInt()
    }
}
