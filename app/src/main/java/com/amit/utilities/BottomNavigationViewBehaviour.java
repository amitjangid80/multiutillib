package com.amit.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

/**
 * Created by AMIT JANGID on 26-Nov-18.
**/
@SuppressWarnings({"unused", "unchecked"})
public class BottomNavigationViewBehaviour extends CoordinatorLayout.Behavior
{
    public BottomNavigationViewBehaviour()
    {

    }

    public BottomNavigationViewBehaviour(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type)
    {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type)
    {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        child.setTranslationY(Math.max(0f, Math.min(Float.parseFloat(String.valueOf(child.getHeight())), child.getTranslationY() + dyConsumed)));
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency)
    {
        if (dependency instanceof Snackbar.SnackbarLayout)
        {
            updateSnackBar(child, dependency);
        }

        return super.layoutDependsOn(parent, child, dependency);
    }

    private void updateSnackBar(View child, View dependency)
    {
        if (dependency.getLayoutParams() instanceof CoordinatorLayout.LayoutParams)
        {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.TOP;
            dependency.setLayoutParams(params);
        }
    }
}
