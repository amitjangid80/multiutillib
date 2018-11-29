package com.amit.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.amit.R;

/**
 * Created by AMIT JANGID on 22-Oct-18.
**/
@SuppressWarnings("unused")
public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton>
{
    private final int toolbarHeight;

    public ScrollingFABBehavior(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        this.toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton fab, @NonNull View dependency)
    {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton fab, @NonNull View dependency)
    {
        if (dependency instanceof AppBarLayout)
        {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();

            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;

            float ratio = dependency.getY() / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }

        return true;
    }
}
