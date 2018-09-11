package com.amit.anim;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amit.R;

/**
 * 2018 May 14 - Monday - 03:05 PM
 * This AnimUtil class will help with animation
**/
@SuppressWarnings("unused")
public class AnimUtil
{
    private static final String TAG = AnimUtil.class.getSimpleName();

    /**
     * slide activity from right to left
     * this method will make the activity to slide in from right and slide out from left.
     *
     * @param context - context of the activity
    **/
    public static void slideActivityFromRightToLeft(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromRightToLeft: exception while animating activity:\n");
            e.printStackTrace();
        }
    }

    /**
     * slide Activity From Left To Right
     * this activity will make the activity to slide in from left and slide out from right
     *
     * @param context - context of the activity
     **/
    public static void slideActivityFromLeftToRight(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromLeftToRight: exception while animating activity:\n");
            e.printStackTrace();
        }
    }

    /**
     * fade in face out activity
     * this method will make activity fade in or out.
     *
     * @param context - context of the activity
    **/
    public static void activityFadeInFadeOut(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        catch (Exception e)
        {
            Log.e(TAG, "activityFadeInFadeOut: exception while animating acivity.");
            e.printStackTrace();
        }
    }

    /**
     * slide from left with stay anim
     * this method will make activity slide in from left with stay
     *
     * @param context - context of the activity
    **/
    public static void slideActivityFromLeftWithStay(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.anim_stay);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromLeftWithStay: exception while animating activity.");
            e.printStackTrace();
        }
    }

    /**
     * slide from right with stay anim
     * this method will make activity slide in from right with stay
     *
     * @param context - context of the activity
    **/
    public static void slideActivityFromRightWithStay(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.anim_stay);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromRightWithStay: exception while animating activity.");
            e.printStackTrace();
        }
    }

    /**
     * slide from bottom to up
     * this method will make the activity to slide from bottom to up with stay.
     *
     * @param context - context of the activity
    **/
    public static void slideActivityFromBottomWithStay(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.bottom_to_up, R.anim.anim_stay);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromBottomWithStay: exception while animating.");
            e.printStackTrace();
        }
    }

    /**
     * slide from up to bottom
     * this method will make the activity to slide from up to bottom with stay.
     *
     * @param context - context of the activity
    **/
    public static void slideActivityFromUpWithStay(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.up_to_bottom, R.anim.anim_stay);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideFromUpWithStayAnim: exception while animating.");
            e.printStackTrace();
        }
    }

    /**
     * slide from bottom to up
     * this method will make the activity to slide from bottom to up.
     *
     * @param context - context of the activity
     **/
    public static void slideActivityFromBottomToUp(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom1);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromBottomToUp: exception while animating.");
            e.printStackTrace();
        }
    }

    /**
     * slide from up to bottom
     * this method will make the activity to slide from up to bottom.
     *
     * @param context - context of the activity
     **/
    public static void slideActivityFromUpToBottom(@NonNull Context context)
    {
        try
        {
            ((Activity) context).overridePendingTransition(R.anim.up_to_bottom, R.anim.bottom_to_up1);
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideActivityFromUpToBottom: exception while animating.");
            e.printStackTrace();
        }
    }

    /**
     * explode transition
     * this method will make any view to explode or popup
     * this method will working with api with or greater then Lollipop
     *
     * @param context - context of the application
     *
     * @param viewGroup - viewGroup on which transition is to be done
     *                    example: relative layout, linear layout or any view
     *
     * @param duration - duration of the transition
    **/
    @TargetApi(21)
    public static void explodeTransition(@NonNull Context context,
                                         ViewGroup viewGroup,
                                         int duration)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                viewGroup.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.explode);
                animation.setDuration(duration);
                viewGroup.setAnimation(animation);
                viewGroup.animate();
                animation.start();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "explodeTranistion: exception while making transition:\n");
            e.printStackTrace();
        }
    }

    /**
     * slide anim from right
     * this method will make any view slide from right
     *
     * @param context - context of the application
     * @param view - view to animate
     * @param duration - duration of animation
    **/
    public static void slideAnimFromRight(@NonNull Context context,
                                          @NonNull View view,
                                          int duration)
    {
        try
        {
            view.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            animation.setDuration(duration);
            view.animate();
            animation.start();
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideAnimFrom: exception while making animation.");
            e.printStackTrace();
        }
    }

    /**
     * slide anim from left
     * this method will make any view slide from right
     *
     * @param context - context of the application
     * @param view - view to animate
     * @param duration - duration of animation
    **/
    public static void slideAnimFromLeft(@NonNull Context context,
                                         @NonNull View view,
                                         int duration)
    {
        try
        {
            view.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            animation.setDuration(duration);
            view.animate();
            animation.start();
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideAnimFrom: exception while making animation.");
            e.printStackTrace();
        }
    }

    /**
     * slide anim
     * this method can be used for setting your own slide animation
     *
     * @param context - context of the application
     * @param view - view on which animation is to be performed
     * @param duration - duration of the animation
     * @param animResId - anim resouce for animation
    **/
    public static void slideAnim(@NonNull Context context,
                                 @NonNull View view,
                                 int duration,
                                 @AnimRes int animResId)
    {
        try
        {
            view.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, animResId);
            animation.setDuration(duration);
            view.animate();
            animation.start();
        }
        catch (Exception e)
        {
            Log.e(TAG, "slideAnim: exception while making animation.");
            e.printStackTrace();
        }
    }

    /**
     * Bounce anim method
     * this method will make a view bounce
     *
     * @param context - context of the application
     * @param view - view to animate
     **/
    public static void bounceAnim(Context context, View view)
    {
        try
        {
            final Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            animation.setInterpolator(interpolator);
            view.startAnimation(animation);
        }
        catch (Exception e)
        {
            Log.e(TAG, "bounceAnim: exception while making bounce animation.");
            e.printStackTrace();
        }
    }
}
