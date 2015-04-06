package com.github.omadahealth.slidepager.lib;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
     * The {@link LayoutInflater} used in {@link #instantiateItem(ViewGroup, int)}
     */
    private LayoutInflater mLayoutInflater;
    /**
     * The list of {@link View} used to retain inflated views
     */
    private List<View> mViews;

    /**
     * Public constructors to the {@link SlidePagerAdapter}
     *
     * @param startDate The start {@link Date} for computing the number of weeks
     * @param endDate   The end {@link Date} for computing the number of weeks
     */
    public SlidePagerAdapter(Context context, Date startDate, Date endDate) {
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View currentView;
        if (mViews.size() >= position) {
            currentView = mViews.get(position);
        } else {
            currentView = mLayoutInflater.inflate(R.layout.view_day_progress, null);
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
}
