package com.amit.interfaces;

public interface AnimButton
{
    void startAnimation();
    void revertAnimation();
    void revertAnimation(final OnAnimEndListener onAnimEndListener);

    void dispose();
    void resetProgress();
    void setProgress(int progress);
}
