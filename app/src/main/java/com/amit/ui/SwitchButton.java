package com.amit.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.amit.R;

public class SwitchButton extends View
{
    private static final String TAG = SwitchButton.class.getSimpleName();

    private String[] mTabTexts = {"Male", "Female"};
    private int mNumOfTabs = mTabTexts.length;

    private static final float TEXT_SIZE = 50; // changed text size to 30 from 14
    private static final float STROKE_WIDTH = 2;
    private static final float STROKE_RADIUS = 0;

    private static final int SELECTED_TAB = 0;
    private static final String FONTS_DIR = "fonts/";
    private static final int SELECTED_COLOR = 0xffeb7b00;

    private Paint mFillPaint;
    private Paint mStrokePaint;

    private int mWidth;
    private int mHeight;

    private TextPaint mSelectedTextPaint;
    private TextPaint mUnSelectedTextPaint;
    private OnSwitchListener onSwitchListener;

    private float perWidth;
    private float mTextSize;
    private float mStrokeWidth;
    private float mStrokeRadius;
    private float mTextHeightOffSet;

    private int mSelectedTab;
    private int mSelectedColor;

    private String mTypeFace;
    private Typeface typeface;
    private Paint.FontMetrics mFontMetrics;

    public SwitchButton(Context context)
    {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    /**
     * 2018 May 03 - Thursday - 05:39 PM
     * init attrs method
     *
     * @param context - context of the application
     * @param attrs - attribute set, set by the user
     *
     * this method gets all the attributes defined and used by the user
    **/
    private void initAttrs(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        mTypeFace = a.getString(R.styleable.SwitchButton_typeface);
        mTextSize = a.getDimension(R.styleable.SwitchButton_textSize, TEXT_SIZE);
        mSelectedTab = a.getInteger(R.styleable.SwitchButton_selectedTab, SELECTED_TAB);
        mStrokeWidth = a.getDimension(R.styleable.SwitchButton_strokeWidth, STROKE_WIDTH);
        mSelectedColor = a.getColor(R.styleable.SwitchButton_selectedColor, SELECTED_COLOR);
        mStrokeRadius = a.getDimension(R.styleable.SwitchButton_strokeRadius, STROKE_RADIUS);

        int mSwitchTabsResId = a.getResourceId(R.styleable.SwitchButton_switchTabs, 0);

        if (mSwitchTabsResId != 0)
        {
            mTabTexts = getResources().getStringArray(mSwitchTabsResId);
            mNumOfTabs = mTabTexts.length;
        }

        if (!TextUtils.isEmpty(mTypeFace))
        {
            typeface = Typeface.createFromAsset(context.getAssets(), FONTS_DIR + mTypeFace);
        }

        a.recycle();
    }

    private void initPaint()
    {
        mStrokePaint = new Paint();
        mStrokePaint.setColor(mSelectedColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setAntiAlias(true);

        mFillPaint = new Paint();
        mFillPaint.setColor(mSelectedColor);
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mStrokePaint.setAntiAlias(true);

        mSelectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSelectedTextPaint.setTextSize(mTextSize);
        mSelectedTextPaint.setColor(0xffffffff);
        mStrokePaint.setAntiAlias(true);

        mUnSelectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnSelectedTextPaint.setTextSize(mTextSize);
        mUnSelectedTextPaint.setColor(mSelectedColor);
        mStrokePaint.setAntiAlias(true);

        mTextHeightOffSet = -(mSelectedTextPaint.ascent() + mSelectedTextPaint.descent()) * 0.5f;
        mFontMetrics = mSelectedTextPaint.getFontMetrics();

        if (typeface != null)
        {
            mSelectedTextPaint.setTypeface(typeface);
            mUnSelectedTextPaint.setTypeface(typeface);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int defaultWidth = getDefaultWidth();
        int defaultHeight = getDefaultHeight();

        setMeasuredDimension(
                getExpectedSize(defaultWidth, widthMeasureSpec),
                getExpectedSize(defaultHeight, heightMeasureSpec));
    }

    /**
     * get default height when android:layout_height="wrap_content"
     */
    private int getDefaultHeight()
    {
        return (int) (mFontMetrics.bottom - mFontMetrics.top) + getPaddingTop() + getPaddingBottom();
    }

    /**
     * get default width when android:layout_width="wrap_content"
     */
    private int getDefaultWidth()
    {
        float tabTextWidth = 0f;
        int tabs = mTabTexts.length;

        for (int i = 0; i < tabs; i++)
        {
            tabTextWidth = Math.max(tabTextWidth, mSelectedTextPaint.measureText(mTabTexts[i]));
        }

        float totalTextWidth = tabTextWidth * tabs;
        float totalStrokeWidth = (mStrokeWidth * tabs);
        int totalPadding = (getPaddingRight() + getPaddingLeft()) * tabs;
        return (int) (totalTextWidth + totalStrokeWidth + totalPadding);
    }

    /**
     * get expected size method
     *
     * @param size - size of the text
     *
     * @param measureSpec - measure spec
     *
     * @return - returns integer
    **/
    private int getExpectedSize(int size, int measureSpec)
    {
        int result = size;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode)
        {
            case MeasureSpec.EXACTLY:

                result = specSize;

                break;

            case MeasureSpec.UNSPECIFIED:

                result = size;

                break;

            case MeasureSpec.AT_MOST:

                result = Math.min(size, specSize);

                break;

            default:
                break;
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float left = mStrokeWidth * 0.5f;
        float top = mStrokeWidth * 0.5f;

        float right = mWidth - mStrokeWidth * 0.5f;
        float bottom = mHeight - mStrokeWidth * 0.5f;

        //draw rounded rectangle
        canvas.drawRoundRect(new RectF(left, top, right, bottom), mStrokeRadius, mStrokeRadius, mStrokePaint);

        //draw line
        for (int i = 0; i < mNumOfTabs - 1; i++)
        {
            canvas.drawLine(perWidth * (i + 1), top, perWidth * (i + 1), bottom, mStrokePaint);
        }

        //draw tab and line
        for (int i = 0; i < mNumOfTabs; i++)
        {
            String tabText = mTabTexts[i];
            float tabTextWidth = mSelectedTextPaint.measureText(tabText);

            if (i == mSelectedTab)
            {
                //draw selected tab
                if (i == 0)
                {
                    drawLeftPath(canvas, left, top, bottom);
                }
                else if (i == mNumOfTabs - 1)
                {
                    drawRightPath(canvas, top, right, bottom);
                }
                else
                {
                    canvas.drawRect(new RectF(perWidth * i, top, perWidth * (i + 1), bottom), mFillPaint);
                }

                // draw selected text
                canvas.drawText(tabText,
                        0.5f * perWidth * (2 * i + 1) - 0.5f * tabTextWidth,
                        mHeight * 0.5f + mTextHeightOffSet, mSelectedTextPaint);
            }
            else
            {
                //draw unselected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1) - 0.5f * tabTextWidth, mHeight * 0.5f +
                        mTextHeightOffSet, mUnSelectedTextPaint);
            }
        }
    }

    /**
     * draw left path method
     *
     * @param canvas - canvas
     *
     * @param left - left
     *
     * @param top - top
     *
     * @param bottom - bottom
    **/
    private void drawLeftPath(Canvas canvas, float left, float top, float bottom)
    {
        Path leftPath = new Path();
        leftPath.moveTo(left + mStrokeRadius, top);
        leftPath.lineTo(perWidth, top);
        leftPath.lineTo(perWidth, bottom);
        leftPath.lineTo(left + mStrokeRadius, bottom);

        leftPath.arcTo(
                new RectF(left, bottom - 2 * mStrokeRadius, left + 2 * mStrokeRadius, bottom),
                90, 90);

        leftPath.lineTo(left, top + mStrokeRadius);

        leftPath.arcTo(
                new RectF(left, top, left + 2 * mStrokeRadius, top + 2 * mStrokeRadius),
                180, 90);

        canvas.drawPath(leftPath, mFillPaint);
    }

    /**
     * draw right path
     *
     * @param canvas - canvas
     *
     * @param top - top
     *
     * @param right - right
     *
     * @param bottom - bottom
    **/
    private void drawRightPath(Canvas canvas, float top, float right, float bottom)
    {
        Path rightPath = new Path();
        rightPath.moveTo(right - mStrokeRadius, top);
        rightPath.lineTo(right - perWidth, top);
        rightPath.lineTo(right - perWidth, bottom);
        rightPath.lineTo(right - mStrokeRadius, bottom);

        rightPath.arcTo(
                new RectF(right - 2 * mStrokeRadius, bottom - 2 * mStrokeRadius, right, bottom),
                90, -90);

        rightPath.lineTo(right, top + mStrokeRadius);

        rightPath.arcTo(
                new RectF(right - 2 * mStrokeRadius, top, right, top + 2 * mStrokeRadius),
                0, -90);

        canvas.drawPath(rightPath, mFillPaint);
    }

    /**
     * called after onMeasure
     *
     * @param w - widht
     *
     * @param h - height
     *
     * @param oldw - old width
     *
     * @param oldh - old height
    **/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        perWidth = mWidth / mNumOfTabs;

        checkAttrs();
    }

    /**
     * check attribute where suitable
     */
    private void checkAttrs()
    {
        if (mStrokeRadius > 0.5f * mHeight)
        {
            mStrokeRadius = 0.5f * mHeight;
        }
    }

    /**
     * receive the event when touched
     *
     * @param event - event
     * @return true or false
    **/
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            float x = event.getX();

            for (int i = 0; i < mNumOfTabs; i++)
            {
                if (x > perWidth * i && x < perWidth * (i + 1))
                {
                    if (mSelectedTab == i)
                    {
                        return true;
                    }

                    mSelectedTab = i;

                    if (onSwitchListener != null)
                    {
                        onSwitchListener.onSwitch(i, mTabTexts[i]);
                    }
                }
            }

            invalidate();
        }

