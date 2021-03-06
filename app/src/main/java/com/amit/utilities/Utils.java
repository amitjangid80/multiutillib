package com.amit.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.AttrRes;
import androidx.annotation.CheckResult;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * https://github.com/jaydeepw/android-utils/tree/master/Utils
**/
@SuppressWarnings({"unused", "WeakerAccess"})
public class Utils
{
    private static final String TAG = Utils.class.getSimpleName();
    private static float xdpi = Float.MIN_VALUE;

    /**
     * is url valid
     * this method will check if the url provided is valid or not
     *
     * @param url - url to check
     * @return - will return true if the url is valid
     *           else will return false
    **/
    @CheckResult
    public static boolean isUrlValid(String url)
    {
        URL urlObj;

        try
        {
            urlObj = new URL(url);
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG, "isUrlValid: excepion while checking valid url.");
            e.printStackTrace();
            return false;
        }

        try
        {
            urlObj.toURI();
        }
        catch (URISyntaxException e)
        {
            Log.e(TAG, "isUrlValid: uri syntax exception.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * to Bold
     * this method will convert the normal text to bold
     *
     * @param sourceText - text to convert to bold
     *
     * @return - {@link android.text.SpannableString} in BOLD TypeFace
    **/
    public static SpannableStringBuilder toBold(String sourceText)
    {
        try
        {
            if (sourceText != null)
            {
                final SpannableStringBuilder sb = new SpannableStringBuilder(sourceText);

                // span to set text color to some RGB value
                final StyleSpan bss = new StyleSpan(Typeface.BOLD);

                // set text bold
                sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                return sb;
            }
            else
            {
                throw new NullPointerException("String to convert cannot be null.");
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "toBold: exception while making the text bold.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * to Bold
     * this method will convert a string or a sub string to bold
     *
     * @param string - string in which the sub string has to be converted to bold
     *                 or string to be converted to bold
     *
     * @param subString - The subString within the string to bold.
     *                    Pass null to bold entire string.
     *
     * @return - {@link android.text.SpannableString} in Bold TypeFace
    **/
    public static SpannableStringBuilder toBold(String string, String subString)
    {
        try
        {
            if (TextUtils.isEmpty(string))
            {
                return new SpannableStringBuilder("");
            }

            SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(string);
            StyleSpan bss = new StyleSpan(Typeface.BOLD);

            if (subString != null)
            {
                int subStringNameStart = string.toLowerCase().indexOf(subString);

                if (subStringNameStart > -1)
                {
                    spannableBuilder.setSpan(
                            bss, subStringNameStart,
                            subStringNameStart + subString.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            else
            {
                // set the entire text to bold
                spannableBuilder.setSpan(
                        bss, 0,
                        spannableBuilder.length(),
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            return spannableBuilder;
        }
        catch (Exception e)
        {
            Log.e(TAG, "toBold: exception while converting string or sub string to bold.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * hide keyboard
     * this method will hide the keyboard
     *
     * @param context - context of the application
    **/
    public static void hideKeyboard(Context context)
    {
        try
        {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager != null)
            {
                inputManager.hideSoftInputFromWindow(
                        ((Activity) context).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "hideKeyboard: exception while hiding keyboard.");
            e.printStackTrace();
        }
    }

    /**
     * get sha 512 hash
     * this method will convert a string to byte array.
     *
     * @param stringToHash - string to convert to hash.
     *
     * @return string converted to hash value.
    **/
    public static String getSha512Hash(String stringToHash)
    {
        try
        {
            if (stringToHash == null)
            {
                return "";
            }

            return getSha512Hash(stringToHash.getBytes());
        }
        catch (Exception e)
        {
            Log.e(TAG, "getSha512Hash: exception while getting sha 512 hash.");
            e.printStackTrace();
            return "";
        }
    }

    /**
     * get sha 512 hash
     * this method will convert the byte array to string
     * which is converted to hash
     *
     * @param dataToHash - byte array to convert to hash value
     *
     * @return string converted into hash value.
    **/
    public static String getSha512Hash(byte[] dataToHash)
    {
        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e(TAG, "getSha512Hash: exception while getting sha 512 hash.");
            e.printStackTrace();
        }

        if (md != null)
        {
            md.update(dataToHash);
            byte[] byteData = md.digest();
            return Base64.encodeToString(byteData, Base64.DEFAULT);
        }

        return "";
    }

    /**
     * 2018 May 14 - Monday - 05:50 PM
     * get drawable
     * this method will get you the drawables
     *
     * @param context - context of the application
     * @param id - drawable id
     *
     * @return returns drawable
    **/
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id)
    {
        return ContextCompat.getDrawable(context, id);
    }

    /**
     * get Color wrapper
     * this method will get the color resource based on android version
     *
     * @param context - context of the application
     * @param id - id of the color resource
     *
     * @return int - color in integer.
    **/
    public static int getColorWrapper(@NonNull Context context, @ColorRes int id)
    {
        return ContextCompat.getColor(context, id);
    }

    /**
     * dp to px
     * this method will convert dp to pixles
     *
     * @param context - context of the application
     * @param dp - dp to convert into pixels
     *
     * @return - pixels in integer form
    **/
    public static int dpToPx(Context context, int dp)
    {
        if (xdpi == Float.MIN_VALUE)
        {
            xdpi = context.getResources().getDisplayMetrics().xdpi;
        }

        return Math.round(dp * (xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Nullable
    public static Drawable getThemeAttrDrawable(@NonNull Context context, @AttrRes int attributeDrawable)
    {
        int[] attrs = new int[]{attributeDrawable};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();
        return drawableFromTheme;
    }

    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attributeColor)
    {
        int[] attrs = new int[]{attributeColor};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int color = ta.getColor(0, Color.TRANSPARENT);
        ta.recycle();
        return color;
    }

    /**
     * convert dp to pixel
     * this method will convert dp to pixels
     *
     * @param context - context of the application
     * @param dp - dp to convert into pixels
     *
     * @return - pixels in integer form
    **/
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * convert pixels to dp
     * this method will converts pixels to dp
     *
     * @param context - context of the application
     * @param px - pixels to be convert into dp
     *
     * @return - dp in float form
    **/
    public static float convertPixelsToDp(float px, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * drawable to bitmap
     * this method will convert a drawable to bitmap
     *
     * @param drawable - drawable to be converted into bitmap
     * @return bitmap
    **/
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * dp 2 px
     * this method will convert dp to pixels
     *
     * @param context - context of the application
     * @param dpValue - dpValue to be convert into pixels
     *
     * @return - pixels in integer form
    **/
    public static int dp2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * convert pixels to dp
     * this method will converts pixels to dp
     *
     * @param context - context of the application
     * @param pxValue - pxValue to be convert into dp
     *
     * @return - dp in float form
    **/
    public static int px2dp(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 2018 June 23 - Saturday - 10:30 AM
     * right padding method
     *
     * this method will append empty or blank or spaces
     * after the string for specified length.
     *
     * @param strText - String text to append spaces to the right
     * @param length - length of the string text including spaces and text.
     *
     * @return - returns the string with spaces appended to the right of the string
    **/
    public static String rightPadding(String strText, int length)
    {
        return String.format("%-" + length + "." + length + "s", strText);
    }

    /**
     * 2018 June 23 - Saturday - 10:30 AM
     * left padding method
     *
     * this method will append empty or blank or spaces
     * after the string for specified length.
     *
     * @param strText - String text to append spaces to the left
     * @param length - length of the string text including spaces and text.
     *
     * @return - returns the string with spaces appended to the left of the string.
    **/
    public static String leftPadding(String strText, int length)
    {
        return String.format("%" + length + "." + length + "s", strText);
    }

    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     *
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     *     lives inside a Fragment hosted by the called Activity).
     *
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     *     asynchronously load/scale a bitmap before the transition can begin).
     *
     * @param activity - Activity on which shared transition is to be performed
     *
     * @param sharedElement - The view on which the actual animation will be done
    **/
    public static void scheduleStartPostponedTransition(final Activity activity, final View sharedElement)
    {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    activity.startPostponedEnterTransition();
                }

                return true;
            }
        });
    }

    /**
     * make text view resizable method
     *
     * this method will make the text view resizeable
     * when you have a large description and don't want to show entire
     * details completely then you can use this to show a link like text "See More"
     *
     * @param tv - text view to make it resizeable
     *
     * @param maxLine - number of lines to be shown before see more button
     *
     * @param expandText - text you want to show for the user to click
     *                     Example: Read more (THIS IS USED BY DEFAULT)
     *
     * @param viewMore - pass true for this parameter
    **/
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore)
    {
        try
        {
            if (tv.getTag() == null)
            {
                tv.setTag(tv.getText());
            }

            ViewTreeObserver treeObserver = tv.getViewTreeObserver();

            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    ViewTreeObserver observer = tv.getViewTreeObserver();
                    observer.removeOnGlobalLayoutListener(this);

                    if (maxLine == 0)
                    {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;

                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());

                        tv.setText(addClickablePartTextViewResizable(
                                Html.fromHtml(tv.getText().toString()), tv, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
                    }
                    else if (maxLine > 0 && tv.getLineCount() >= maxLine)
                    {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;

                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());

                        tv.setText(addClickablePartTextViewResizable(
                                Html.fromHtml(tv.getText().toString()), tv, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
                    }
                    else
                    {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;

                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());

                        tv.setText(addClickablePartTextViewResizable(
                                Html.fromHtml(tv.getText().toString()), tv, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e(TAG, "makeTextViewResizable: exception while making text view resizable:\n");
            e.printStackTrace();
        }
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned spanned,
                                                                            final TextView tv,
                                                                            final String spannableText,
                                                                            final boolean viewMore)
    {
        String text = spanned.toString();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);

        if (text.contains(spannableText))
        {
            spannableStringBuilder.setSpan(new MySpannable(false)
                                           {
                                               @Override
                                               public void onClick(@NonNull View widget)
                                               {
                                                   if (viewMore)
                                                   {
                                                       tv.setLayoutParams(tv.getLayoutParams());
                                                       tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                                                       tv.invalidate();
                                                       makeTextViewResizable(tv, -1, "... Read Less", false);
                                                   }
                                                   else
                                                   {
                                                       tv.setLayoutParams(tv.getLayoutParams());
                                                       tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                                                       tv.invalidate();
                                                       makeTextViewResizable(tv, 4, "... Read More", true);
                                                   }
                                               }
                                           },
                    text.indexOf(spannableText), text.indexOf(spannableText) + spannableText.length(), 0);
        }

        return spannableStringBuilder;
    }
}
