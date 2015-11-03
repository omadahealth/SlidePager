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
import android.databinding.BindingAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.omadahealth.slidepager.lib.adapter.AbstractSlidePagerAdapter;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.views.AbstractSlideView;
import com.github.omadahealth.slidepager.lib.views.ProgressView;

import java.util.List;

/**
 * Created by oliviergoutay and stoyand on 4/1/15.
 */
public class SlidePager extends ViewPager {
    /**
     * The tag for logging
     */
    private static final String TAG = "SlidePager";

    /**
     * A user defined {@link OnPageChangeListener} that can
     * be added to {@link #setOnPageChangeListener(OnPageChangeListener)}. The default page listener
     * is defined implemented by this class and set in {@link #setSlidePager()}
     */
    private OnSlidePageChangeListener mUserPageListener;

    /**
     * Indicates if the user configured the style to be reanimating each time we are scrolling the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     * or not.
     */
    protected boolean mHasToReanimate;

    /**
     * True if we should start at the last position in the {@link AbstractSlidePagerAdapter}
     */
    private boolean mStartAtEnd;

    /**
     * The attributes that this view is initialized with; need them to init the
     * child {@link ProgressView}s
     */
    private TypedArray mAttributes;

    /**
     * The page index saved to load the {@link com.github.omadahealth.slidepager.lib.views.SlideView} only once on
     * {@link android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)}
     */
    private int mPageIndex;

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
        if (!(adapter instanceof AbstractSlidePagerAdapter)) {
            throw new IllegalArgumentException("PagerAdapter should be a subclass of AbstractSlidePagerAdapter");
        }

        super.setAdapter(adapter);

        if (mStartAtEnd) {
            int position = adapter.getCount() - 1;
            setCurrentItem(position >= 0 ? position : 0);
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return i;
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

        if (getAdapter() instanceof AbstractSlidePagerAdapter) {
            transformer.transformPage(((AbstractSlidePagerAdapter) getAdapter()).getCurrentView(getCurrentItem()), 0);
            super.setPageTransformer(reverseDrawingOrder, transformer);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "SlidePager click :" + (ev.getAction() & MotionEvent.ACTION_MASK));
        onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    @BindingAdapter("sp_slide_show_circular_bars")
    public static void showCircularBars(SlidePager slidePager, Boolean visible) {
        if (visible == null) {
            visible = true;
        }
        ProgressView.setShowCircularBar(visible);
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attrs        The attributes to read from
     * @param defStyleAttr The styles to read from
     */
    private void loadStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            setAttributeSet(getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SlidePager,
                    defStyleAttr, 0));
            mStartAtEnd = mAttributes.getBoolean(R.styleable.SlidePager_slide_start_at_end, false);
            mHasToReanimate = mAttributes.getBoolean(R.styleable.SlidePager_slide_pager_reanimate_slide_view, true);
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
                mPageIndex = position;
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
                        animateStreaks(getCurrentItem(), false);
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE://animate here onPageSelected not called when we hit a wall
                        Log.e(TAG, "SCROLL_STATE_IDLE for page index : " + getCurrentItem() + " mPageIndex : " + mPageIndex);
                        if (getCurrentItem() == mPageIndex) {
                            animatePage(getCurrentItem());
                        }
                        break;
                }

                if (mUserPageListener != null) {
                    mUserPageListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    /**
     * Resets the {@link ProgressView} attributes to o progress and uncompleted colors.
     * Hides the series with {@link #animateStreaks(int, boolean)}
     *
     * @param position The position of the page to reset
     */
    @SuppressWarnings("unchecked")
    private void resetPage(int position) {
        if (getAdapter() != null) {
            AbstractSlideView slideView = ((AbstractSlidePagerAdapter) getAdapter()).getCurrentView(position);
            if (slideView != null) {
                if (!slideView.hasAnimated() || mHasToReanimate) {
                    slideView.resetPage(mAttributes);
                }
            }
        }
    }

    /**
     * @param position
     */
    private void animatePage(int position) {
        AbstractSlideView slideView = ((AbstractSlidePagerAdapter) getAdapter()).getCurrentView(position);
        if (!slideView.hasAnimated() || mHasToReanimate) {
            slideView.animatePage(mUserPageListener, mAttributes);
        } else {
            slideView.animateStreaks();
        }
    }

    /**
     * @param position
     * @param show
     */
    private void animateStreaks(int position, boolean show) {
        AbstractSlideView slideView = ((AbstractSlidePagerAdapter) getAdapter()).getCurrentView(position);
        slideView.resetStreaks(show);
    }

    /**
     * Returns the initiated child views in {@link SlideTransformer#initTags(View)}
     *
     * @param position
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<View> getChildren(int position) {
        View selectedView = (((AbstractSlidePagerAdapter) getAdapter()).getCurrentView(position));


        if (selectedView.getTag() == null) {
            SlideTransformer.initTags(selectedView);
        }

        return (List<View>) selectedView.getTag(R.id.slide_transformer_tag_key);
    }

    /**
     * Refreshes the page animation
     */
    public void refreshPage() {
        animatePage(getCurrentItem());
    }

    /**
     * Gets the attribute set that we pass from xml attr in {@link R.styleable#SlidePager}
     * in {@link #loadStyledAttributes(AttributeSet, int)}
     *
     * @return
     */
    public TypedArray getAttributeSet() {
        return mAttributes;
    }

    /**
     * Sets the {@link #mAttributes} that are passed on to the {@link ProgressView} and {@link android.transition.Slide}
     *
     * @param attributeSet
     */

    public void setAttributeSet(TypedArray attributeSet) {
        this.mAttributes = attributeSet;
    }
}
