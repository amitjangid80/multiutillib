package com.amit.utilities;

/*
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:52 PM
**/
@SuppressWarnings("unused")
public class TextUtilities
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
    public static int replaceTrueOrFalse(String string)
    {
        return string.equalsIgnoreCase("True") ? 1 : 0;
    }

    /**
     * 2018 September 14 - Friday - 12:34 PM
     * replace null with zero method
     *
     * this method will replace null or empty values of string with zero
     *
     * @param stringToReplace - string to replace null with
     * @return it will return 1 or 0
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
     * 2018 September 14 - Friday - 12:34 PM
     * remove last char method
     *
     * this method will remove the last character of the string
     *
     * @param stringToRemovedLastCharFrom - string to remove the last character from
     * @return it will return string with last character removed
     **/
    public static String removeLastChar(String stringToRemovedLastCharFrom)
    {
        if (stringToRemovedLastCharFrom != null && stringToRemovedLastCharFrom.length() > 0)
        {
            stringToRemovedLastCharFrom = stringToRemovedLastCharFrom.substring(0, stringToRemovedLastCharFrom.length() - 1);
        }

        return stringToRemovedLastCharFrom;
    }
}
