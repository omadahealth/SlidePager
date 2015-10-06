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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.demo.databinding.ActivityMainBinding;
import com.github.omadahealth.slidepager.lib.SlideTransformer;
import com.github.omadahealth.slidepager.lib.adapter.SlideChartPagerAdapter;
import com.github.omadahealth.slidepager.lib.adapter.SlidePagerAdapter;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Date;

public class MainActivity extends ActionBarActivity implements OnSlidePageChangeListener<ChartProgressAttr> {
    /**
     * Tag for logging this class
     */
    private static final String TAG = "MainActivity";

    /**
     * Max number of weeks in 'a program', could be set to the number of weeks if. Simply causes
     * slightly different output of the text displaying what week we are in
     */
    private static final int DEFAULT_PROGRAM_WEEKS = 16;

    /**
     * Start date of the {@link ActivityMainBinding#slidePager}
     */
    private Date mStartDate;

    /**
     * The current index of the {@link SlidePagerAdapter} we set on {@link ActivityMainBinding#slidePager}
     */
    private int mCurrentPage;

    /**
     * The current index of the {@link com.github.omadahealth.slidepager.lib.views.ProgressView}
     * in the displaying {@link android.transition.Slide}
     */
    private int mCurrentIndex;

    private ProgressAttr progressAttr = new ProgressAttr(0, false, false);

    private ActivityMainBinding mainBinding;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //SlidePagerAdapter
        mStartDate = Utilities.getPreviousDate(DEFAULT_PROGRAM_WEEKS);

        final SlidePagerAdapter adapterOne = new SlidePagerAdapter(this, mStartDate, new Date(), mainBinding.slidePager.getAttributeSet(), this, DEFAULT_PROGRAM_WEEKS);
        SlideView.setSelectedView(0);
        mainBinding.slidePager.setAdapter(adapterOne);
        mainBinding.slidePager.setPageTransformer(false, new SlideTransformer());
        mainBinding.slidePager.setOnPageChangeListener(this);

        Button change = (Button) findViewById(R.id.change_button);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressAttr = new ProgressAttr(progressAttr.getProgress() == 100 ? 0 : 100, mCurrentPage == 15 && mCurrentIndex == 4, false);
                adapterOne.getCurrentView(mCurrentPage).animateProgressView(mCurrentIndex, progressAttr);
            }
        });
        mainBinding.slidePager.post(new Runnable() {
            @Override
            public void run() {
                mainBinding.slidePager.refreshPage();
            }
        });


        //SlideChartPagerAdapter
        SlideChartPagerAdapter adapterChart = new SlideChartPagerAdapter(this, Utilities.getPreviousDate(16), new Date(), mainBinding.chartPager.getAttributeSet(), this, DEFAULT_PROGRAM_WEEKS);
        mainBinding.chartPager.setAdapter(adapterChart);
        mainBinding.chartPager.setOnPageChangeListener(this);
        mainBinding.chartPager.post(new Runnable() {
            @Override
            public void run() {
                mainBinding.chartPager.refreshPage();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainBinding.slidePager.refreshPage();
        mainBinding.chartPager.refreshPage();
    }

    @Override
    public ChartProgressAttr getDayProgress(int page, int index) {
        Log.i(TAG, "Get day progress : " + page + ", " + index);
        int progress = 0;
        Double value = null;
        if (page == 14) {
            value = 180.2d;
            progress = 100;
        } else {
            value = 180.2d;
            progress = (int) Math.round((index % 4) * 33.33d);
        }

        String day = Utilities.getSelectedDayText(Utilities.getPreviousDate(16).getTime(), page, index);

        int color = getResources().getColor(progress < 34 ? R.color.red :
                progress < 67 ? R.color.orange :
                        R.color.green);

        return new ChartProgressAttr(progress, value, day, page == 15 && index == 5, page == 15 && index == 6, color, color, R.drawable.checkmark_green, false, false);
    }

    @Override
    public void onDaySelected(int page, int index) {
        Log.i("MainActivity", "onDaySelected : " + page + ", " + index);
        mCurrentPage = page;
        mCurrentIndex = index;
    }

    @Override
    public boolean isDaySelectable(int page, int index) {
        if (page == 15 && index > 4) {
            return false;
        }
        return true;
    }

    @Override
    public String getDayTextLabel(int page, int index) {
        return Utilities.getSelectedDayText(mStartDate.getTime(), page, index);
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

}
