package com.amit.utilities;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AMIT JANGID on 09-Oct-18.
 * <p>
 * this class will help in formatting date and time
**/
@SuppressWarnings({"unused, deprecation", "WeakerAccess"})
public class DateTimeUtils
{
    private static final String TAG = DateTimeUtils.class.getSimpleName();

    /**
     * 2018 November 03 - Saturday - 12:00 PM
     * get current date time method
     *
     * this method will current date time in string type
     *
     * @param inDateTimeFormat - Pass the date or date time format you
     *                           want to get date or date time in format
     *                           Ex: dd-MM-yyyy or dd-MM-yyyy hh:mm
     *
     * @return - date or date time returned
    **/
    public static String getCurrentDateTime(String inDateTimeFormat)
    {
        if (inDateTimeFormat != null && inDateTimeFormat.length() != 0)
        {
            String dateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
            return DateTimeUtils.formatDateTime(dateTime, inDateTimeFormat);
        }

        Log.e(TAG, "getCurrentDateTime: given date format is not valid");
        return "";
    }

    /**
     * format date time method
     * <p>
     * this method will format date time in to formats user provides
     *
     * @param dateToFormat - date time which you need to format
     *                       EX: 2018-10-09
     *
     * @param inDateTimeFormat - format of the date time in which you want to format given date
     *                           EX: dd-MM-yyyy OR dd-MM-yyyy hh:mm:ss
     *
     * @return date time in format provided
    **/
    public static String formatDateTime(String dateToFormat, String inDateTimeFormat)
    {
        if (dateToFormat != null && dateToFormat.length() > 0)
        {
            return DateFormat.format(inDateTimeFormat, new Date(dateToFormat)).toString();
        }

        Log.e(TAG, "formatDateTime: give date cannot be parsed in given format.");
        return dateToFormat;
    }

    /**
     * format date time method
     * <p>
     * this method will format date time in to formats user provides
     *
     * @param dateToFormat - date time which you need to format
     *                       EX: 2018-10-09
     *
     * @param inDateTimeFormat - format of the date time in which you want to format given date
     *                           EX: dd-MM-yyyy OR dd-MM-yyyy hh:mm:ss
     *
     * @param fromDateTimFormat - format of date time from which you want to format
     *                            EX: yyyy-MM-dd OR dd-MM-yyyy hh:mm:ss
     *
     * @return date time in format provided
    **/
    public static String formatDateTime(String dateToFormat, String inDateTimeFormat, String fromDateTimFormat)
            throws ParseException
    {
        if (dateToFormat != null && dateToFormat.length() > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(fromDateTimFormat, Locale.getDefault());
            return DateFormat.format(inDateTimeFormat, sdf.parse(dateToFormat)).toString();
        }

        Log.e(TAG, "formatDateTime: give date cannot be parsed in given format.");
        return dateToFormat;
    }

    /**
     * 2018 April 27 - Friday - 04:00 PM
     * format milli seconds to time method
     *
     * this method formats the string in hh:mm:ss format
    **/
    public static String formatMilliSecondsToTime(long milliseconds)
    {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    
        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    /**
     * two digit string method
     *
     * this string formats the given parameter in two digits
     *
     * @param number - number to be formatted in two digits
     *
     * @return returns number in two digits in string format
    **/
    public static String twoDigitString(long number)
    {
        if (number == 0)
        {
            return "00";
        }

        if (number / 10 == 0)
        {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    /**
     * 2018 September 22 - Saturday - 04:38 PM
     * convert days in millis method
     *
     * this method will convert days in milliseconds
     *
     * @param days - days value in integer to be converted
     *
     * @return returns milli seconds value of given number of days
    **/
    public static long convertDaysInMillis(int days)
    {
        return days * 24 * 60 * 60 * 1000;
    }
    
    /**
     * 2019 June 03 - Monday - 12:14 PM
     * get current fin year method
     *
     * this method will get current fin year in yy-yy format
     *
     * @param context - context of the application or activity
     *
     * @return it will return current fin year in yy-yy format
    **/
    public static String getCurrentFinYear(Context context)
    {
        try
        {
            String currentFinYear;
            
            int currentYear = Integer.parseInt(DateTimeUtils.getCurrentDateTime("yy"));
            int currentMonth = Integer.parseInt(DateTimeUtils.getCurrentDateTime("MM"));
            
            int nextYear = currentYear + 1;
            
            if (currentMonth <= 3)
            {
                nextYear = currentYear;
                currentYear = currentYear - 1;
            }
            
            currentFinYear = currentYear + "-" + nextYear;
            return currentFinYear;
        }
        catch (Exception e)
        {
            Log.e(TAG, "getCurrentFinYear: exception while getting current fin year:\n");
            e.printStackTrace();
            
            return "";
        }
    }
    
    /**
     * 2019 July 15 - Monday - 01:29 PM
     * convert to date time from milliseconds method
     *
     * this method will convert milliseconds to date time
     *
     * @param context           - context of the application or activity
     *
     * @param milliseconds      - milli seconds to be converted to date time
     *
     * @param inDateTimeFormat  - format in which you want date time
     *                            Example: yyyy-MM-dd HH:mm:ss
     *
     * @return Date time in specified in inDateTimeFormat
    **/
    private static String convertToDateTimeFromMilliseconds(Context context, Long milliseconds, String inDateTimeFormat)
    {
        try
        {
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(inDateTimeFormat, Locale.US);
            
            // Create a calendarIcon object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            return formatter.format(calendar.getTime());
        }
        catch (Exception e)
        {
            Log.e(TAG, "exception while converting to date time from milliseconds:\n");
            e.printStackTrace();
            
            return String.valueOf(milliseconds);
        }
    }
}
