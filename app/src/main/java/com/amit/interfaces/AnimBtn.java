package com.amit.interfaces;

public interface AnimBtn
{
    void startAnimation();
    void revertAnimation();
    void revertAnimation(final OnAnimEndListener onAnimEndListener);
    void dispose();
    void setProgress(int progress);
    void resetProgress();
}
