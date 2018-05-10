package com.amit.ui;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class UiUtils
{
    private static final String TAG = UiUtils.class.getSimpleName();

    /**
     * set char counter
     * Shows live character counter for the number of characters
     * typed in the parameter {@link android.widget.EditText}
     *
     * @param editText          Characters to count from
     * @param tvCounterView     {@link android.widget.TextView} to show live character count in
     * @param maxCharCount      Max characters that can be typed in into the parameter edit text
     * @param countDown         if true, only the remaining of the max character count will be displayed.
     *                          if false, current character count as well as max character count will be displayed in the UI.
    **/
    public static void setCharCounter(EditText editText,
                                      final TextView tvCounterView,
                                      final int maxCharCount,
                                      final boolean countDown)
    {
        try
        {
            if (editText == null)
            {
                throw new NullPointerException("EditText to count text on cannot be null.");
            }

            if (tvCounterView == null)
            {
                throw new NullPointerException("TextView on which counter should be displayed cannot be null.");
            }

            if (countDown)
            {
                tvCounterView.setText(String.valueOf(maxCharCount));
            }
            else
            {
                tvCounterView.setText(String.valueOf("0 / " + maxCharCount));
            }

            // setting max length to edit text
            setMaxLength(editText, maxCharCount);

            editText.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (countDown)
                    {
                        // show only the remaining number of characters
                        int charsLeft = maxCharCount - s.length();

                        if (charsLeft >= 0)
                        {
                            tvCounterView.setText(String.valueOf(charsLeft));
                        }
                    }
                    else
                    {
                        // show number of chars / max chars in the UI
                        String counter = s.length() + " / " + maxCharCount;
                        tvCounterView.setText(counter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
        }
        catch (Exception e)
        {
            Log.e(TAG, "setCharCounter: exception while setting char counter.");
            e.printStackTrace();
        }
    }

    /**
     * set char counter
     * Shows live character counter for the number of characters
     *
     * @param textInputEditText          Characters to count from
     * @param tvCounterView     {@link android.widget.TextView} to show live character count in
     * @param maxCharCount      Max characters that can be typed in into the parameter edit text
     * @param countDown         if true, only the remaining of the max character count will be displayed.
     *                          if false, current character count as well as max character count will be displayed in the UI.
     **/
    public static void setCharCounter(TextInputEditText textInputEditText,
                                      final TextView tvCounterView,
                                      final int maxCharCount,
                                      final boolean countDown)
    {
        try
        {
            if (textInputEditText == null)
            {
                throw new NullPointerException("EditText to count text on cannot be null.");
            }

            if (tvCounterView == null)
            {
                throw new NullPointerException("TextView on which counter should be displayed cannot be null.");
            }

            if (countDown)
            {
                tvCounterView.setText(String.valueOf(maxCharCount));
            }
            else
            {
                tvCounterView.setText(String.valueOf("0 / " + maxCharCount));
            }

            // setting max length to text input edit text
            setMaxLength(textInputEditText, maxCharCount);

            textInputEditText.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (countDown)
                    {
                        // show only the remaining number of characters
                        int charsLeft = maxCharCount - s.length();

                        if (charsLeft >= 0)
                        {
                            tvCounterView.setText(String.valueOf(charsLeft));
                        }
                    }
                    else
                    {
                        // show number of chars / max chars in the UI
                        String counter = s.length() + " / " + maxCharCount;
                        tvCounterView.setText(counter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
        }
        catch (Exception e)
        {
            Log.e(TAG, "setCharCounter: exception while setting char counter.");
            e.printStackTrace();
        }
    }

    /**
     * set max length
     * this method sets max text length for text view
     *
     * @param textView - text view on which you want to set max length
     * @param maxLength - length to set on text view
    **/
    public static void setMaxLength(TextView textView, int maxLength)
    {
        try
        {
            if (textView == null)
            {
                throw new NullPointerException("TextView cannot be null.");
            }

            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            textView.setFilters(fArray);
        }
        catch (Exception e)
        {
            Log.e(TAG, "setMaxLength: exception while setting max length to text view.");
            e.printStackTrace();
        }
    }

    /**
     * set max length
     * this method sets max text length for text view
     *
     * @param editText - text view on which you want to set max length
     * @param maxLength - length to set on text view
     **/
    public static void setMaxLength(EditText editText, int maxLength)
    {
        try
        {
            if (editText == null)
            {
                throw new NullPointerException("TextView cannot be null.");
            }

            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editText.setFilters(fArray);
        }
        catch (Exception e)
        {
            Log.e(TAG, "setMaxLength: exception while setting max length to text view.");
            e.printStackTrace();
        }
    }

    /**
     * set max length
     * this method sets max text length for text view
     *
     * @param textInputEditText - text view on which you want to set max length
     * @param maxLength - length to set on text view
     **/
    public static void setMaxLength(TextInputEditText textInputEditText, int maxLength)
    {
        try
        {
            if (textInputEditText == null)
            {
                throw new NullPointerException("TextView cannot be null.");
            }

            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            textInputEditText.setFilters(fArray);
        }
        catch (Exception e)
        {
            Log.e(TAG, "setMaxLength: exception while setting max length to text view.");
            e.printStackTrace();
        }
    }
}
