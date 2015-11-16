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
package com.github.omadahealth.slidepager.lib.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Date;

/**
 * Created by stoyan on 4/3/15.
 */
public class SlidePagerAdapter extends AbstractSlidePagerAdapter<SlideView> {

    private static final String TAG = "SlidePagerAdapter";

    public SlidePagerAdapter(Context context, Date startDate) {
        super(context, startDate);
    }

    public SlidePagerAdapter(Context context, Date startDate, Date endDate) {
        super(context, startDate, endDate);
    }

    public SlidePagerAdapter(Context context, Date startDate, Date endDate, TypedArray attributes, OnSlidePageChangeListener pageListener, int maxWeeks) {
        super(context, startDate, endDate, attributes, pageListener, maxWeeks);
    }

    /**
     * Initializes an array the size of the number of weeks between the to parameters.
     * Throws {@link IllegalArgumentException} if startDate is after endDate
     *
     * @return The number of weeks between the two dates
     */
    protected SlideView[] initViews() {
        if (mEndDate.before(mStartDate)) {
            Log.e(TAG, "End date is before start date. Reverts to same date as start date");
            mStartDate = mEndDate;
        }
        mWeeks = Utilities.getWeeksBetween(mStartDate, mEndDate);
        mWeeks = mWeeks == 0 ? 1 : mWeeks;

        return new SlideView[mWeeks];
    }

    /**
     * Initializes a new {@link SlideView} and sets its
     * {@link OnSlideListener}
     *
     * @return
     */
    protected SlideView getWeekSlide(int pagePosition, int weeks) {
        int weeksSince = weeks - (pagePosition + 1);
        SlideView week = new SlideView(mContext, mAttributeSet, pagePosition, mUserPageListener, getLeftText(pagePosition, mWeeks), getRightText(weeksSince));
        week.setListener(new OnSlideListener() {
            @Override
            public void onDaySelected(int page, int index) {
                if (mUserPageListener != null) {
                    mUserPageListener.onDaySelected(page, index);
                }
            }

            @Override
            public boolean isDaySelectable(int page, int index) {
                if (mUserPageListener != null) {
                    return mUserPageListener.isDaySelectable(page, index);
                }
                //Allow by default, let user disable by choice
                return true;
            }

            @Override
            public String getDayTextLabel(int page, int index) {
                if (mUserPageListener != null) {
                    return mUserPageListener.getDayTextLabel(page, index);
                }
                //Null causes default text to be set
                return null;
            }
        });
        return week;
    }
}
