package com.amit.shinebtn;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.amit.anim.Ease;
import com.amit.anim.EaseInterpolator;

import java.util.Random;

public class ShineView extends View
{
    private static final String TAG = "ShineView";
    private static long FRAME_REFRESH_DELAY = 25; //default 10ms ,change to 25ms for saving cpu.

    ShineAnimator shineAnimator;
    ValueAnimator clickAnimator;

    ShineButton shineButton;
    private Paint paint;
    private Paint paint2;
    private Paint paintSmall;

    int colorCount = 10;
    static int colorRandom[] = new int[10];

    //Customer property
    float smallOffsetAngle;
    float turnAngle;
    float shineDistanceMultiple;

    long animDuration;
    long clickAnimDuration;

    int shineCount;
    int shineSize = 0;
    int smallShineColor = colorRandom[0];
    int bigShineColor = colorRandom[1];

    boolean allowRandomColor = false;
    boolean enableFlashing = false;

    RectF rectF = new RectF();
    RectF rectFSmall = new RectF();

    Random random = new Random();
    int centerAnimX;
    int centerAnimY;
    int btnWidth;
    int btnHeight;

    double thirdLength;
    float value;
    float clickValue = 0;
    boolean isRun = false;
    private float distanceOffset = 0.2f;

    public ShineView(Context context)
    {
        super(context);
    }

    public ShineView(Context context, final ShineButton shineButton, ShineParams shineParams)
    {
        super(context);
        initShineParams(shineParams, shineButton);

        this.shineAnimator = new ShineAnimator(animDuration, shineDistanceMultiple, clickAnimDuration);
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY);
        this.shineButton = shineButton;

        paint = new Paint();
        paint.setColor(bigShineColor);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStrokeWidth(20);
        paint2.setStrokeCap(Paint.Cap.ROUND);

        paintSmall = new Paint();
        paintSmall.setColor(smallShineColor);
        paintSmall.setStrokeWidth(10);
        paintSmall.setStyle(Paint.Style.STROKE);
        paintSmall.setStrokeCap(Paint.Cap.ROUND);

