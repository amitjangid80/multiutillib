package com.amit.utilities;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:52 PM
**/
@SuppressWarnings({"unused", "WeakerAccess"})
public class TextUtils
{
    /**
     * replace null method
     * this method will replace null with empty space
     *
     * @param string - string where you want to replace null
     * @return it will return empty string
    **/
    public static String replaceNullWithEmpty(String string)
    {
        if (string == null)
        {
            return "";
        }
        else if (string.equalsIgnoreCase("null"))
        {
            return "";
        }
        else if (string.equalsIgnoreCase(" "))
        {
            return "";
        }
        else if (string.equalsIgnoreCase(""))
        {
            return "";
        }

        return string;
    }

    /**
     * replace true or false
     * this method will replace true or false with 1 or 0
     *
     * @param string - string to replace true or false with
     * @return it will return 1 or 0
    **/
    public static int replaceTrueOrFalse(@NonNull String string)
    {
        return string.equalsIgnoreCase("True") ? 1 : 0;
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:35 AM
     * replace one or zero to boolean method
     * this method will replace 1 or 0 value to True or False
     *
     * @param valueToReplace - integer value to be replaced
     * @return True or False
    **/
    public static boolean replaceOneOrZeroToBoolean(int valueToReplace)
    {
        return valueToReplace == 1;
    }

    /**
     * 2018 September 14 - Friday - 12:34 PM
     * replace null with zero method
     *
     * this method will replace null or empty values of string with zero
     *
     * @param stringToReplace - string to replace null with
     * @return it will return string value passed or 0
    **/
    public static int replaceNullWithZero(String stringToReplace)
    {
        if (stringToReplace == null)
        {
            return 0;
        }
        else if (stringToReplace.equalsIgnoreCase("null"))
        {
            return 0;
        }
        else if (stringToReplace.equalsIgnoreCase(" "))
        {
            return 0;
        }
        else if (stringToReplace.equalsIgnoreCase(""))
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(stringToReplace);
        }
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:35 AM
     * replace null by zero method
     *
     * this method will replace null or empty values of string with zero
     *
     * @param stringToReplace - string to replace null with
     * @return it will return string value passed or 0 as string
    **/
    public static String replaceNullByZero(String stringToReplace)
    {
        if (stringToReplace == null)
        {
            return "0";
        }
        else if (stringToReplace.equalsIgnoreCase("null"))
        {
            return "0";
        }
        else if (stringToReplace.equalsIgnoreCase(" "))
        {
            return "0";
        }
        else if (stringToReplace.equalsIgnoreCase(""))
        {
            return "0";
        }
        else
        {
            return stringToReplace;
        }
    }
    
    /**
     * replace null method
     * this method will replace null with empty space
     *
     * @param string - string where you want to replace null
     * @return it will return empty string
    **/
    public static String replaceNullWithDash(String string)
    {
        if (string == null)
        {
            return "-";
        }
        else if (string.equalsIgnoreCase("null"))
        {
            return "-";
        }
        else if (string.equalsIgnoreCase(" "))
        {
            return "-";
        }
        else if (string.equalsIgnoreCase(""))
        {
            return "-";
        }
        
        return string;
    }

    /**
     * 2018 September 14 - Friday - 12:34 PM
     * remove last char method
     *
     * this method will remove the last character of the string
     *
     * @param stringToRemoveLastCharFrom - string to remove the last character from
     * @return it will return string with last character removed
    **/
    public static String removeLastChar(String stringToRemoveLastCharFrom)
    {
        if (stringToRemoveLastCharFrom != null && stringToRemoveLastCharFrom.length() > 0)
        {
            stringToRemoveLastCharFrom = stringToRemoveLastCharFrom.substring(0, stringToRemoveLastCharFrom.length() - 1);
        }

        return stringToRemoveLastCharFrom;
    }

    /**
     * capitalizeString method
     *
     * this method will capitalizeString or set the string to upper case
     *
     * @param string - string to capitalize
     * return - will return the string which was passed in capitalize form
    **/
    public static String capitalizeString(String string)
    {
        if (string == null || string.length() == 0)
        {
            return "";
        }

        char first = string.charAt(0);

        if (Character.isUpperCase(first))
        {
            return string;
        }
        else
        {
            return Character.toUpperCase(first) + string.substring(1);
        }
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:41 AM
     * format decimal for one places.
     *
     * this method will format decimal value to one places after decimal
     *
     * @param valueToFormat - value in double data type to format
     *
     * @return formatted value in double with one places after decimal
    **/
    public static double formatDecimalForOnePlaces(double valueToFormat)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("0.0", symbols);
        return Double.parseDouble(decimalFormat.format(valueToFormat));
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:41 AM
     * format decimal for two places.
     *
     * this method will format decimal value to two places after decimal
     *
     * @param valueToFormat - value in double data type to format
     *
     * @return formatted value in double with two places after decimal
    **/
    public static double formatDecimalForTwoPlaces(double valueToFormat)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        return Double.parseDouble(decimalFormat.format(valueToFormat));
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:41 AM
     * format decimal for three places.
     *
     * this method will format decimal value to three places after decimal
     *
     * @param valueToFormat - value in double data type to format
     *
     * @return formatted value in double with three places after decimal
    **/
    public static double formatDecimalForThreePlaces(double valueToFormat)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("0.000", symbols);
        return Double.parseDouble(decimalFormat.format(valueToFormat));
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:41 AM
     * format decimal for four places.
     *
     * this method will format decimal value to four places after decimal
     *
     * @param valueToFormat - value in double data type to format
     *
     * @return formatted value in double with four places after decimal
    **/
    public static double formatDecimalForFourPlaces(double valueToFormat)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("0.0000", symbols);
        return Double.parseDouble(decimalFormat.format(valueToFormat));
    }
    
    /**
     * 2019 Apr 26 - Friday - 11:41 AM
     * format decimal to single digit
     *
     * this method will format decimal value to single digit
     * if you don't want to show 0 after decimal
     *
     * @param valueToFormat - value in double data type to format
     *
     * @return formatted value in double with three places after decimal
    **/
    public static String formatDecimalToSingleDigit(double valueToFormat)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("#", symbols);
        return decimalFormat.format(valueToFormat);
    }
}
