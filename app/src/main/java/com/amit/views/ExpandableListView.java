package com.amit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by AMIT JANGID on 02-Oct-18.
 *
 * this class is used for showing data in list view inside a scroll view
**/
@SuppressWarnings("unused")
public class ExpandableListView extends ListView
{
    private boolean isListViewExpandable = false;

    public ExpandableListView(Context context)
    {
        super(context);
    }

    public ExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExpandableListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (isListViewExpandable)
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

    public void setIsListViewExpandable(boolean isListViewExpandable)
    {
        this.isListViewExpandable = isListViewExpandable;
    }
}
