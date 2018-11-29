package com.amit.utilities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by AMIT JANGID on 20-Oct-18.
**/
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class RecyclerViewScroll extends RecyclerView.OnScrollListener
{
    private static final float MINIMUM = 25;
    private boolean isVisible = true;
    private int scrollDist = 0;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);

        if (isVisible && scrollDist > MINIMUM)
        {
            // calling hide fab button method
            hideFabButton();

            scrollDist = 0;
            isVisible = false;
        }
        else if (!isVisible && scrollDist < -MINIMUM)
        {
            // calling show fab button method
            showFabButton();

            scrollDist = 0;
            isVisible = true;
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0))
        {
            scrollDist += dy;
        }
    }

    protected abstract void showFabButton();
    protected abstract void hideFabButton();
}
