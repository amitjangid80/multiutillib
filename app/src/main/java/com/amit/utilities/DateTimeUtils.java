package com.amit.utilities;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AMIT JANGID on 09-Oct-18.
 * <p>
 * this class will help in formatting date and time
**/
@SuppressWarnings("unused, deprecation")
public class DateTimeUtils
{
    private static final String TAG = DateTimeUtils.class.getSimpleName();

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

        return twoDigitString(hours) + " : " +
                twoDigitString(minutes) + " : "
                + twoDigitString(seconds);
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
}