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
package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/2/15.
 */
public class DayProgressView extends RelativeLayout {
    /**
     * The tag for logging
     */
    private static final String TAG = "DayProgressView";

    /**
     * The left streak
     */
    private ImageView mLeftStreak;

    /**
     * The right streak
     */
    private ImageView mRightStreak;

    /**
     * The circular progress bar
     */
    private CircularBar mCircularBar;

    /**
     * The textview that shows today's day name
     */
    private TypefaceTextView mDayOfWeek;

    /**
     * The duration of the easing in of {@link #mLeftStreak} and {@link #mRightStreak}
     */
    private static final int EASE_IN_DURATION = 500;

    /**
     * For save and restore instance of progressbar
     */
    private static final String INSTANCE_STATE = "saved_instance";

    /**
     * The default fill color for {@link #mCircularBar} when progress is 100
     */
    private int mCompletedFillColor;

    /**
     * The default fill color for {@link #mCircularBar} when progress is below 100
     */
    private int mNotCompletedFillColor;

    /**
     * The progress color for {@link #mCircularBar} when progress is 100
     */
    private int mCompletedColor;

    /**
     * The progress color for {@link #mCircularBar} when progress is below 100
     */
    private int mNotCompletedColor;

    /**
     * The progress color for {@link #mCircularBar} when it is today's date
     */
    private int mTodayColor;

    /**
     * The days in a week
     */
    private static String[] mWeekDays;

    /**
     * The sibling {@link DayProgressView} of this class
     */
    private List<View> mSiblings;

    /**
     * Boolean that controls if the {@link #mLeftStreak} and  {@link #mRightStreak} showed
     * be shown
     */
    private boolean mShowStreaks;

    public enum STREAK {
        LEFT_STREAK,
        RIGHT_STREAK
    }

    public DayProgressView(Context context) {
        this(context, null);
    }

