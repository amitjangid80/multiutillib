package com.amit.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.amit.R;
import com.amit.animDrawable.CircularAnimatedDrawable;
import com.amit.animDrawable.CircularRevealAnimatedDrawable;
import com.amit.interfaces.AnimButton;
import com.amit.interfaces.CustomizeByCode;
import com.amit.interfaces.OnAnimEndListener;
import com.amit.utilities.Utils;

public class ProgressImgBtn extends AppCompatImageButton implements AnimButton, CustomizeByCode
{
    private enum State
    {
        PROGRESS, IDLE, DONE, STOPED
    }

    private State mState;
    private Params mParams;

    private Drawable mSrc;
    private AnimatorSet mMorphingAnimatorSet;

    private int progress;
    private int mFillColorDone;
    private Bitmap mBitmapDone;

    private boolean layoutDone;
    private boolean doneWhileMorphing;
    private boolean shouldStartAnimation;
    private boolean mIsMorphingInProgress;

    private GradientDrawable mGradientDrawable;
    private CircularAnimatedDrawable mAnimatedDrawable;
    private CircularRevealAnimatedDrawable mRevealDrawable;

    /**
     * Constructor of the class
     *
     * @param context - context of the application
    **/
    public ProgressImgBtn(Context context)
    {
        super(context);
        init(context, null, 0, 0);
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     * @param attrs - attribute set
    **/
    public ProgressImgBtn(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     * @param attrs - attribute set
     * @param defStyleAttr - default style attr
    **/
    public ProgressImgBtn(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     * @param attrs - attr set
     * @param defStyleAttr - default style attr
     * @param defStyleRes - default style res
    **/
    @TargetApi(23)
    public ProgressImgBtn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Common initializer method.
     *
     * @param context - Context
     * @param attrs - Attributes passed in the XML
    **/
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        mParams = new Params();
        mParams.progressBarPadding = 0f;
        ProgressBtn.BackgroundAndMorphingDrawables drawables;

        if (attrs == null)
        {
            drawables = ProgressBtn.loadGradientDrawable(Utils.getDrawable(getContext(), R.drawable.default_btn_shape));
        }
        else
        {
            int[] attrsArray = new int[]{
                    android.R.attr.background, // 0
            };

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressBtn, defStyleAttr, defStyleRes);
            TypedArray taBg = context.obtainStyledAttributes(attrs, attrsArray, defStyleAttr, defStyleRes);
            drawables = ProgressBtn.loadGradientDrawable(taBg.getDrawable(0));

            mParams.initialCornerRadius = ta.getDimension(R.styleable.ProgressBtn_initialCornerRadius, 0);
            mParams.finalCornerRadius = ta.getDimension(R.styleable.ProgressBtn_finalCornerRadius, 100);
            mParams.progressBarWidth = ta.getDimension(R.styleable.ProgressBtn_progressBarWidth, 10);
            mParams.progressBarPadding = ta.getDimension(R.styleable.ProgressBtn_progressBarPadding, 0);

            mParams.progressBarColor = ta.getColor(R.styleable.ProgressBtn_progressBarColor,
                    Utils.getColorWrapper(context, android.R.color.black));

            ta.recycle();
            taBg.recycle();
        }

        mState = State.IDLE;

        if (drawables != null)
        {
            mGradientDrawable = drawables.morphingDrawable;

            if (drawables.backGroundDrawable != null)
            {
                setBackground(drawables.backGroundDrawable);
            }
        }

        resetProgress();
    }

    @Override
    public void setBackgroundColor(int color)
    {
        mGradientDrawable.setColor(color);
    }

    @Override
    public void setBackgroundResource(@ColorRes int resId)
    {
        mGradientDrawable.setColor(ContextCompat.getColor(getContext(), resId));
    }

    @Override
    public void setProgressBarColor(int color)
    {
        mParams.progressBarColor = color;

        if (mAnimatedDrawable != null)
        {
            mAnimatedDrawable.setLoadingBarColor(color);
        }
    }

    @Override
    public void setProgressBarWidth(float width)
    {
        mParams.progressBarWidth = width;
    }

    @Override
    public void setDoneColor(int color)
    {
        mParams.doneColor = color;
    }

    @Override
    public void setProgressBarPadding(float padding)
    {
        mParams.progressBarPadding = padding;
    }

    @Override
    public void setInitialHeight(int height)
    {
        mParams.initialHeight = height;
    }

    @Override
    public void setInitialCornerRadius(float radius)
    {
        mParams.initialCornerRadius = radius;
    }

    @Override
    public void setFinalCornerRadius(float radius)
    {
        mParams.finalCornerRadius = radius;
    }

    /**
     * This method is called when the button and its dependencies are going to draw it selves.
     *
     * @param canvas Canvas
    **/
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        layoutDone = true;

        if (shouldStartAnimation)
        {
            startAnimation();
        }

        if (mState == State.PROGRESS && !mIsMorphingInProgress)
        {
            drawProgress(canvas);
        }
        else if (mState == State.DONE)
        {
            drawDoneAnimation(canvas);
        }
    }

