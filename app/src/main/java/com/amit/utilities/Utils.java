package com.amit.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.AttrRes;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * https://github.com/jaydeepw/android-utils/tree/master/Utils
**/
@SuppressLint("HardwareIds")
@SuppressWarnings("unused")
public class Utils
{
    private static final String TAG = Utils.class.getSimpleName();
    private static float xdpi = Float.MIN_VALUE;

    /**
     * is Sd Card Mounted
     * this method will check if sd card is mounted or not
     *
     * @return - true or false
     *           if sd card available then will return true
     *           else will return false
    **/
    @CheckResult
    public static boolean isSdCardMounted()
    {
        try
        {
            String status = Environment.getExternalStorageState();
            return status.equals(Environment.MEDIA_MOUNTED);
        }
        catch (Exception e)
        {
            Log.e(TAG, "isSdCardMounted: exception while checking for sd card mounted.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2018 April 02 - Monday - 06:21 PM
     * Get IMEI Number method
     *
     * this method gets IMEI number after getting the permission.
     *
     * @return - it will return IMEI number if permission granted
     *           else if no permission granted then will return empty string.
    **/
    @CheckResult
    public static String getIMEINumber(Context context)
    {
        try
        {
            String imeiNumber;
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (telephonyManager != null)
            {
                // checking if read phone state permission given or not
                // if yes the getting the imei number
                // else asking for permission
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        imeiNumber = telephonyManager.getImei();
                        Log.e(TAG, "getIMEINumber: IMEI Number is: " + imeiNumber);
                    }
                    else
                    {
                        imeiNumber = telephonyManager.getDeviceId();
                        Log.e(TAG, "getIMEINumber: Device Id is: " + imeiNumber);
                    }

                    return imeiNumber;
                }
                else
                {
                    Log.e(TAG, "getIMEINumber: READ_PHONE_STATE permission not granted.");
                    return "";
                }
            }
            else
            {
                Log.e(TAG, "getIMEINumber: telephony manager was null.");
                return "";
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getIMEINumber: expection while getting imei number:\n");
            e.printStackTrace();
            return "";
        }
    }

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
            byte byteData[] = md.digest();
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
    public static Drawable getDrawable(@NonNull Context context, int id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            return context.getDrawable(id);
        }
        else
        {
            return context.getResources().getDrawable(id, null);
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return context.getColor(id);
        }
        else
        {
            return context.getResources().getColor(id, null);
        }
    }

    /**
     * get Time with AM/PM Method
     *
     * This method will show the time in two digits and also am pm
     * if the time selected is afternoon 02:00 then it will show 02:00 PM
     * else of the time selected is night 02:00 then it will show 02:00 AM
     *
     * @param hours - hours to convert
     * @param minutes - minutes to convert
     *
     * @return String with time appended with AM/PM
    **/
    public static String getTimeWithAMPM(int hours, int minutes)
    {
        try
        {
            String timeStamp = "AM", time;

            if (hours > 12)
            {
                timeStamp = "PM";
                hours -= 12;
            }
            else if (hours == 0)
            {
                timeStamp = "AM";
                hours += 12;
            }
            else if (hours == 12)
            {
                timeStamp = "PM";
            }

            time = String.format(Locale.getDefault(), "%02d", hours) + ":" +
                    String.format(Locale.getDefault(), "%02d", minutes) + " " + timeStamp;

            return time;
        }
        catch (Exception e)
        {
            Log.e("Exception", "in show time with am pm method in generate qr code activity:\n");
            e.printStackTrace();
            return "";
        }
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
    public static Bitmap drawableToBitmap (Drawable drawable)
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
     * get screen size
     * this method will get the size of the screen
     *
     * @param context - context of the application
     * @return size of the screen as Point
    **/
    public static Point getScreenSize(Context context)
    {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (wm != null)
        {
            wm.getDefaultDisplay().getSize(point);
        }

        return point;
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
     * 2018 September 18 - Tuesday - 04:54 PM
     * get battery percentage method
     *
     * this method will get the percentage of battery remaining
     *
     * @param context - context of the application
     * @return battery percentage in int or 0
    **/
    private static int getBatteryPercentage(Context context)
    {
        try
        {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, intentFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            float batteryPercentage = level / (float) scale;
            int batteryLevel = (int) (batteryPercentage * 100);

            Log.e(TAG, "getBatteryPercentage: current battery level is: " + batteryLevel);
            return batteryLevel;

        }
        catch (Exception e)
        {
            Log.e(TAG, "getBatteryPercentage: exception while getting battery percentage:\n");
            e.printStackTrace();
            return 0;
        }
    }
}
