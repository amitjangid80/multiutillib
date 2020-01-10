package com.amit.utilities;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by AMIT JANGID on 26-Nov-18.
**/
public class MySpannable extends ClickableSpan
{
    private final boolean isUnderLine;

    MySpannable(boolean isUnderLine)
    {
        this.isUnderLine = isUnderLine;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds)
    {
        ds.setUnderlineText(isUnderLine);
        ds.setColor(Color.parseColor("#1b76d3"));
    }

    @Override
    public void onClick(@NonNull View widget)
    {

    }
}
