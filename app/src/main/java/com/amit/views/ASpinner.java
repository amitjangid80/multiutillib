package com.amit.views;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.util.AttributeSet;

import com.amit.R;

/**
 * Created by AMIT JANGID on 25/02/2019.
**/
@SuppressWarnings("unchecked")
public class ASpinner extends AppCompatEditText
{
    private int mExpandTint;
    private String[] mItems;
    private int mSelected = -1;
    private String mTitle = "Select";

    @ColorInt
    private int titleColor;

    private Drawable mExpandDrawable;

    private OnItemClickListener mOnItemClickListener;

    public ASpinner(Context context)
    {
        super(context);
        init(null);
    }

    public ASpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public ASpinner(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        setFocusable(false);
        setSingleLine(true);
        setLongClickable(false);

        mExpandTint = ContextCompat.getColor(getContext(), R.color.translucent_90);

        if (attrs != null)
        {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ASpinner);

            if (typedArray != null)
            {
                mTitle = typedArray.getString(R.styleable.ASpinner_spinner_dialog_title) == null ?
                        "Select" :
                        typedArray.getString(R.styleable.ASpinner_spinner_dialog_title);

                mExpandTint = typedArray.getColor(R.styleable.ASpinner_spinner_dialog_icon_tint, mExpandTint);
                titleColor = typedArray.getColor(R.styleable.ASpinner_spinner_dialog_title_color, getResources().getColor(R.color.black));

                typedArray.recycle();
            }
        }

        mExpandDrawable = ContextCompat.getDrawable(getContext(), R.drawable.spinner_expand_icon);
        mExpandDrawable.setColorFilter(new PorterDuffColorFilter(mExpandTint, PorterDuff.Mode.SRC_IN));

        // calling set icon method
        setIcon();
    }

    private void setIcon()
    {
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], mExpandDrawable, drawables[3]);
    }

    public void setExpandTint(int expandTint)
    {
        this.mExpandTint = expandTint;
        postInvalidate();
    }

    public void setItems(String[] items)
    {
        this.mItems = items;
        postInvalidate();
    }

    public void setTitle(String title)
    {
        this.mTitle = title;
        postInvalidate();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void postInvalidate()
    {
        super.postInvalidate();
        mExpandDrawable.setColorFilter(new PorterDuffColorFilter(mExpandTint, PorterDuff.Mode.SRC_IN));

        // calling set icon method
        setIcon();
    }

    protected void setSelected(int selected)
    {
        this.mSelected = selected;
    }

    @Override
    public boolean performClick()
    {
        ASpinnerDialog dialog = ASpinnerDialog.newInstance(mTitle, mItems, mSelected, titleColor);
        dialog.setListener(mOnItemClickListener, ASpinner.this);
        dialog.show(findActivity(getContext()).getSupportFragmentManager(), dialog.getTag());
        return super.performClick();
    }

    public void clear()
    {
        setText("");
        mSelected = -1;
    }

    public static <T extends FragmentActivity> T findActivity(Context context)
    {
        if (context == null)
        {
            throw new IllegalArgumentException("Context cannot be null.");
        }

        if (context instanceof FragmentActivity)
        {
            return (T) context;
        }
        else
        {
            ContextWrapper contextWrapper = (ContextWrapper) context;
            Context baseContext = contextWrapper.getBaseContext();

            if (baseContext == null)
            {
                throw new IllegalStateException("Activity was not found as base context view!");
            }

            return findActivity(baseContext);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(String selectedItem, int position);
    }
}
