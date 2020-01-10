package com.amit.anim;

import android.animation.TimeInterpolator;

import androidx.annotation.NonNull;

/**
 * Created by Amit Jangid on 21,May,2018
 **/
public class EaseInterpolator implements TimeInterpolator
{
    private final Ease ease;

    public EaseInterpolator(@NonNull Ease ease)
    {
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float input)
    {
        return EaseProvider.get(this.ease, input);
    }

    public Ease getEase()
    {
        return ease;
    }
}
