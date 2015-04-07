package com.github.omadahealth.slidepager;

import com.github.omadahealth.slidepager.lib.utils.Utilities;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

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
        cal.add(Calendar.DAY_OF_WEEK, -7);
        assertEquals(1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), new Date()));

        //Check one week to
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_WEEK, 7);
        assertEquals(-1, Utilities.getWeeksBetween(new Date(cal.getTimeInMillis()), new Date()));
    }
}
