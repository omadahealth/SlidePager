package com.github.omadahealth.slidepager.lib.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.github.omadahealth.slidepager.lib.SlidePager;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.slidepager.lib.views.AbstractSlideView;
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Date;

/**
 * Created by oliviergoutay on 8/5/15.
 */
public abstract class AbstractSlidePagerAdapter<T extends AbstractSlideView> extends PagerAdapter {
    /**
     * The maximum number of weeks before we change the {@link #getLeftText(int, int)}
     */
    protected static final int DEFAULT_MAX_WEEKS = 10;

    /**
     * The start date of the {@link SlidePager}
     */
    protected Date mStartDate;

    /**
     * The end date of the {@link SlidePager}
     */
    protected Date mEndDate;

    /**
     * The context of the app
     */
    protected Context mContext;

    /**
     * The list of {@link View} used to retain inflated views
     */
    protected T[] mViews;

    /**
     * Weeks that this view represents
     */
    protected int mWeeks;

    /**
     * The weeks in the program
     */
    protected final int mMaxWeeks;

    /**
     * A user defined {@link ViewPager.OnPageChangeListener}
     */
    protected OnSlidePageChangeListener mUserPageListener;

    /**
     * The attribute set from xml
     */
    protected TypedArray mAttributeSet;

    public void setAttributeSet(TypedArray attributeSet) {
        this.mAttributeSet = attributeSet;
    }

    /**
     * Calls {@link #AbstractSlidePagerAdapter(Context, Date, Date)} with the end date being set
     * to today
     *
     * @param context   The context
     * @param startDate The start {@link Date} for computing the number of weeks
     */
    public AbstractSlidePagerAdapter(Context context, Date startDate) {
        this(context, startDate, new Date());
    }

    /**
     * Public constructors to the {@link SlidePagerAdapter}
     *
     * @param context   The context
     * @param startDate The start {@link Date} for computing the number of weeks
     * @param endDate   The end {@link Date} for computing the number of weeks
     */
    public AbstractSlidePagerAdapter(Context context, Date startDate, Date endDate) {
        this(context, startDate, endDate, null, null, -1);
    }


    public AbstractSlidePagerAdapter(Context context, Date startDate, Date endDate, TypedArray attributes, OnSlidePageChangeListener pageListener, int maxWeeks) {
        this.mContext = context;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mMaxWeeks = maxWeeks;
        this.mUserPageListener = pageListener;
        setAttributeSet(attributes);
        this.mViews = initViews();
    }

    @Override
    public int getCount() {
        return mViews.length;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        T currentView;
        if (mViews.length > position && position >= 0) {
            currentView = getViewForPosition(position);
        } else {
            return null;
        }

        collection.addView(currentView);
        return currentView;
    }

    /**
     * Gets the {@link SlideView} for the current pager position, uses lazy initialization
     * to instantiate new views
     *
     * @param position The position in the pager
     * @return The existing slide view, or a new one
     */
    private T getViewForPosition(int position) {
        if (mViews == null) {
            this.mViews = initViews();
        }
        if (position < 0 || position > mViews.length - 1) {
            return null;
        }

        T currentView = mViews[position];

        if (currentView == null) {
            currentView = getWeekSlide(position, mWeeks);
            mViews[position] = currentView;
        }

        return currentView;
    }

    @Override
    public void destroyItem(ViewGroup collection, final int position, Object view) {
        collection.removeView((View) view);
//        mViews[position] = null;
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
    public T getCurrentView(int position) {
        if (mViews == null || position < 0 || position > mViews.length - 1) {
            return null;
        }

        return getViewForPosition(position);
    }

    /**
     * Initializes an array the size of the number of weeks between the to parameters.
     * Throws {@link IllegalArgumentException} if startDate is after endDate
     *
     * @return The number of weeks between the two dates
     */
    protected abstract T[] initViews();

    /**
     * Initializes a new {@link SlideView} and sets its
     * {@link OnSlideListener}
     *
     * @return
     */
    protected abstract T getWeekSlide(int pagePosition, int weeks);


    /**
     * Returns the left text for the {@link SlideView}
     *
     * @return
     */
    public String getLeftText(int pagePosition, int totalWeeks) {
        return Utilities.getWeekOfText(pagePosition, totalWeeks, mMaxWeeks);
    }

    /**
     * Returns the right text for the {@link SlideView}
     *
     * @return
     */
    public String getRightText(int weeksSince) {
        return Utilities.getWeekRangeText(mEndDate, weeksSince);
    }

    /**
     * External setter for {@link #mUserPageListener}
     *
     * @param userPageListener The new {@link OnSlidePageChangeListener}
     */
    public void setUserPageListener(OnSlidePageChangeListener userPageListener) {
        this.mUserPageListener = userPageListener;
    }
}
