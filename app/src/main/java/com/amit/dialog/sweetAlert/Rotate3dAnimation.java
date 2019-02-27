package com.amit.dialog.sweetAlert;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.amit.R;

/**
 * Created by AMIT JANGID on 27/02/2019.
**/
public class Rotate3dAnimation extends Animation
{
    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;

    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;

    private int mRollType;
    private float mPivotX;
    private float mPivotY;
    private float mToDegrees;
    private float mFromDegrees;

    private Camera mCamera;

    public static final int ROLL_BY_X = 0;
    public static final int ROLL_BY_Y = 1;
    public static final int ROLL_BY_Z = 2;

    protected static class Description
    {
        public int type;
        public float value;
    }

    private Description parseValue(TypedValue value)
    {
        Description d = new Description();

        if (value == null)
        {
            d.type = ABSOLUTE;
            d.value = 0;
        }
        else
        {
            if (value.type == TypedValue.TYPE_FRACTION)
            {
                d.type = (value.data & TypedValue.COMPLEX_UNIT_MASK) ==
                        TypedValue.COMPLEX_UNIT_FRACTION_PARENT ?
                        RELATIVE_TO_PARENT : RELATIVE_TO_SELF;

                d.value = TypedValue.complexToFloat(value.data);
                return d;
            }
            else if (value.type == TypedValue.TYPE_FLOAT)
            {
                d.type = ABSOLUTE;
                d.value = value.getFloat();
                return d;
            }
            else if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT)
            {
                d.type = ABSOLUTE;
                d.value = value.data;
                return d;
            }
        }

        d.type = ABSOLUTE;
        d.value = 0.0f;

        return d;
    }

    public Rotate3dAnimation(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Rotate3dAnimation);

        mRollType = a.getInt(R.styleable.Rotate3dAnimation_rollType, ROLL_BY_X);
        mToDegrees = a.getFloat(R.styleable.Rotate3dAnimation_toDeg, 0.0f);
        mFromDegrees = a.getFloat(R.styleable.Rotate3dAnimation_fromDeg, 0.0f);

        Description d = parseValue(a.peekValue(R.styleable.Rotate3dAnimation_pivotX));

        mPivotXType = d.type;
        mPivotXValue = d.value;

        a.recycle();

        initializePivotPoint();
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees)
    {
        mPivotX = 0.0f;
        mPivotY = 0.0f;

        mRollType = rollType;
        mToDegrees = toDegrees;
        mFromDegrees = fromDegrees;
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees, float pivotX, float pivotY)
    {
        mPivotXValue = pivotX;
        mPivotYValue = pivotY;

        mPivotXType = ABSOLUTE;
        mPivotYType = ABSOLUTE;

        mRollType = rollType;
        mToDegrees = toDegrees;
        mFromDegrees = fromDegrees;

        initializePivotPoint();
    }

    private void initializePivotPoint()
    {
        if (mPivotXType == ABSOLUTE)
        {
            mPivotX = mPivotXValue;
        }

        if (mPivotYType == ABSOLUTE)
        {
            mPivotY = mPivotYValue;
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight)
    {
        super.initialize(width, height, parentWidth, parentHeight);

        mCamera = new Camera();
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - mFromDegrees) * interpolatedTime);

        final Matrix matrix = t.getMatrix();
        mCamera.save();

        switch (mRollType)
        {
            case ROLL_BY_X:

                mCamera.rotateX(degrees);

                break;

            case ROLL_BY_Y:

                mCamera.rotateY(degrees);

                break;

            case ROLL_BY_Z:

                mCamera.rotateZ(degrees);

                break;
        }

        mCamera.getMatrix(matrix);
        mCamera.restore();

        matrix.preTranslate(-mPivotX, -mPivotY);
        matrix.postTranslate(mPivotX, mPivotY);
    }
}
