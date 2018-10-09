package com.amit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by AMIT JANGID on 20-Sep-18.
 *
 * this grid view class can be used instead of normal grid view inside a scroll view
**/
@SuppressWarnings("unused")
public class ExpandableGridView extends GridView
{
    private boolean isGridViewExpandable = false;

    public ExpandableGridView(Context context)
    {
        super(context);
    }

    public ExpandableGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExpandableGridView(Context context, AttributeSet attrs, int defStyles)
    {
        super(context, attrs, defStyles);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (isGridViewExpandable)
        {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setIsGridViewExpandable(boolean isGridViewExpandable)
    {
        this.isGridViewExpandable = isGridViewExpandable;
    }
}
