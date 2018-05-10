package com.amit.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.CheckResult;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * https://github.com/jaydeepw/android-utils/tree/master/Utils
**/
public class Utils
{
    private static final String TAG = Utils.class.getSimpleName();

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
}
