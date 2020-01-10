package com.amit.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_WIFI_STATE;

/**
 * Created by AMIT JANGID on 09-Oct-18.
 *
 * this class will help in getting information related to devices
**/
@SuppressLint("HardwareIds")
@SuppressWarnings("unused")
public class DeviceUtils
{
    private static final String TAG = DeviceUtils.class.getSimpleName();

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
    public static String getIMEINumber(@NonNull Context context)
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
     * 2018 September 18 - Tuesday - 04:54 PM
     * get battery percentage method
     *
     * this method will get the percentage of battery remaining
     *
     * @param context - context of the application
     * @return battery percentage in int or 0
    **/
    @CheckResult
    public static int getBatteryPercentage(@NonNull Context context)
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

    /**
     * get device id method
     *
     * this method will get the device id
     *
     * @param context - application context
     * @return device id in string
    **/
    public static String getDeviceID(@NonNull Context context)
    {
        // this will get the device id of the device
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * get device name method
     *
     * this method will get the name of the device
     * @return name of the device with manufacturer
    **/
    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
        {
            return TextUtils.capitalizeString(model);
        }
        else
        {
            return TextUtils.capitalizeString(manufacturer + " " + model);
        }
    }

    /**
     * 2018 October 04 - Thursday - 02:50 PM
     * get mac address method
     *
     * this method will get mac address of the device
    **/
    public static String getMacAddress(Context context)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if ((ContextCompat.checkSelfPermission(context, ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED))
                {
                    Log.e(TAG, "getMacAddress: access wifi state permission not granted.");
                    return "";
                }
            }

            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface networkInterface : interfaces)
            {
                if (!networkInterface.getName().equalsIgnoreCase(interfaceName))
                {
                    continue;
                }

                byte[] mac = networkInterface.getHardwareAddress();

                if (mac == null)
                {
                    return "";
                }

                StringBuilder builder = new StringBuilder();

                for (byte aMac : mac)
                {
                    builder.append(String.format("%02X:", aMac));
                }

                if (builder.length() > 0)
                {
                    builder.deleteCharAt(builder.length() - 1);
                }

                Log.e(TAG, "getMacAddress: mac address of device is: " + builder.toString());
                return builder.toString();
            }

           return "";
        }
        catch (Exception e)
        {
            Log.e(TAG, "getMacAddress: exception while getting mac address:\n");
            e.printStackTrace();
            return "";
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
}
