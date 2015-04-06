package com.github.omadahealth.slidepager.lib;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.github.omadahealth.slidepager.views.DayProgressView;
import com.nineoldandroids.animation.Animator;

import java.util.List;

/**
 * Created by oliviergoutay on 4/1/15.
 */
public class SlidePager extends ViewPager implements ViewPager.OnPageChangeListener {
    /**
     * A user defined {@link android.support.v4.view.ViewPager.OnPageChangeListener} that can
     * be added to {@link #setOnPageChangeListener(OnPageChangeListener)}. The default page listener
     * is defined implemented by this class and set in {@link #setSlidePager(OnPageChangeListener)}
     */
    private OnSlidePageChangeListener mUserPageListener;

    private static final String[] days = {"S", "M", "T", "W", "T", "F", "S"};

    /**
     * The default animation time
     */
    private static final int DEFAULT_PROGRESS_ANIMATION_TIME = 1000;

    /**
     * The animation time in milliseconds that we animate the progress
     */
    private int mProgressAnimationTime = DEFAULT_PROGRESS_ANIMATION_TIME;

    /**
     * The color that is set to fill the {@link com.github.OrangeGangsters.circularbarpager.library.CircularBar} at the end of the animation if the progress is 100%
     */
    private int mFillColor;

    /**
     * The color that is set to draw the line of the {@link com.github.OrangeGangsters.circularbarpager.library.CircularBar} at the end of the animation if the progress is 100%
     */
    private int mCompletedColor;

    /**
     * The color that is set to draw the line of the {@link com.github.OrangeGangsters.circularbarpager.library.CircularBar} at the end of the animation if the progress is less than 100%
     */
    private int mNotCompletedColor;

    /**
     * The color that is set to draw the line of the {@link com.github.OrangeGangsters.circularbarpager.library.CircularBar} if it represents today
     */
    private int mTodayColor;

    public SlidePager(Context context) {
        this(context, null);
    }

    public SlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadStyledAttributes(attrs, 0);
        setSlidePager(this);

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (!(adapter instanceof SlidePagerAdapter)) {
            throw new IllegalArgumentException("PagerAdapter should be a subclass of SlidePagerAdapter");
        }

        super.setAdapter(adapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
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
            transformer.transformPage(((SlidePagerAdapter) getAdapter()).getCurrentView(0), 0);
            super.setPageTransformer(reverseDrawingOrder, transformer);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mUserPageListener != null) {
            mUserPageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(final int position) {
        animatePage(position);

        if (mUserPageListener != null) {
            mUserPageListener.onPageSelected(position);
        }
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attrs        The attributes to read from
     * @param defStyleAttr The styles to read from
     */
    public void loadStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            final TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SlidePager,
                    defStyleAttr, 0);
            Resources res = getContext().getResources();
            mFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_fill_color, res.getColor(R.color.circle_fill_color));
            mCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_completed_color, res.getColor(R.color.green_color));
            mNotCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_color, res.getColor(R.color.dark_gray));
            mTodayColor = attributes.getColor(R.styleable.SlidePager_slide_progress_today_color, res.getColor(R.color.green_color));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {

            View selectedView = (((SlidePagerAdapter) getAdapter()).getCurrentView(getCurrentItem()));

            List<View> children = (List<View>) selectedView.getTag();
            animateSeries(children);
        }
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            onPageSelected(getCurrentItem());
        }

        if (mUserPageListener != null) {
            mUserPageListener.onPageScrollStateChanged(state);
        }
    }

    @SuppressWarnings("unchecked")
    private void animatePage(final int position) {
        View selectedView = ((SlidePagerAdapter) getAdapter()).getCurrentView(getCurrentItem());
        final List<View> children = (List<View>) selectedView.getTag();
        if (children != null) {
            int i = 0;
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    final int index = i;
                    final DayProgressView dayProgressView = (DayProgressView) child;
                    dayProgressView.getDayOfWeek().setText(days[i]);
                    dayProgressView.addAnimationListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (position == getCurrentItem() && dayProgressView.getCircularBar().getProgress() >= 99.95f) {

                                //Previous exists
                                if (index - 1 >= 0) {
                                    //Previous is complete
                                    DayProgressView previousDay = (DayProgressView) children.get(index - 1);
                                    if (previousDay.getCircularBar().getProgress() >= 99.95f) {
                                        dayProgressView.show(true, DayProgressView.STREAK.LEFT_STREAK);
                                    }
                                }

                                //Next exists
                                if (index + 1 < children.size()) {
                                    //Next is complete
                                    DayProgressView nextDay = (DayProgressView) children.get(index + 1);
                                    if (nextDay.getCircularBar().getProgress() >= 99.95f) {
                                        dayProgressView.show(true, DayProgressView.STREAK.RIGHT_STREAK);
                                    }
                                }

                                //Set Color
                                dayProgressView.getCircularBar().setCircleFillColor(mFillColor);
                                dayProgressView.getCircularBar().setClockwiseReachedArcColor(mCompletedColor);
                        }
                    }

                    @Override
                    public void onAnimationCancel (Animator animation){
                    }

                    @Override
                    public void onAnimationRepeat (Animator animation){
                    }
                });

                animateProgress(dayProgressView, i);

                i++;
            }
        }
    }

}

    private void animateProgress(DayProgressView view, int index) {
        if (mUserPageListener != null) {
            int progress = mUserPageListener.getSlideProgress(index);

            //Init colors
            view.getCircularBar().setCircleFillColor(getResources().getColor(android.R.color.transparent));
            view.getCircularBar().setClockwiseReachedArcColor(mNotCompletedColor);

            view.animateProgress(0, progress, mProgressAnimationTime);
        }
    }

    private void animateSeries(List<View> children) {
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    final DayProgressView dayProgressView = (DayProgressView) child;
                    dayProgressView.show(false, DayProgressView.STREAK.RIGHT_STREAK);
                    dayProgressView.show(false, DayProgressView.STREAK.LEFT_STREAK);
                }
            }
        }
    }


    private void setSlidePager(ViewPager.OnPageChangeListener listener) {
        super.setOnPageChangeListener(listener);
    }


    public int getProgressAnimationTime() {
        return mProgressAnimationTime;
    }

    public void setProgressAnimationTime(int duration) {
        this.mProgressAnimationTime = duration;
    }

}