        clickAnimator = ValueAnimator.ofFloat(0f, 1.1f);
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY);
        clickAnimator.setDuration(clickAnimDuration);
        clickAnimator.setInterpolator(new EaseInterpolator(Ease.QUART_OUT));

        clickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                clickValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        clickAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                clickValue = 0;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });

        shineAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                shineButton.removeView(ShineView.this);
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });
    }

    public ShineView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ShineView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void showAnimation(ShineButton shineButton)
    {
        btnWidth = shineButton.getWidth();
        btnHeight = shineButton.getHeight();
        thirdLength = getThirdLength(btnHeight, btnWidth);
        int[] location = new int[2];

        shineButton.getLocationInWindow(location);
        Rect visibleFrame = new Rect();

        if (isWindowsNotLimit(shineButton.activity))
        {
            shineButton.activity.getWindow().getDecorView().getLocalVisibleRect(visibleFrame);
        }
        else
        {
            shineButton.activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
        }

        centerAnimX = location[0] + btnWidth / 2 - visibleFrame.left; // If navigation bar is not displayed on left, visibleFrame.left is 0.

        if (isTranslucentNavigation(shineButton.activity))
        {
            if (isFullScreen(shineButton.activity))
            {
                centerAnimY = visibleFrame.height() - shineButton.getBottomHeight(false) + btnHeight / 2;
            }
            else
            {
                centerAnimY = visibleFrame.height() - shineButton.getBottomHeight(true) + btnHeight / 2;
            }
        }
        else
        {
            centerAnimY = getMeasuredHeight() - shineButton.getBottomHeight(false) + btnHeight / 2;
        }

        shineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                value = (float) valueAnimator.getAnimatedValue();

                if (shineSize != 0 && shineSize > 0)
                {
                    paint.setStrokeWidth((shineSize) * (shineDistanceMultiple - value));
                    paintSmall.setStrokeWidth(((float) shineSize / 3 * 2) * (shineDistanceMultiple - value));
                }
                else
                {
                    paint.setStrokeWidth((btnWidth / 2) * (shineDistanceMultiple - value));
                    paintSmall.setStrokeWidth((btnWidth / 3) * (shineDistanceMultiple - value));
                }

                rectF.set(centerAnimX - (btnWidth / (3 - shineDistanceMultiple) * value),
                        centerAnimY - (btnHeight / (3 - shineDistanceMultiple) * value),
                        centerAnimX + (btnWidth / (3 - shineDistanceMultiple) * value),
                        centerAnimY + (btnHeight / (3 - shineDistanceMultiple) * value));

                rectFSmall.set(centerAnimX - (btnWidth / ((3 - shineDistanceMultiple) + distanceOffset) * value),
                        centerAnimY - (btnHeight / ((3 - shineDistanceMultiple) + distanceOffset) * value),
                        centerAnimX + (btnWidth / ((3 - shineDistanceMultiple) + distanceOffset) * value),
                        centerAnimY + (btnHeight / ((3 - shineDistanceMultiple) + distanceOffset) * value));

                invalidate();
            }
        });

        shineAnimator.startAnim(this, centerAnimX, centerAnimY);
        clickAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for (int i = 0; i < shineCount; i++)
        {
            if (allowRandomColor)
            {
                paint.setColor(colorRandom[Math.abs(colorCount / 2 - i) >= colorCount ? colorCount - 1 : Math.abs(colorCount / 2 - i)]);
            }

            canvas.drawArc(rectF,
                    360f / shineCount * i + 1 + ((value - 1) * turnAngle),
                    0.1f,
                    false,
                    getConfigPaint(paint));
        }

        for (int i = 0; i < shineCount; i++)
        {
            if (allowRandomColor)
            {
                paint.setColor(colorRandom[Math.abs(colorCount / 2 - i) >= colorCount ? colorCount - 1 : Math.abs(colorCount / 2 - i)]);
            }

            canvas.drawArc(rectFSmall,
                    360f / shineCount * i + 1 - smallOffsetAngle + ((value - 1) * turnAngle),
                    0.1f,
                    false,
                    getConfigPaint(paintSmall));

        }

        paint.setStrokeWidth(btnWidth * (clickValue) * (shineDistanceMultiple - distanceOffset));

        if (clickValue != 0)
        {
            paint2.setStrokeWidth(btnWidth * (clickValue) * (shineDistanceMultiple - distanceOffset) - 8);
        }
        else
        {
            paint2.setStrokeWidth(0);
        }

        canvas.drawPoint(centerAnimX, centerAnimY, paint);
        canvas.drawPoint(centerAnimX, centerAnimY, paint2);

        if (shineAnimator != null && !isRun)
        {
            isRun = true;
            showAnimation(shineButton);
        }
    }

    private Paint getConfigPaint(Paint paint)
    {
        if (enableFlashing)
        {
            paint.setColor(colorRandom[random.nextInt(colorCount - 1)]);
        }

        return paint;
    }

    private double getThirdLength(int btnHeight, int btnWidth)
    {
        int all = btnHeight * btnHeight + btnWidth * btnWidth;
        return Math.sqrt(all);
    }

    public static class ShineParams
    {
        ShineParams()
        {
            colorRandom[0] = Color.parseColor("#FFFF99");
            colorRandom[1] = Color.parseColor("#FFCCCC");
            colorRandom[2] = Color.parseColor("#996699");
            colorRandom[3] = Color.parseColor("#FF6666");
            colorRandom[4] = Color.parseColor("#FFFF66");
            colorRandom[5] = Color.parseColor("#F44336");
            colorRandom[6] = Color.parseColor("#666666");
            colorRandom[7] = Color.parseColor("#CCCC00");
            colorRandom[8] = Color.parseColor("#666666");
            colorRandom[9] = Color.parseColor("#999933");
        }

        public boolean allowRandomColor = false;
        public long animDuration = 1500;
        public int bigShineColor = 0;
        public long clickAnimDuration = 200;
        public boolean enableFlashing = false;
        public int shineCount = 7;
        public float shineTurnAngle = 20;
        public float shineDistanceMultiple = 1.5f;
        public float smallShineOffsetAngle = 20;
        public int smallShineColor = 0;
        public int shineSize = 0;
    }

    private void initShineParams(ShineParams shineParams, ShineButton shineButton)
    {
        shineCount = shineParams.shineCount;
        turnAngle = shineParams.shineTurnAngle;
        smallOffsetAngle = shineParams.smallShineOffsetAngle;
        enableFlashing = shineParams.enableFlashing;
        allowRandomColor = shineParams.allowRandomColor;
        shineDistanceMultiple = shineParams.shineDistanceMultiple;
        animDuration = shineParams.animDuration;
        clickAnimDuration = shineParams.clickAnimDuration;
        smallShineColor = shineParams.smallShineColor;
        bigShineColor = shineParams.bigShineColor;
        shineSize = shineParams.shineSize;

        if (smallShineColor == 0)
        {
            smallShineColor = colorRandom[6];
        }

        if (bigShineColor == 0)
        {
            bigShineColor = shineButton.getColor();
        }
    }

    /**
     * @param activity
     * @return isFullScreen
     */
    public static boolean isFullScreen(Activity activity)
    {
        int flag = activity.getWindow().getAttributes().flags;
        return ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN)
        {
            return true;
        }
        else
        {
            return false;
        }*/
    }

    /**
     * @param activity
     * @return isTranslucentNavigation
     */
    public static boolean isTranslucentNavigation(Activity activity)
    {
        int flag = activity.getWindow().getAttributes().flags;
        return ((flag & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        /*if ((flag & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        {
            return true;
        }
        else
        {
            return false;
        }*/
    }

    private boolean isWindowsNotLimit(Activity activity)
    {
        int flag = activity.getWindow().getAttributes().flags;
        return ((flag & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                == WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        /*if ((flag & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                == WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        {
            return true;
        }
        else
        {
            return false;
        }*/
    }
}
