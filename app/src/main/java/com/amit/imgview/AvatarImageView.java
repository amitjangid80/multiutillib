package com.amit.imgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.amit.R;

public class AvatarImageView extends CircularImageView
{
    public static final int SHOW_IMAGE = 2;
    public static final int SHOW_INITIAL = 1;

    private static final int DEF_TEXT_SIZE = 90;
    private static final String DEF_INITIAL = "A";
    private static final int DEF_STATE = SHOW_INITIAL;
    private static final int DEF_BACKGROUND_COLOR = 0xE53935;

    private Paint mTextPaint;
    private Rect mTextBounds;

    private Paint mBackgroundPaint;
    private RectF mBackgroundBounds;

    private int mShowState;
    private String mInitial;

    public AvatarImageView(Context context)
    {
        this(context, null);
    }

    public AvatarImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        int showState = DEF_STATE;
        int textColor = Color.WHITE;
        int textSize = DEF_TEXT_SIZE;
        String initial = DEF_INITIAL;
        int backgroundColor = DEF_BACKGROUND_COLOR;

        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0);
            initial = a.getString(R.styleable.AvatarImageView_avatar_text);
            showState = a.getInt(R.styleable.AvatarImageView_avatar_state, showState);
            textColor = a.getColor(R.styleable.AvatarImageView_avatar_textColor, textColor);
            textSize = a.getDimensionPixelSize(R.styleable.AvatarImageView_avatar_textSize, textSize);
            backgroundColor = a.getColor(R.styleable.AvatarImageView_avatar_backgroundColor, backgroundColor);
            a.recycle();
        }

        mShowState = showState;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mTextBounds = new Rect();
        mInitial = extractInitial(initial);
        updateTextBounds();

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mBackgroundBounds = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        updateCircleDrawBounds(mBackgroundBounds);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mShowState == SHOW_INITIAL)
        {
            float textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY();

            canvas.drawOval(mBackgroundBounds, mBackgroundPaint);
            canvas.drawText(mInitial, mBackgroundBounds.centerX(), textBottom, mTextPaint);

            drawStroke(canvas);
            drawHighlight(canvas);
        }
        else
        {
            super.onDraw(canvas);
        }
    }

    public String getInitial()
    {
        return mInitial;
    }

    public void setInitial(String letter)
    {
        mInitial = extractInitial(letter);
        updateTextBounds();
        invalidate();
    }

    public int getState()
    {
        return mShowState;
    }

    public void setState(int state)
    {
        if (state != SHOW_INITIAL && state != SHOW_IMAGE)
        {
            String msg = "Illegal avatar state value: " + state + ", use either SHOW_INITIAL or SHOW_IMAGE constant.";
            throw new IllegalArgumentException(msg);
        }

        mShowState = state;
        invalidate();
    }

    public float getTextSize()
    {
        return mTextPaint.getTextSize();
    }

    public void setTextSize(float size)
    {
        mTextPaint.setTextSize(size);
        updateTextBounds();
        invalidate();
    }

    public int getTextColor()
    {
        return mTextPaint.getColor();
    }

    public void setTextColor(int color)
    {
        mTextPaint.setColor(color);
        invalidate();
    }

    public int getAvatarBackgroundColor()
    {
        return mBackgroundPaint.getColor();
    }

    public void setAvatarBackgroundColor(int color)
    {
        mBackgroundPaint.setColor(color);
        invalidate();
    }

    private String extractInitial(String letter)
    {
        if (letter == null || letter.trim().length() <= 0) return "?";
        return String.valueOf(letter.charAt(0));
    }

    private void updateTextBounds()
    {
        mTextPaint.getTextBounds(mInitial, 0, mInitial.length(), mTextBounds);
    }
}
