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
import com.github.omadahealth.slidepager.lib.views.SlideView;

import java.util.Date;

/**
 * Created by dae.park on 10/5/15.
 */
public class SlideBarChartPagerAdapter extends AbstractSlidePagerAdapter<SlideBarChartView> {
    /**
     * The maximum number of weeks before we change the {@link #getLeftText(int, int)}
     */
    private static final int DEFAULT_MAX_WEEKS = 10;

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
    private SlideBarChartView[] mViews;

    /**
     * Weeks that this view represents
     */
    private int mWeeks;

    /**
     * The weeks in the program
     */
    private final int mMaxWeeks;

    /**
     * A user defined {@link ViewPager.OnPageChangeListener}
     */
    private OnSlidePageChangeListener mUserPageListener;

    /**
     * The attribute set from xml
     */
    private TypedArray mAttributeSet;

    public void setAttributeSet(TypedArray attributeSet) {
        this.mAttributeSet = attributeSet;
    }

    /**
     * Calls {@link #SlideBarChartPagerAdapter(Context, Date, Date)} with the end date being set
     * to today
     *
     * @param context   The context
     * @param startDate The start {@link Date} for computing the number of weeks
     */
    public SlideBarChartPagerAdapter(Context context, Date startDate) {
        this(context, startDate, new Date());
    }

    /**
     * Public constructors to the {@link SlideChartPagerAdapter}
     *
     * @param context   The context
     * @param startDate The start {@link Date} for computing the number of weeks
     * @param endDate   The end {@link Date} for computing the number of weeks
     */
    public SlideBarChartPagerAdapter(Context context, Date startDate, Date endDate) {
        this(context, startDate, endDate, null, null, -1);

    }

    public SlideBarChartPagerAdapter(Context context, Date startDate, Date endDate, TypedArray attributes, OnSlidePageChangeListener pageListener, int maxWeeks) {
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
        SlideBarChartView currentView;
        if (mViews.length > position) {
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
    private SlideBarChartView getViewForPosition(int position) {
        if (mViews == null) {
            this.mViews = initViews();
        }

        SlideBarChartView currentView = mViews[position];

        if (currentView == null) {
            currentView = getWeekSlide(position, mWeeks);
            mViews[position] = currentView;
        }

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

    @Override
    public SlideBarChartView getCurrentView(int position) {
        if (mViews == null || position > mViews.length - 1) {
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
    private SlideBarChartView[] initViews() {
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
    private SlideBarChartView getWeekSlide(int pagePosition, int weeks) {
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
     * Returns the left text for the {@link SlideView}
     *
     * @return
     */
    private String getLeftText(int pagePosition, int totalWeeks) {

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
}