    /**
     * If the mAnimatedDrawable is null or its not running,
     * it get created. Otherwise its draw method is
     * called here.
     *
     * @param canvas Canvas
    **/
    private void drawProgress(Canvas canvas)
    {
        if (mAnimatedDrawable == null || !mAnimatedDrawable.isRunning())
        {
            mAnimatedDrawable = new CircularAnimatedDrawable(this,
                    mParams.progressBarWidth,
                    mParams.progressBarColor);

            int offset = (getWidth() - getHeight()) / 2;
            int left = offset + mParams.progressBarPadding.intValue();
            int right = getWidth() - offset - mParams.progressBarPadding.intValue();
            int bottom = getHeight() - mParams.progressBarPadding.intValue();
            int top = mParams.progressBarPadding.intValue();

            mAnimatedDrawable.setBounds(left, top, right, bottom);
            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
        }
        else
        {
            mAnimatedDrawable.setProgress(progress);
            mAnimatedDrawable.draw(canvas);
        }
    }

    /**
     * set progress
     * this method sets the progress
     *
     * @param progress set a progress to switch displaying a determinate circular progress
    **/
    public void setProgress(int progress)
    {
        progress = Math.max(
                CircularAnimatedDrawable.MIN_PROGRESS,
                Math.min(CircularAnimatedDrawable.MAX_PROGRESS, progress));

        this.progress = progress;
    }

    /**
     * resets a given progress and shows an indeterminate progress animation
    **/
    public void resetProgress()
    {
        this.progress = CircularAnimatedDrawable.MIN_PROGRESS - 1;
    }

    /**
     * Stops the animation and sets the button in the STOPED state.
    **/
    public void stopAnimation()
    {
        if (mState == State.PROGRESS && !mIsMorphingInProgress)
        {
            mState = State.STOPED;
            mAnimatedDrawable.stop();
        }
    }

    /**
     * Call this method when you want to show a 'completed' or a 'done' status. You have to choose the
     * color and the image to be shown. If your loading progress ended with a success status you probrably
     * want to put a icon for "sucess" and a blue color, otherwise red and a failure icon. You can also
     * show that a music is completed... or show some status on a game... be creative!
     *
     * @param fillColor The color of the background of the button
     * @param bitmap    The image that will be shown
    **/
    public void doneLoadingAnimation(int fillColor, Bitmap bitmap)
    {
        if (mState != State.PROGRESS)
        {
            return;
        }

        if (mIsMorphingInProgress)
        {
            doneWhileMorphing = true;
            mFillColorDone = fillColor;
            mBitmapDone = bitmap;
            return;
        }

        mState = State.DONE;

        if (mAnimatedDrawable != null)
        {
            mAnimatedDrawable.stop();
        }

        mRevealDrawable = new CircularRevealAnimatedDrawable(this, fillColor, bitmap);

        int left = 0;
        int right = getWidth();
        int bottom = getHeight();
        int top = 0;

        mRevealDrawable.setBounds(left, top, right, bottom);
        mRevealDrawable.setCallback(this);
        mRevealDrawable.start();
    }

    /**
     * Method called on the onDraw when the button is on DONE status
     *
     * @param canvas Canvas
    **/
    private void drawDoneAnimation(Canvas canvas)
    {
        mRevealDrawable.draw(canvas);
    }

    public void revertAnimation()
    {
        revertAnimation(null);
    }

