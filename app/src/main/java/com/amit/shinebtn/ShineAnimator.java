package com.amit.shinebtn;

import android.animation.ValueAnimator;
import android.graphics.Canvas;

import com.amit.anim.Ease;
import com.amit.anim.EaseInterpolator;

public class ShineAnimator extends ValueAnimator
{
    private float MAX_VALUE = 1.5f;
    private long ANIM_DURATION = 1500;
    private Canvas canvas;

    ShineAnimator()
    {
        setFloatValues(1f, MAX_VALUE);
        setDuration(ANIM_DURATION);
        setStartDelay(200);
        setInterpolator(new EaseInterpolator(Ease.QUART_OUT));
    }

    ShineAnimator(long duration,float max_value,long delay)
    {
        setFloatValues(1f, max_value);
        setDuration(duration);
        setStartDelay(delay);
        setInterpolator(new EaseInterpolator(Ease.QUART_OUT));
    }

    public void startAnim(final ShineView shineView, final int centerAnimX, final int centerAnimY)
    {
        start();
    }

    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
    }
}
