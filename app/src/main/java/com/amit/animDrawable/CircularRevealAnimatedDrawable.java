package com.amit.animDrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CircularRevealAnimatedDrawable extends Drawable implements Animatable
{
    private boolean mIsFilled;
    private boolean isRunning;

    private Paint mPaint;
    private View mAnimatedView;
    private Paint mPaintImageReady;

    private float mRadius;
    private float mFinalRadius;
    private float mCenterWidth;
    private float mCenterHeight;
    private float bitMapXOffSet;
    private float bitMapYOffSet;

    private int mImageReadyAlpha;

    private Bitmap mReadyImage;
    private ValueAnimator mRevealInAnimation;

    /**
     * Circular reveal animated drawable
     * constructor of the class
     *
     * @param view The view that if being animated.
     * @param fillColor The color of the background that will the revealed.
     * @param bitmap The image that will be shown in the end of the animation.
    **/
    public CircularRevealAnimatedDrawable(View view, int fillColor, Bitmap bitmap)
    {
        isRunning = false;
        mAnimatedView = view;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(fillColor);

        mPaintImageReady = new Paint();
        mPaintImageReady.setAntiAlias(true);
        mPaintImageReady.setStyle(Paint.Style.FILL);
        mPaintImageReady.setColor(Color.TRANSPARENT);

        mRadius = 0;
        mImageReadyAlpha = 0;
        mReadyImage = bitmap;
    }

    public boolean isFilled()
    {
        return mIsFilled;
    }

    /**
     * on bounds change
     * this method is called when the bound changes
    **/
    @Override
    protected void onBoundsChange(Rect bounds)
    {
        super.onBoundsChange(bounds);

        int bitmapWidth = (int) ((bounds.right - bounds.left) * 0.6);
        int bitmapHeight = (int) ((bounds.bottom - bounds.top) * 0.6);

        bitMapXOffSet = (float) (((bounds.right - bounds.left) - bitmapWidth) / 2);
        bitMapYOffSet = (float) (((bounds.bottom - bounds.top) - bitmapHeight) / 2);
        mReadyImage = Bitmap.createScaledBitmap(mReadyImage, bitmapWidth, bitmapHeight, false);

        mFinalRadius = (bounds.right - bounds.left) / 2;
        mCenterWidth = (bounds.right + bounds.left) / 2;
        mCenterHeight = (bounds.bottom + bounds.top) / 2;
    }

    /**
     * set up animation
     * this method is a reveal animation to show the button background
     * and a alpha animation to show the bitmap
    **/
    private void setupAnimation()
    {
        final ValueAnimator alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.setDuration(80);

        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mImageReadyAlpha = (int) animation.getAnimatedValue();
                invalidateSelf();
                mAnimatedView.invalidate();
            }
        });

        mRevealInAnimation = ValueAnimator.ofFloat(0, mFinalRadius);
        mRevealInAnimation.setInterpolator(new DecelerateInterpolator());
        mRevealInAnimation.setDuration(120);

        mRevealInAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mRadius = (float) animation.getAnimatedValue();
                invalidateSelf();
                mAnimatedView.invalidate();
            }
        });

        mRevealInAnimation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                mIsFilled = true;
                alphaAnimator.start();
            }
        });
    }

    /**
     * start
     * this method will start the animation
    **/
    @Override
    public void start()
    {
        if (isRunning)
        {
            return;
        }

        setupAnimation();
        isRunning = true;
        mRevealInAnimation.start();
    }

    /**
     * stop
     * this method will stop the animation
    **/
    @Override
    public void stop()
    {
        if (!isRunning)
        {
            return;
        }

        isRunning = false;
        mRevealInAnimation.cancel();
    }

    /**
     * is running
     * this method set if the animation is running
     *
     * @return Return if its running or not.
    **/
    @Override
    public boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawCircle(mCenterWidth, mCenterHeight, mRadius, mPaint);

        if (mIsFilled)
        {
            mPaintImageReady.setAlpha(mImageReadyAlpha);
            canvas.drawBitmap(mReadyImage, bitMapXOffSet, bitMapYOffSet, mPaintImageReady);
        }
    }

    @Override
    public void setAlpha(int alpha)
    {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter)
    {

    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    public void dispose()
    {
        if (mRevealInAnimation != null)
        {
            mRevealInAnimation.end();
            mRevealInAnimation.removeAllUpdateListeners();
            mRevealInAnimation.cancel();
        }

        mRevealInAnimation = null;
    }
}
