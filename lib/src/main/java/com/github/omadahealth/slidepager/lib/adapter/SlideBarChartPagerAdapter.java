package com.github.omadahealth.slidepager.lib.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.github.omadahealth.slidepager.lib.SlidePager;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.SlideBarChartView;
import com.github.omadahealth.slidepager.lib.views.SlideChartView;
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Date;

/**
 * Created by dae.park on 10/5/15.
 */
public class SlideBarChartPagerAdapter extends AbstractSlidePagerAdapter<SlideBarChartView> {

    public SlideBarChartPagerAdapter(Context context, Date startDate) {
        this(context, startDate, new Date());
    }

    public SlideBarChartPagerAdapter(Context context, Date startDate, Date endDate) {
        this(context, startDate, endDate, null, null, -1);
    }

    public SlideBarChartPagerAdapter(Context context, Date startDate, Date endDate, TypedArray attributes, OnSlidePageChangeListener pageListener, int maxWeeks) {
        super(context, startDate, endDate, attributes, pageListener, maxWeeks);
    }

    /**
     * Initializes an array the size of the number of weeks between the to parameters.
     * Throws {@link IllegalArgumentException} if startDate is after endDate
     *
     * @return The number of weeks between the two dates
     */
    protected SlideBarChartView[] initViews() {
        if (mEndDate.before(mStartDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        mWeeks = Utilities.getWeeksBetween(mStartDate, mEndDate);
        mWeeks = mWeeks == 0 ? 1 : mWeeks;

        return new SlideBarChartView[mWeeks];
    }

    /**
     * Initializes a new {@link SlideView} and sets its
     * {@link OnSlideListener}
     *
     * @return
     */
    protected SlideBarChartView getWeekSlide(int pagePosition, int weeks) {
        SlideBarChartView week = new SlideBarChartView(mContext, mAttributeSet, pagePosition, mUserPageListener);
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


    /**
     * Returns the right text for the {@link SlideView}
     *
     * @return
     */
    public String getRightText(int weeksSince) {
        return Utilities.getWeekRangeText(mEndDate, weeksSince);
    }
}
