package com.github.omadahealth.slidepager;

import com.github.omadahealth.slidepager.lib.utils.Utilities;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stoyan on 4/6/15.
 */
public class UtilitiesTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests {@link Utilities#getWeeksBetween(Date, Date)}
     */
    public void testGetWeeksBetween(){
        //Check same day
        assertEquals(0, Utilities.getWeeksBetween(new Date(), new Date()));

        //Check one week ago
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        SimpleDateFormat df = new SimpleDateFormat(Utilities.DATE_FULL_STRING_FORMAT, Locale.US);
        while(!df.format(cal.getTime()).toLowerCase().equals("sunday")){
            cal.add(Calendar.DAY_OF_WEEK, -1);
        }
        now = cal.getTime();
        //1 days ago, sat
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
        //2 days ago, fri
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
        //3 days ago, thru
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
        //4 days ago, wen
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
        //5 days ago, tue
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
        //6 days ago, mon
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
        //7 days ago, sun
        cal.add(Calendar.DAY_OF_WEEK, -1);
        assertEquals(2, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));

        //Check one week to
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_WEEK, 7);
        assertEquals(-2, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), now));
    }

}
