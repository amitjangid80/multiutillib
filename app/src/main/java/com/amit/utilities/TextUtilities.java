package com.amit.utilities;

/*
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:52 PM
**/

public class TextUtilities
{
    /**
     * replace null method
     * this method will replace null with empty space
     *
     * @param string - string where you want to replace null
     * @return it will return empty string
    **/
    public static String replaceNull(String string)
    {
        if (string == null)
        {
            return "";
        }

        if (string.equalsIgnoreCase("null"))
        {
            return "";
        }

        if (string.equalsIgnoreCase(" "))
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
        if (string.equalsIgnoreCase("True"))
        {
            return 1;
        }

        if (string.equalsIgnoreCase("False"))
        {
            return 0;
        }

        return 0;
    }
}
