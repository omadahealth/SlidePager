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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.interfaces.PageChildInterface;
import com.github.omadahealth.slidepager.lib.views.DayProgressView;
import com.github.omadahealth.slidepager.lib.views.WeekSlideView;

import java.util.List;

/**
 * Created by oliviergoutay and stoyand on 4/1/15.
 */
public class SlidePager extends ViewPager {
    /**
     * A user defined {@link OnPageChangeListener} that can
     * be added to {@link #setOnPageChangeListener(OnPageChangeListener)}. The default page listener
     * is defined implemented by this class and set in {@link #setSlidePager()}
     */
    private OnSlidePageChangeListener mUserPageListener;

    /**
     * The default animation time
     */
    private static final int DEFAULT_PROGRESS_ANIMATION_TIME = 1000;

    /**
     * The animation time in milliseconds that we animate the progress
     */
    private int mProgressAnimationTime = DEFAULT_PROGRESS_ANIMATION_TIME;

    /**
     * True if we should start at the last position in the {@link SlidePagerAdapter}
     */
    private boolean mStartAtEnd;

    /**
     * The attributes that this view is initialized with; need them to init the
     * child {@link DayProgressView}s
     */
    private TypedArray mAttributes;

    public SlidePager(Context context) {
        this(context, null);
    }

    public SlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadStyledAttributes(attrs, 0);
        setSlidePager();

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (!(adapter instanceof SlidePagerAdapter)) {
            throw new IllegalArgumentException("PagerAdapter should be a subclass of SlidePagerAdapter");
        }

        super.setAdapter(adapter);

        if (mStartAtEnd) {
            int position = adapter.getCount() - 1;
            setCurrentItem(position >= 0 ? position : 0);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        if (!(listener instanceof OnSlidePageChangeListener)) {
            throw new IllegalArgumentException("OnPageChangeListener should be a subclass of OnSlidePageChangeListener");
        }

        mUserPageListener = (OnSlidePageChangeListener) listener;
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        if (!(transformer instanceof SlideTransformer)) {
            throw new IllegalArgumentException("Transformer should be a subclass of SlideTransformer");
        }

        if (getAdapter() == null) {
            super.setPageTransformer(reverseDrawingOrder, transformer);
            return;
        }

        if (getAdapter() instanceof SlidePagerAdapter) {
            transformer.transformPage(((SlidePagerAdapter) getAdapter()).getCurrentView(getCurrentItem()), 0);
            super.setPageTransformer(reverseDrawingOrder, transformer);
        }
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attrs        The attributes to read from
     * @param defStyleAttr The styles to read from
     */
    private void loadStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            mAttributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SlidePager,
                    defStyleAttr, 0);
            mStartAtEnd = mAttributes.getBoolean(R.styleable.SlidePager_slide_progress_start_at_end, false);
        }
    }

    /**
     * Sets the {@link #setOnPageChangeListener(OnPageChangeListener)} for this class. If the user wants
     * to link their own {@link android.support.v4.view.ViewPager.OnPageChangeListener} then they should
     * use {@link #setOnPageChangeListener(OnPageChangeListener)}; where we set {@link #mUserPageListener}
     */
    private void setSlidePager() {
        super.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mUserPageListener != null) {
                    mUserPageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageSelected(int position) {
                if (mUserPageListener != null) {
                    mUserPageListener.onPageSelected(position);
                }
                resetPage(position);

            }


            @Override
            public void onPageScrollStateChanged(int state) {
                //page scrolled with room: drag, settle, idle
                //page scrolled when no room: drag, idle
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        animateSeries(getChildren(getCurrentItem()), false);

                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:

                        break;
                    case ViewPager.SCROLL_STATE_IDLE://animate here onPageSelected not called when we hit a wall
                        animatePage(getCurrentItem());
                        break;
                }

                if (mUserPageListener != null) {
                    mUserPageListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    /**
     * Resets the {@link DayProgressView} attributes to o progress and uncompleted colors.
     * Hides the series with {@link #animateSeries(List, boolean)}
     *
     * @param position The position of the page to reset
     */
    @SuppressWarnings("unchecked")
    private void resetPage(int position) {
        View selectedView = ((SlidePagerAdapter) getAdapter()).getCurrentView(position);
        ((WeekSlideView) selectedView).loadStyledAttributes(mAttributes);
        final List<View> children = (List<View>) selectedView.getTag();
        animateSeries(children, false);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    DayProgressView dayProgressView = (DayProgressView) child;
                    dayProgressView.reset();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void animatePage(int position) {
        View selectedView = ((SlidePagerAdapter) getAdapter()).getCurrentView(position);
        final List<View> children = (List<View>) selectedView.getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    ((PageChildInterface) child).loadStyledAttributes(mAttributes);
                    animateProgress((DayProgressView) child, children);
                }
            }
        }
    }

    private void animateProgress(DayProgressView view, List<View> children) {
        if (mUserPageListener != null) {
            int progress = mUserPageListener.getDayProgress(view.getIntTag());
            view.animateProgress(0, progress, mProgressAnimationTime, children);
        }
    }

    /**
     * @param children
     */
    private void animateSeries(List<View> children, boolean show) {
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    final DayProgressView dayProgressView = (DayProgressView) child;
                    dayProgressView.showStreak(show, DayProgressView.STREAK.RIGHT_STREAK);
                    dayProgressView.showStreak(show, DayProgressView.STREAK.LEFT_STREAK);
                }
            }
        }
    }

    /**
     * Refreshes the page animation
     */
    public void refreshPage() {
        animatePage(getCurrentItem());
    }

    @SuppressWarnings("unchecked")
    private List<View> getChildren(int position) {
        View selectedView = (((SlidePagerAdapter) getAdapter()).getCurrentView(position));

        return (List<View>) selectedView.getTag();
    }

    /**
     * Gets the animation duration
     *
     * @return Time in milliseconds
     */
    public int getProgressAnimationTime() {
        return mProgressAnimationTime;
    }

    /**
     * Sets the animation duration
     *
     * @param duration Time in milliseconds
     */
    public void setProgressAnimationTime(int duration) {
        this.mProgressAnimationTime = duration;
    }

}
