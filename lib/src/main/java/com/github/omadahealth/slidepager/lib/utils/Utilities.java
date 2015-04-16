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

    public static Date resetTime(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getWeekRangeText(Date date, int weeksSince) {
        SimpleDateFormat df = new SimpleDateFormat(MONTH_IN_YEAR_STRING_FORMAT, Locale.US);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, -weeksSince);

        //get beginning of week
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        String dateString = df.format(cal.getTime()) + " - ";

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        dateString += df.format(cal.getTime());

        return dateString;
    }
}
