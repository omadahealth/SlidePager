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
package com.github.omadahealth.slidepager.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.omadahealth.slidepager.lib.interfaces.OnWeekListener;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.WeekSlideView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by stoyan on 4/3/15.
 */
public class SlidePagerAdapter extends PagerAdapter {

    /**
     * The start date of the {@link SlidePager}
     */
    private Date mStartDate;

    /**
     * The end date of the {@link SlidePager}
     */
    private Date mEndDate;

    /**
     * The context of the app
     */
    private Context mContext;

    /**
     * The list of {@link View} used to retain inflated views
     */
    private List<View> mViews;

    private TypedArray mAttributeSet;

    public void setAttributeSet(TypedArray attributeSet) {
        this.mAttributeSet = attributeSet;
    }


    /**
     * Calls {@link #SlidePagerAdapter(Context, Date, Date)} with the end date being set
     * to today
     *
     * @param context   The context
     * @param startDate The start {@link Date} for computing the number of weeks
     */
    public SlidePagerAdapter(Context context, Date startDate) {
        this(context, startDate, new Date());
    }

    /**
     * Public constructors to the {@link SlidePagerAdapter}
     *
     * @param context   The context
     * @param startDate The start {@link Date} for computing the number of weeks
     * @param endDate   The end {@link Date} for computing the number of weeks
     */
    public SlidePagerAdapter(Context context, Date startDate, Date endDate) {
        this(context, startDate, endDate, null);

    }

    public SlidePagerAdapter(Context context, Date startDate, Date endDate, TypedArray attributes) {
        this.mContext = context;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        setAttributeSet(attributes);
        this.mViews = initViews(startDate, endDate);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View currentView;
        if (mViews.size() > position) {
            currentView = mViews.get(position);
        } else {
            currentView = getWeekSlide();
            mViews.add(currentView);
        }

        collection.addView(currentView);
        return currentView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Get the current view based on the current position of the {@link SlidePager}
     *
     * @param position The position to get the view from
     * @return The view for the given position, if created yet
     */
    public View getCurrentView(int position) {
        if (mViews == null || position > mViews.size() - 1) {
            return null;
        }
        return mViews.get(position);
    }

    /**
     * Initializes an array the size of the number of weeks between the to parameters.
     * Throws {@link IllegalArgumentException} if startDate is after endDate
     *
     * @param startDate The start date
     * @param endDate   The end date
     * @return The number of weeks between the two dates
     */
    private List<View> initViews(Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        int weeks = Utilities.getWeeksBetween(startDate, endDate);
        int size = weeks == 0 ? 1 : weeks;
        List<View> views = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            views.add(getWeekSlide());
        }
        return views;
    }

    /**
     * Initializes a new {@link WeekSlideView} and sets its
     * {@link OnWeekListener}
     * @return
     */
    private WeekSlideView getWeekSlide(){
        WeekSlideView week = new WeekSlideView(mContext, mAttributeSet);
        week.setListener(new OnWeekListener() {
            @Override
            public void onDaySelected(int index) {
               Log.i("onDaySelected", "index : " + index);
            }
        });
        return week;
    }
}