        return true;
    }

    /**
     * called when switched
     */
    public interface OnSwitchListener
    {
        void onSwitch(int position, String tabText);
    }

    public SwitchButton setOnSwitchListener(@NonNull OnSwitchListener onSwitchListener)
    {
        this.onSwitchListener = onSwitchListener;
        return this;
    }

    /**
     * get position of selected tab
     */
    public int getSelectedTab()
    {
        return mSelectedTab;
    }

    /**
     * set selected tab
     *
     * @param mSelectedTab - selected tab
     * @return - button
     */
    public SwitchButton setSelectedTab(int mSelectedTab)
    {
        this.mSelectedTab = mSelectedTab;
        invalidate();

        if (onSwitchListener != null)
        {
            onSwitchListener.onSwitch(mSelectedTab, mTabTexts[mSelectedTab]);
        }

        return this;
    }

    public void clearSelection()
    {
        this.mSelectedTab = -1;
        invalidate();
    }

    /**
     * set data for the switch button
     *
     * @param tagTexts -  tag texts
     * @return - button
     */
    public SwitchButton setText(String... tagTexts)
    {
        if (tagTexts.length > 1)
        {
            this.mTabTexts = tagTexts;
            mNumOfTabs = tagTexts.length;
            requestLayout();
            return this;
        }
        else
        {
            throw new IllegalArgumentException("the size of tagTexts should greater then 1");
        }
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable("View", super.onSaveInstanceState());
        bundle.putFloat("StrokeRadius", mStrokeRadius);
        bundle.putFloat("StrokeWidth", mStrokeWidth);
        bundle.putFloat("TextSize", mTextSize);
        bundle.putInt("SelectedColor", mSelectedColor);
        bundle.putInt("SelectedTab", mSelectedTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            mStrokeRadius = bundle.getFloat("StrokeRadius");
            mStrokeWidth = bundle.getFloat("StrokeWidth");
            mTextSize = bundle.getFloat("TextSize");
            mSelectedColor = bundle.getInt("SelectedColor");
            mSelectedTab = bundle.getInt("SelectedTab");
            super.onRestoreInstanceState(bundle.getParcelable("View"));
        }
        else
        {
            super.onRestoreInstanceState(state);
        }
    }
}
