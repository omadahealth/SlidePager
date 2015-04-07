package com.github.omadahealth.slidepager.lib;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.omadahealth.slidepager.lib.utils.Utilities;

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
     * The {@link LayoutInflater} used in {@link #instantiateItem(ViewGroup, int)}
     */
    private LayoutInflater mLayoutInflater;

    /**
     * The list of {@link View} used to retain inflated views
     */
    private List<View> mViews;

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
        this.mContext = context;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViews = initViews(startDate, endDate);
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
            currentView = mLayoutInflater.inflate(R.layout.view_week, null);
            mViews.add(currentView);
        }
//        collection.addView(currentView);
//        View currentView = mLayoutInflater.inflate(R.layout.view_week, null);
//        DayProgressView view = new DayProgressView(mContext);
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
            views.add(mLayoutInflater.inflate(R.layout.view_week, null));
        }
        return views;
    }
}
