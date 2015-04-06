package com.github.omadahealth.slidepager.lib;

import android.content.Context;
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
    private ViewPager.OnPageChangeListener mUserPageListener;

    private static final String[] days = {"S", "M", "T", "W", "T", "F", "S"};

    /**
     * The animation time in milliseconds that we take to display the steps taken
     */
    private static final int BAR_ANIMATION_TIME = 1000;

    public SlidePager(Context context) {
        this(context, null);
    }

    public SlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        mUserPageListener = listener;
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
    }

    @Override
    public void onPageSelected(final int position) {
        animatePage(position);
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
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });

                    animateProgress(dayProgressView, i);

                    i++;
                }
            }
        }
    }

    private void animateProgress(DayProgressView view, int i) {
        switch (view.getId()) {
            case R.id.day_progress_3:
            case R.id.day_progress_4:
            case R.id.day_progress_5:
                view.animateProgress(0, 100, BAR_ANIMATION_TIME);
                break;
            default:
                view.animateProgress(0, (25 * i), BAR_ANIMATION_TIME);
                view.getCircularBar().setCircleFillColor(getResources().getColor(android.R.color.transparent));
                break;
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
}
