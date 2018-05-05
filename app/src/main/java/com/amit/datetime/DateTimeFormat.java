package com.amit.datetime;

/**
 * DateTimeFormat
 *  Patterns used to parse given date {@link DateTimeUtils} will use those pattern
 *
**/
@SuppressWarnings("WeakerAccess")
public class DateTimeFormat
{
    /**
     * Typical MySqL/SQL dateTime format with dash as separator
     */
    public static final String DATE_TIME_PATTERN_1  = "yyyy-MM-dd HH:mm:ss";

    /**
     * Typical MySqL/SQL dateTime format with slash as separator
     */
    public static final String DATE_TIME_PATTERN_2  = "dd/MM/yyyy HH:mm:ss";

    public static final String DATE_TIME_PATTERN_3  = "dd-MM-yyyy HH:mm:ss";

    /**
     * Typical MySqL/SQL date format with dash as separator
     */
    public static final String DATE_PATTERN_1 = "yyyy-MM-dd";

    /**
     * Typical MySqL/SQL date format with slash as separator
     */
    public static final String DATE_PATTERN_2 = "dd/MM/yyyy";

    public static final String DATE_PATTERN_3 = "dd-MM-yyyy";

    /**
     * Time format full in 24 hours format
     */
    public static final String TIME_PATTERN_1 = "HH:mm:ss";

    /**
     * Time format with am/pm in 12 hours format
    **/
    public static final String TIME_PATTERN_2 = "hh:mm a";
}
