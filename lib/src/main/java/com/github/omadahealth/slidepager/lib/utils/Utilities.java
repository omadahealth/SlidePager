/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Omada Health, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.omadahealth.slidepager.lib.utils;

import com.github.omadahealth.slidepager.lib.adapter.SlidePagerAdapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by stoyan on 4/6/15.
 */
public class Utilities {

    public static final long MILLI_SECONDS_IN_WEEK = 1000 * 60 * 60 * 24 * 7;

    /**
     * Day in month: March 30
     */
    private static final String MONTH_IN_YEAR_STRING_FORMAT = "LLLL d";

    /**
     * Day:  Tuesday
     */
    public static final String DATE_FULL_STRING_FORMAT = "EEEE";

    /**
     * Short month day:   Jan 20
     */
    public static final String DATE_SHORT_MONTH_DAY_STRING_FORMAT = "LLL d";

    /**
     * Day in a week: Tue
     */
    public static final String DATE_SHORTENED_DAY_OF_WEEK_STRING_FORMAT = "EEE";

    /**
     * 123.432654 => 123.4
     */
    public static final String WEIGHT_FORMAT = "0.0";

    /**
     * The precision of the scale's rounding
     */
    public static final double WEIGHT_SCALE_PRECISION = 0.2;

    /**
     * The string to show if the date is today
     */
    public static final String TODAY_UPPERCASE = "TODAY";

    /**
     * Calculates the number of weeks between two dates
     *
     * @param start The start date
     * @param end   The end date
     * @return Number of weeks the end date is after the start date, could be negative
     */
    public static int getWeeksBetween(Date start, Date end) {
        //if end is before start
        if (end.before(start)) {
            return -getWeeksBetween(end, start);
        }

        start = resetTime(start);
        end = resetTime(end);

        //if same day
        if (start.compareTo(end) == 0) {
            return 0;
        }

        Calendar cal = new GregorianCalendar();
        cal.setTime(start);
        int weeks = 0;
        while (cal.getTime().compareTo(end) <= 0) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    /**
     * Sets the time of date to midnight
     *
     * @param date the date
     * @return the start of that date
     */
    public static Date resetTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Generates a string representing the date range of, ie. April 5 - April 11
     *
     * @param date        The initial date
     * @param weeksBefore Number of weeks before the date
     * @return ie. April 5 - April 11
     */
    public static String getWeekRangeText(Date date, int weeksBefore) {
        SimpleDateFormat df = new SimpleDateFormat(MONTH_IN_YEAR_STRING_FORMAT, Locale.US);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, -weeksBefore);

        //Get the start of the week
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        String dateString = df.format(cal.getTime()) + " - ";

        //Get the end of the week
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        dateString += df.format(cal.getTime());

        return dateString;
    }

    /**
     * Generates a string representing the current week compared to the total weeks represented
     *
     * @param position   The current View in the {@link SlidePagerAdapter}
     * @param totalWeeks The total number of weeks the {@link SlidePagerAdapter}
     * @param maxWeeks   The number of set weeks, could be the same as maxWeeks
     * @return Html string of what week we are in
     */
    public static String getWeekOfText(int position, int totalWeeks, int maxWeeks) {
        String start = "<html><b>Week ";
        String mid = " </b>of ";
        String end = "</html>";
        if (totalWeeks > maxWeeks) {
            return "<html><b>Week " + (position + 1) + "</b></html>";
        }

        return start + (position + 1) + mid + totalWeeks + end;
    }

    /**
     * Formats the weight to a "0.0" in an increment of {@link #WEIGHT_SCALE_PRECISION} format and returns it as a double
     *
     * @param value The value to format
     * @return
     */
    public static double formatWeightToDouble(Double value) {
        if (value == null) {
            value = 0.0;
        }
        double diff = value % WEIGHT_SCALE_PRECISION;
        double roundedValue;
        if (diff >= 0.1) {
            roundedValue = value + (WEIGHT_SCALE_PRECISION - diff);
        } else {
            roundedValue = value - diff;
        }
        DecimalFormat weightFormat = new DecimalFormat(WEIGHT_FORMAT, new DecimalFormatSymbols(Locale.ENGLISH));
        return Double.parseDouble(weightFormat.format(roundedValue));
    }

    /**
     * Formats the weight of to "0.0" format
     *
     * @param value The value to format
     * @return
     */
    public static String formatWeight(Double value) {
        if (value == null) {
            return "-";
        }

        DecimalFormat weightFormat = new DecimalFormat(WEIGHT_FORMAT, new DecimalFormatSymbols(Locale.ENGLISH));
        return weightFormat.format(formatWeightToDouble(value));
    }

    /**
     * Checks to see if time is within this week. Performs second-millisecond
     * conversion
     *
     * @param milli The epoch time in seconds or milliseconds
     * @return True, if the date is within 1 week in the past
     */
    public static boolean withinAWeek(long milli) {
        long diff = System.currentTimeMillis() - toMilliseconds(milli);
        return diff < MILLI_SECONDS_IN_WEEK;
    }

    /**
     * Checks to see if the time given is in the same day as today
     * Function checks that time is in milliseconds
     *
     * @param milli time
     * @return true if it is the same day
     */
    public static boolean isToday(long milli) {
        long inMilli = toMilliseconds(milli);
        return getStartOfDay(new Date(inMilli)) == getStartOfDay(new Date());
    }

    /**
     * Get a selected day text from a start point and a page and index
     */
    public static Date getSelectedDay(long start, int page, int index) {
        Calendar cal = Calendar.getInstance();
        Date startDate = new Date(start);
        cal.setTime(startDate);

        cal.add(Calendar.WEEK_OF_YEAR, page);
        cal.add(Calendar.DAY_OF_YEAR, index);
        return new Date(getStartOfDay(new Date(cal.getTimeInMillis())));
    }

    /**
     * Returns the start of the day in milliseconds
     *
     * @param day The day who's start we need
     * @return Time in milliseconds
     */
    public static long getStartOfDay(Date day) {
        if (day == null) {
            day = new Date();
        }

        // use UTC time zone
        Calendar cal = Calendar.getInstance();
        cal.setTime(day); // compute start of the day for the timestamp
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * Converts any time to milliseconds
     *
     * @param since The epoch time, seconds or milliseconds
     * @return The epoch time in milliseconds
     */
    public static long toMilliseconds(long since) {
        if (since == 0) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.getTimeInMillis() / since > 10) {
            return TimeUnit.SECONDS.toMillis(since);
        }
        return since;
    }

    /**
     * Returns a date from a time
     *
     * @param weeksBefore Number of weeksBefore before today
     * @return
     */
    public static Date getPreviousDate(int weeksBefore) {
        weeksBefore--;
        Calendar cal = new GregorianCalendar();
        Date now = new Date();

        cal.setTime(now);
        //Get the start of the week
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        cal.add(Calendar.WEEK_OF_YEAR, -weeksBefore);

        return cal.getTime();
    }

    /**
     * Returns the selected day text in  'Jan 20' format
     *
     * @param start The start date of the {@link com.github.omadahealth.slidepager.lib.SlidePager} in milliseconds
     * @param page  The index of the page
     * @param index The index of the view inside the page
     * @return The formatted date string
     */
    public static String getSelectedDayText(long start, int page, int index) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_SHORT_MONTH_DAY_STRING_FORMAT, Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        Date startDate = new Date(start);
        cal.setTime(startDate);

        cal.add(Calendar.WEEK_OF_YEAR, page);
        cal.add(Calendar.DAY_OF_YEAR, index);
        return sf.format(new Date(cal.getTimeInMillis())).toUpperCase();
    }
}