    public DayProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        injectViews(context);
//        loadStyledAttributes(attrs, defStyleAttr);
        initAnimations();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
//        bundle.putBoolean(INSTANCE_START_LINE_ENABLED, isStartLineEnabled());
//        bundle.putFloat(INSTANCE_CLOCKWISE_REACHED_BAR_HEIGHT, getClockwiseReachedArcWidth());
//        bundle.putFloat(INSTANCE_CLOCKWISE_OUTLINE_BAR_HEIGHT, getClockwiseOutlineArcWidth());
//        bundle.putInt(INSTANCE_CLOCKWISE_REACHED_BAR_COLOR, getClockwiseReachedArcColor());
//        bundle.putInt(INSTANCE_CLOCKWISE_OUTLINE_BAR_COLOR, getClockwiseOutlineArcColor());
//        bundle.putFloat(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_HEIGHT, getCounterClockwiseReachedArcWidth());
//        bundle.putFloat(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_HEIGHT, getCounterClockwiseOutlineArcWidth());
//        bundle.putInt(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_COLOR, getCounterClockwiseReachedArcColor());
//        bundle.putInt(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_COLOR, getCounterClockwiseOutlineArcColor());
//        bundle.putBoolean(INSTANCE_CIRCLE_FILL_ENABLED, isCircleFillEnabled());
//        bundle.putInt(INSTANCE_CIRCLE_FILL_COLOR, getCircleFillColor());
//        bundle.putInt(INSTANCE_MAX, getMax());
//        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
//        bundle.putString(INSTANCE_SUFFIX, getSuffix());
//        bundle.putString(INSTANCE_PREFIX, getPrefix());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
//            mStartLineEnabled = bundle.getBoolean(INSTANCE_START_LINE_ENABLED);
//            mClockwiseReachedArcWidth = bundle.getFloat(INSTANCE_CLOCKWISE_REACHED_BAR_HEIGHT);
//            mClockwiseOutlineArcWidth = bundle.getFloat(INSTANCE_CLOCKWISE_OUTLINE_BAR_HEIGHT);
//            mClockwiseArcColor = bundle.getInt(INSTANCE_CLOCKWISE_REACHED_BAR_COLOR);
//            mClockwiseOutlineArcColor = bundle.getInt(INSTANCE_CLOCKWISE_OUTLINE_BAR_COLOR);
//            mCounterClockwiseReachedArcWidth = bundle.getFloat(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_HEIGHT);
//            mCounterClockwiseOutlineArcWidth = bundle.getFloat(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_HEIGHT);
//            mCounterClockwiseArcColor = bundle.getInt(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_COLOR);
//            mCounterClockwiseOutlineArcColor = bundle.getInt(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_COLOR);
//            mCircleFillEnabled = bundle.getBoolean(INSTANCE_CIRCLE_FILL_ENABLED);
//            mCircleFillColor = bundle.getInt(INSTANCE_CIRCLE_FILL_COLOR);
//            mCircleFillMode = bundle.getInt(INSTANCE_CIRCLE_FILL_MODE);
//            initializePainters();
//            setMax(bundle.getInt(INSTANCE_MAX));
//            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
//            setPrefix(bundle.getString(INSTANCE_PREFIX));
//            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Initiate the view and start butterknife injection
     *
     * @param context
     */
    private void injectViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_day_progress, this);
        ButterKnife.inject(this, view);

        mLeftStreak = ButterKnife.findById(this, R.id.day_progress_streak_left);
        mRightStreak = ButterKnife.findById(this, R.id.day_progress_streak_right);
        mCircularBar = ButterKnife.findById(this, R.id.circularbar);
        mDayOfWeek = ButterKnife.findById(this, R.id.day_of_week);
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attributes The attributes to read from, do not pass {@link AttributeSet} as inflation needs the context of the {@link android.support.v4.view.PagerAdapter}
     */
    public void loadStyledAttributes(TypedArray attributes) {
        if (attributes != null) {
            Resources res = getContext().getResources();
            mWeekDays = res.getStringArray(R.array.week_days);
            mShowStreaks = attributes.getBoolean(R.styleable.SlidePager_slide_progress_show_streaks, true);
            mCompletedFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_completed_fill_color, res.getColor(R.color.default_progress_completed_fill_color));
            mNotCompletedFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_fill_color, res.getColor(R.color.default_progress_not_completed_fill_color));
            mCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_completed_color, res.getColor(R.color.default_progress_completed_color));
            mNotCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_color, res.getColor(R.color.default_progress_not_completed_color));
            mTodayColor = attributes.getColor(R.styleable.SlidePager_slide_progress_today_color, res.getColor(R.color.default_progress_today_color));

            setDayText();
            //Do not recycle attributes, we need them for the future views
        }
    }

    private void initAnimations() {
        final int index = getIntTag();
        addAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (getCircularBar().getProgress() >= 99.95f) {

                    if(mShowStreaks && mSiblings != null && mSiblings.size() > 0){
                        //Previous exists
                        if (getIntTag() - 1 >= 0) {
                            View previousDay = mSiblings.get(index - 1);
                            if(previousDay instanceof DayProgressView){
                                //Previous is complete
                                if (((DayProgressView)previousDay).getCircularBar().getProgress() >= 99.95f) {
                                    showStreak(true, DayProgressView.STREAK.LEFT_STREAK);
                                }
                            }

                        }

                        //Next exists
                        if (index + 1 < mSiblings.size()) {
                            View nextDay = mSiblings.get(index + 1);
                            if(nextDay instanceof DayProgressView){
                                //Next is complete
                                if (((DayProgressView)nextDay).getCircularBar().getProgress() >= 99.95f) {
                                    showStreak(true, DayProgressView.STREAK.RIGHT_STREAK);
                                }
                            }
                        }
                    }

                    //Set Color
                    mCircularBar.setCircleFillColor(mCompletedFillColor);
                    mCircularBar.setClockwiseReachedArcColor(mCompletedColor);
                } else {
                    //Set Color
                    mCircularBar.setCircleFillColor(mNotCompletedFillColor);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * Sets the text for the {@link #mDayOfWeek}
     */
    private void setDayText() {
        getDayOfWeek().setText(mWeekDays[getIntTag()]);
    }

    /**
     * Animate the progress from start to end for the {@link CircularBar} and the rest of the views in
     * this container
     *
     * @param start 0-100
     * @param end   0-100
     * @param duration The duration in milliseconds of the animation
     * @param siblings The sibling views we use to evaluate streaks showing
     */
    public void animateProgress(int start, int end, int duration, List<View> siblings) {
        mSiblings = siblings;
        mCircularBar.setClockwiseReachedArcColor(end == 100 ? mCompletedColor : mNotCompletedColor);
        mCircularBar.animateProgress(start, end, duration);
    }

    /**
     * Calls {@link #animateProgress(int, int, int, List)} with showing streaks set to false
     */
    public void animateProgress(int start, int end, int duration) {
        mShowStreaks = false;
        mCircularBar.setClockwiseReachedArcColor(end == 100 ? mCompletedColor : mNotCompletedColor);
        mCircularBar.animateProgress(start, end, duration);
    }

    /**
     * Show or hide the streaks between the view
     *
     * @param show True if to show, false otherwise
     * @param side The side to animate and change visibility
     */
    public void showStreak(final boolean show, STREAK side) {
        AnimatorSet set = new AnimatorSet();
        View sideView = null;
        switch (side) {
            case LEFT_STREAK:
                sideView = mLeftStreak;
                break;
            case RIGHT_STREAK:
                sideView = mRightStreak;
                break;
            default:
                return;
        }
        float start = show ? 0 : 1f;
        float end = show ? 1f : 0;
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, EASE_IN_DURATION, ObjectAnimator.ofFloat(sideView, "alpha", start, end)));
        set.setDuration(EASE_IN_DURATION);
        final View finalSideView = sideView;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (show) {
                    finalSideView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show) {
                    finalSideView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    public void addAnimationListener(Animator.AnimatorListener listener) {
        mCircularBar.addListener(listener);
    }

    public TypefaceTextView getDayOfWeek() {
        return mDayOfWeek;
    }

    public CircularBar getCircularBar() {
        return mCircularBar;
    }

    public Integer getIntTag() {
        return Integer.parseInt((String) getTag());
    }
}
