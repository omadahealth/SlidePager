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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.SlidePager;
import com.github.omadahealth.slidepager.lib.SlidePagerAdapter;
import com.github.omadahealth.slidepager.lib.SlideTransformer;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Date;

public class MainActivity extends ActionBarActivity implements OnSlidePageChangeListener {

    /**
     * The slide pager we use
     */
    private SlidePager mSlidePager;

    /**
     * Max number of weeks in 'a program', could be set to the number of weeks if. Simply causes
     * slightly different output of the text displaying what week we are in
     */
    private static final int DEFAULT_PROGRAM_WEEKS = 16;

    private Date mStartDate;

    private int mCurrentPage;
    private int mCurrentIndex;

    private ProgressAttr progressAttr = new ProgressAttr(0, false);
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStartDate = Utilities.getPreviousDate(DEFAULT_PROGRAM_WEEKS);

        mSlidePager = (SlidePager) findViewById(R.id.slidepager1);
        final SlidePagerAdapter adapterOne = new SlidePagerAdapter(this, mStartDate, new Date(), mSlidePager.getAttributeSet(), this, DEFAULT_PROGRAM_WEEKS);
        SlideView.setSelectedView(2);
        mSlidePager.setAdapter(adapterOne);
        mSlidePager.setPageTransformer(false, new SlideTransformer());
        mSlidePager.setOnPageChangeListener(this);


        Button change = (Button) findViewById(R.id.change_button);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressAttr = new ProgressAttr(progressAttr.getProgress() == 100 ? 0 : 100, mCurrentPage == 15 && mCurrentIndex == 4);
                adapterOne.getCurrentView(mCurrentPage).animateProgressView(mCurrentIndex, progressAttr);
            }
        });

        mSlidePager.post(new Runnable() {
            @Override
            public void run() {
                mSlidePager.refreshPage();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSlidePager.refreshPage();
    }

    @Override
    public ProgressAttr getDayProgress(int page, int index) {
        int progress;
        if (page == 15 && index > 4) {
            progress = 0;
        } else if (page == 15 && index == 4) {
            return progressAttr;
        } else {
            progress = 100;
        }

        return new ProgressAttr(progress, page == 15 && index == 4);
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
