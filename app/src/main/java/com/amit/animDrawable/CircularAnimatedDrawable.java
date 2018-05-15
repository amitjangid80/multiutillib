package com.amit.animDrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class CircularAnimatedDrawable extends Drawable implements Animatable
{
    public static final int MIN_PROGRESS = 0;
    public static final int MAX_PROGRESS = 100;

    private AnimatorSet mAnimatorSet;
    private ValueAnimator mValueAnimatorAngle;
    private ValueAnimator mValueAnimatorSweep;
    private ValueAnimator mValueAnimatorProgress;

    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new AccelerateDecelerateInterpolator();

    private static final Float MIN_SWEEP_ANGLE = 50f;
    private static final int SWEEP_ANIMATOR_DURATION = 700;
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final int PROGRESS_ANIMATOR_DURATION = 200;

    private Paint mPaint;
    private View mAnimatedView;
    private final RectF fBounds = new RectF();

    private float mBorderWidth;
    private float mCurrentSweepAngle;
    private float mCurrentGlobalAngle;
    private float mCurrentGlobalAngleOffset;

    private boolean mModeAppearing;
    private boolean mRunning;

    private int progress;
    private boolean shouldDraw;
    private float shownProgress;

    /**
     * circular animated drawable
     * constructor of the class
     *
     * @param view View to be animated
     * @param borderWidth The width of the spinning bar
     * @param arcColor The color of the spinning bar
    **/
    public CircularAnimatedDrawable(View view, float borderWidth, int arcColor)
    {
        mAnimatedView = view;
        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(arcColor);

        setupAnimations();
        shouldDraw = true;
        mAnimatorSet = new AnimatorSet();
    }

    @Override
    protected void onBoundsChange(Rect bounds)
    {
        super.onBoundsChange(bounds);

        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;

        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.top + mBorderWidth / 2f - .5f;
    }

    /**
     * set loading bar color
     * this method will set the lading or progress bar color
     *
     * @param color - color for progress or loading bar
    **/
    public void setLoadingBarColor(int color)
    {
        mPaint.setColor(color);
    }

    /**
     * start
     * this method will start the animation
    **/
    @Override
    public void start()
    {
        if (isRunning())
        {
            return;
        }

        mRunning = true;
        mAnimatorSet.cancel();
    }

    /**
     * stop
     * this method will stop the animation
    **/
    @Override
    public void stop()
    {
        if (!isRunning())
        {
            return;
        }

        mRunning = false;
        mAnimatorSet.cancel();
    }

    /**
     * is running
     * this method will inform if the animation is in progress or not
    **/
    @Override
    public boolean isRunning()
    {
        return mRunning;
    }

    /**
     * draw
     * this method is called when the drawable is going to draw itself
     *
     * @param canvas - canvas
    **/
    @Override
    public void draw(@NonNull Canvas canvas)
    {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;

        if (progress >= MIN_PROGRESS && progress <= MAX_PROGRESS)
        {
            startAngle = -90;
            sweepAngle = shownProgress;
        }
        else if (!mModeAppearing)
        {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        }
        else
        {
            sweepAngle += MIN_SWEEP_ANGLE;
        }

        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha)
    {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter)
    {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * set up animation
     * in this method all the animations are set
    **/
    private void setupAnimations()
    {
        mValueAnimatorAngle = ValueAnimator.ofFloat(0, 360f);
        mValueAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mValueAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mValueAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

        mValueAnimatorAngle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mCurrentGlobalAngle = (float) animation.getAnimatedValue();
            }
        });

        mValueAnimatorSweep = ValueAnimator.ofFloat(0, 360f - 2 * MIN_SWEEP_ANGLE);
        mValueAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mValueAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mValueAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);

        mValueAnimatorSweep.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationRepeat(Animator animation)
            {
                toggleAppearingMode();
                shouldDraw = false;
            }
        });

        mValueAnimatorSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mCurrentSweepAngle = (float) animation.getAnimatedValue();

                if (mCurrentSweepAngle < 5)
                {
                    shouldDraw = true;
                }

                if (shouldDraw)
                {
                    mAnimatedView.invalidate();
                }
            }
        });

        // progress animation is set up on each change of progress val
    }

    /**
     * toggle appearing mode
     * this method is called in every repetition of the animation,
     * so the animation makes the sweep growing and
     * then make it shrinking
    **/
    private void toggleAppearingMode()
    {
        mModeAppearing = !mModeAppearing;

        if (mModeAppearing)
        {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    /**
     * dispose
     * this method will dispose or end the progress
    **/
    public void dispose()
    {
        if (mValueAnimatorAngle != null)
        {
            mValueAnimatorAngle.end();;
            mValueAnimatorAngle.removeAllUpdateListeners();
            mValueAnimatorAngle.cancel();
        }

        mValueAnimatorAngle = null;

        if (mValueAnimatorSweep != null)
        {
            mValueAnimatorSweep.end();
            mValueAnimatorSweep.removeAllUpdateListeners();
            mValueAnimatorSweep.cancel();
        }

        mValueAnimatorSweep = null;

        if (mValueAnimatorProgress != null)
        {
            if (mValueAnimatorProgress.isRunning())
            {
                mValueAnimatorProgress.end();
            }

            mValueAnimatorProgress.removeAllUpdateListeners();
            mValueAnimatorProgress.cancel();
        }

        if (mAnimatorSet != null)
        {
            mAnimatorSet.end();
            mAnimatorSet.cancel();
        }
    }

    /**
     * set progress
     * this method will the progress
     *
     * @param progress - progress to be set
    **/
    public void setProgress(int progress)
    {
        if (this.progress == progress)
        {
            return;
        }

        this.progress = progress;

        if (progress < MIN_PROGRESS)
        {
            shownProgress = 0;
        }

        if (mValueAnimatorProgress == null)
        {
            mValueAnimatorProgress = ValueAnimator.ofFloat(shownProgress, progress * 3.6f);
            mValueAnimatorProgress.setInterpolator(SWEEP_INTERPOLATOR);
            mValueAnimatorProgress.setDuration(PROGRESS_ANIMATOR_DURATION);

            mValueAnimatorProgress.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    shownProgress = (float) animation.getAnimatedValue();
                    mAnimatedView.invalidate();
                }
            });
        }
        else
        {
            if (mValueAnimatorProgress.isRunning())
            {
                mValueAnimatorProgress.cancel();
            }

            mValueAnimatorProgress.setFloatValues(shownProgress, progress * 3.6f);
        }

        if (isRunning() && progress >= MIN_PROGRESS)
        {
            mValueAnimatorProgress.start();
        }
    }
}