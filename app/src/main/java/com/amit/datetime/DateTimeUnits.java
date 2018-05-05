package com.amit.datetime;

/**
 * DateTimeUnits
 * Define units used by {@link DateTimeUtils#getDateDiff(Date, Date, DateTimeUnits)}
 * and also {@link DateTimeUtils#formatDate(long, DateTimeUnits)}
 *
**/
@SuppressWarnings("WeakerAccess")
public enum DateTimeUnits
{
    /**
     * Days
     */
    DAYS,

    /**
     * Hours
     */
    HOURS,

    /**
     * Minutes
     */
    MINUTES,

    /**
     * Seconds
     */
    SECONDS,

    /**
     * Milliseconds
     */
    MILLISECONDS,
}
