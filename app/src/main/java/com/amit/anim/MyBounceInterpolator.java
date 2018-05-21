package com.amit.anim;

import android.view.animation.Interpolator;

class MyBounceInterpolator implements Interpolator
{
    private double mAmplitude = 1;
    private double mFrequency = 10;

    MyBounceInterpolator(double amplitude, double frequency)
    {
        this.mAmplitude = amplitude;
        this.mFrequency = frequency;
    }

    public float getInterpolation(float time)
    {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) * Math.cos(mFrequency * time) + 1);
    }
}
