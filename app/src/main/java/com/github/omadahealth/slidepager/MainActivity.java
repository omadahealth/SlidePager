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
package com.github.omadahealth.slidepager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.SlidePager;
import com.github.omadahealth.slidepager.lib.SlideTransformer;
import com.github.omadahealth.slidepager.lib.adapter.SlideChartPagerAdapter;
import com.github.omadahealth.slidepager.lib.adapter.SlidePagerAdapter;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements OnSlidePageChangeListener {

    /**
     * The slide pager we use
     */
    private SlidePager mSlidePager;

    /**
     * The slide pager we use
     */
    private SlidePager mSlideChartPager;

    /**
     * Max number of weeks in 'a program', could be set to the number of weeks if. Simply causes
     * slightly different output of the text displaying what week we are in
     */
    private static final int DEFAULT_PROGRAM_WEEKS = 16;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SlidePagerAdapter
        mSlidePager = (SlidePager) findViewById(R.id.slidepager1);
        SlidePagerAdapter adapterOne = new SlidePagerAdapter(this, getPreviousDate(16), new Date(), mSlidePager.getAttributeSet(), this, DEFAULT_PROGRAM_WEEKS);
        SlideView.setSelectedView(5);
        mSlidePager.setAdapter(adapterOne);
        mSlidePager.setPageTransformer(false, new SlideTransformer());
        mSlidePager.setOnPageChangeListener(this);
        mSlidePager.post(new Runnable() {
            @Override
            public void run() {
                mSlidePager.refreshPage();
            }
        });

        //SlideChartPagerAdapter
        mSlideChartPager = (SlidePager) findViewById(R.id.slidepager2);
        SlideChartPagerAdapter adapterChart = new SlideChartPagerAdapter(this, getPreviousDate(16), new Date(), mSlideChartPager.getAttributeSet(), this, DEFAULT_PROGRAM_WEEKS);
        mSlideChartPager.setAdapter(adapterChart);
        mSlideChartPager.setOnPageChangeListener(this);
        mSlideChartPager.post(new Runnable() {
            @Override
            public void run() {
                mSlideChartPager.refreshPage();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSlidePager.refreshPage();
        mSlideChartPager.refreshPage();
    }

    @Override
    public ProgressAttr getDayProgress(int page, int index) {
        int progress;
        Double value = null;
        if(page == 15 && index == 2){
            progress = 0;
        }
        else {
            value = 180.2d;
            progress = 100;
        }

        String day = Utilities.getSelectedDayText(getPreviousDate(16).getTime(), page, index);

        return new ProgressAttr(progress, value, day, page == 15 && index == 5);
    }

    @Override
    public void onDaySelected(int page, int index) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * Returns a date from a time
     *
     * @param weeks Number of weeks before today
     * @return
     */
    private Date getPreviousDate(int weeks) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        cal.add(Calendar.DAY_OF_YEAR, -1 * 7 * weeks);
        return new Date(cal.getTimeInMillis());
    }
}
