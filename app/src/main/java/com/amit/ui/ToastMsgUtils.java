package com.amit.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.amit.R;

/**
 * Created by AMIT JANGID
 * 2018 April 21 - Saturday - 05:00 PM
 *
 * Toast message utils class
**/
@SuppressWarnings({"WeakerAccess", "unused"})
public class ToastMsgUtils
{
    private ToastMsgUtils()
    {

    }

    static Drawable tintIcon(@NonNull Drawable drawable, @ColorInt int tintColor)
    {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor)
    {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(context, R.drawable.toast_frame);
        return tintIcon(toastDrawable, tintColor);
    }

    static void setBackground(@NonNull View view, Drawable drawable)
    {
        view.setBackground(drawable);
    }

    static Drawable getDrawable(@NonNull Context context, @DrawableRes int id)
    {
        return ContextCompat.getDrawable(context, id);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            return context.getDrawable(id);
        }
        else
        {
            return context.getResources().getDrawable(id, null);
        }*/
    }
}
