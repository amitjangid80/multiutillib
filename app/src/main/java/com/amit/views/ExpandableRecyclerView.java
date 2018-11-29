package com.amit.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by AMIT JANGID on 27-Nov-18.
**/
@SuppressWarnings("unused")
public class ExpandableRecyclerView extends RecyclerView
{
    private boolean isRecyclerViewExpandable = false;

    public ExpandableRecyclerView(@NonNull Context context)
    {
        super(context);
    }

    public ExpandableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExpandableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec)
    {
        if (isRecyclerViewExpandable)
        {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthSpec, heightSpec);
        }
    }

    public void setRecyclerViewExpandable(boolean recyclerViewExpandable)
    {
        isRecyclerViewExpandable = recyclerViewExpandable;
    }
}
