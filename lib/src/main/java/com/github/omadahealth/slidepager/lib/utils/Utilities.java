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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by stoyan on 4/6/15.
 */
public class Utilities {
    /**
     * Day in month: March 30
     */
    private static final String MONTH_IN_YEAR_STRING_FORMAT = "LLLL d";

    /**
     * Short month day:   Jan 20
     */
    public static final String DATE_SHORT_MONTH_DAY_STRING_FORMAT = "LLL d";

    /**
     * Calculates the number of weeks between two dates
     * @param start The start date
     * @param end The end date
     * @return Number of weeks the end date is after the start date, could be negative
     */
    public static int getWeeksBetween(Date start, Date end) {

        if (end.before(start)) {
            return -getWeeksBetween(end, start);
        }
        start = resetTime(start);
        end = resetTime(end);

        Calendar cal = new GregorianCalendar();
        cal.setTime(start);
        int weeks = 0;
        while (cal.getTime().before(end)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    /**
     * Sets the time of date to midnight
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
     * @param date The initial date
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
     * @param position The current View in the {@link com.github.omadahealth.slidepager.lib.SlidePagerAdapter}
     * @param totalWeeks The total number of weeks the {@link com.github.omadahealth.slidepager.lib.SlidePagerAdapter}
     * @param maxWeeks The number of set weeks, could be the same as maxWeeks
     * @return Html string of what week we are in
     */
    public static String getWeekOfText(int position, int totalWeeks, int maxWeeks) {
        String start = "<html><b>Week ";
        String mid = " </b>of ";
        String end = "</html>";
        if(totalWeeks > maxWeeks){
            return "<html><b>Week " + (position + 1) + "</b></html>";
        }

        return start + (position + 1) + mid + totalWeeks + end;
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
