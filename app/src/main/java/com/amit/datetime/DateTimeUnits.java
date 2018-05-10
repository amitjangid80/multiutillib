package com.amit.datetime;

import java.util.Date;

/**
 * DateTimeUnits
 * Define units used by {@link DateTimeUtils#getDateDiff(Date, Date, DateTimeUnits)}
 * and also {@link DateTimeUtils#formatTimeStampToDate(long, DateTimeUnits)}
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
