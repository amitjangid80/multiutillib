package com.amit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.CheckResult;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created By AMIT JANGID
 * 2018 April 17 - Tuesday - 12:50 PM
**/
public class Internet
{
    private static final String TAG = Internet.class.getSimpleName();

    /**
     * isInternetConnected
     * this method will check for connectivity service
     *
     * @param context - context of the application
     * @return - returns true or false.
    **/
    @CheckResult
    public static boolean isInternetConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        else
        {
            return false;
        }
    }

    /**
     * isMobileNetConnected
     * this method helps to find out whether the user is connected to mobile internet or not
     *
     * @param context - context of the application
     * @return this returns true or false
     *         true if connected or connecting
     *         false if not connected.
    **/
    @CheckResult
    public static boolean isMobileNetConnected(Context context)
    {
        try
        {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

            if (manager != null)
            {
                return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
            }
            else
            {
                Log.e(TAG, "isMobileInternetAvailable: connectivity manager was null.");
                return false;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "isMobileInternetAvailable: exception while checking for mobile internet.");
            return false;
        }
    }

    /**
     * isWifiConnected
     * this method will check if the device is connected to a wifi
     *
     * @param context - context of the application
     * @return - this returns true or false
     *           true if connected
     *           false if not connected
    **/
    @CheckResult
    public static boolean isWifiConnected(Context context)
    {
        try
        {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

            if (manager != null)
            {
                return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
            }
            else
            {
                Log.e(TAG, "isWifiConnected: connectivity manager is null.");
                return false;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "isWifiConnected: exception while checking for wifi connection.");
            e.printStackTrace();
            return false;
        }
    }
}