    public void revertAnimation(final OnAnimEndListener onAnimEndListener)
    {
        mState = State.IDLE;
        resetProgress();

        if (mAnimatedDrawable != null && mAnimatedDrawable.isRunning())
        {
            stopAnimation();
        }

        if (mIsMorphingInProgress)
        {
            mMorphingAnimatorSet.cancel();
        }

        setClickable(false);
        int fromWidth = getWidth();
        int fromHeight = getHeight();

        int toHeight = mParams.initialHeight;
        int toWidth = mParams.initialWdith;

        ObjectAnimator cornerAnimation =
                ObjectAnimator.ofFloat(mGradientDrawable,
                        "cornerRadius",
                        mParams.finalCornerRadius,
                        mParams.initialCornerRadius);

        ValueAnimator widthAnimation = ValueAnimator.ofInt(fromWidth, toWidth);

        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = val;
                setLayoutParams(layoutParams);
            }
        });

        ValueAnimator heightAnimation = ValueAnimator.ofInt(fromHeight, toHeight);

        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = val;
                setLayoutParams(layoutParams);
            }
        });

        /*ValueAnimator strokeAnimation = ValueAnimator.ofFloat(
                getResources().getDimension(R.dimen.stroke_login_button),
                getResources().getDimension(R.dimen.stroke_login_button_loading));
        strokeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((ShapeDrawable)mGradientDrawable).getPaint().setStrokeWidth((Float)animation.getAnimatedValue());
            }
        });*/

        mMorphingAnimatorSet = new AnimatorSet();
        mMorphingAnimatorSet.setDuration(300);
        mMorphingAnimatorSet.playTogether(cornerAnimation, widthAnimation, heightAnimation);

        mMorphingAnimatorSet.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                setImageDrawable(mSrc);
                setClickable(true);
                mIsMorphingInProgress = false;

                if (onAnimEndListener != null)
                {
                    onAnimEndListener.onAnimEnd();
                }
            }
        });

        mIsMorphingInProgress = true;
        mMorphingAnimatorSet.start();
    }

    @Override
    public void dispose()
    {
        if (mMorphingAnimatorSet != null)
        {
            mMorphingAnimatorSet.end();
            mMorphingAnimatorSet.removeAllListeners();
            mMorphingAnimatorSet.cancel();
        }

        mMorphingAnimatorSet = null;
    }

    /**
     * Method called to start the animation.
     * Morphs in to a ball and then starts a loading spinner.
    **/
    public void startAnimation()
    {
        if (mState != State.IDLE) {
            return;
        }

        if (!layoutDone)
        {
            shouldStartAnimation = true;
            return;
        }

        shouldStartAnimation = false;

        if (mIsMorphingInProgress)
        {
            mMorphingAnimatorSet.cancel();
        }
        else
        {
            mParams.initialWdith = getWidth();
            mParams.initialHeight = getHeight();
        }

        mState = State.PROGRESS;
        mSrc = this.getDrawable();

        this.setImageDrawable(null);
        this.setClickable(false);

        int toHeight = mParams.initialHeight;
        int toWidth = toHeight;

        ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(
                mGradientDrawable,
               "cornerRadius",
                mParams.initialCornerRadius,
                mParams.finalCornerRadius);

        ValueAnimator widthAnimation = ValueAnimator.ofInt(mParams.initialWdith, toWidth);

        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = val;
                setLayoutParams(layoutParams);
            }
        });

        ValueAnimator heightAnimation = ValueAnimator.ofInt(mParams.initialHeight, toHeight);

        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = val;
                setLayoutParams(layoutParams);
            }
        });

        /*ValueAnimator strokeAnimation = ValueAnimator.ofFloat(
                getResources().getDimension(R.dimen.stroke_login_button),
                getResources().getDimension(R.dimen.stroke_login_button_loading));
        strokeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((ShapeDrawable)mGradientDrawable).getPaint().setStrokeWidth((Float)animation.getAnimatedValue());
            }
        });*/

        mMorphingAnimatorSet = new AnimatorSet();
        mMorphingAnimatorSet.setDuration(300);
        mMorphingAnimatorSet.playTogether(cornerAnimation, widthAnimation, heightAnimation);

        mMorphingAnimatorSet.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mIsMorphingInProgress = false;

                if (doneWhileMorphing)
                {
                    doneWhileMorphing = false;

                    Runnable runnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            doneLoadingAnimation(mFillColorDone, mBitmapDone);
                        }
                    };

                    new Handler().postDelayed(runnable, 50);
                }
            }
        });

        mIsMorphingInProgress = true;
        mMorphingAnimatorSet.start();
    }

    /**
     * Check if button is animating
    **/
    public Boolean isAnimating()
    {
        return mState == State.PROGRESS;
    }

    /**
     * Class with all the params to configure the button.
    **/
    private class Params
    {
        private float progressBarWidth;
        private int progressBarColor;
        private int doneColor;
        private Float progressBarPadding;
        private Integer initialHeight;
        private int initialWdith;
        private float initialCornerRadius;
        private float finalCornerRadius;
    }
}
