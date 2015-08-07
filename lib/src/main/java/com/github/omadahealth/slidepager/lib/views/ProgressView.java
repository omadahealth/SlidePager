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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/2/15.
 */
public class ProgressView extends RelativeLayout {
    /**
     * The tag for logging
     */
    private static final String TAG = "ProgressView";

    /**
     * The left streak
     */
    private ImageView mLeftStreak;

    /**
     * The right streak
     */
    private ImageView mRightStreak;

    /**
     * The completed check mark that goes inside {@link #mCircularBar}
     */
    private ImageView mCheckMark;

    /**
     * The circular progress bar
     */
    private CircularBar mCircularBar;

    /**
     * The textview that shows today's day name
     */
    private TypefaceTextView mProgressText;

    /**
     * The default {@link #mProgressText} is initialized in its attrs
     */
    private TypefaceType mDefaultProgressTypeface;

    /**
     * The duration of the easing in of {@link #mLeftStreak} and {@link #mRightStreak}
     */
    private static final int EASE_IN_DURATION = 350;

    /**
     * For save and restore instance of progressbar
     */
    private static final String INSTANCE_STATE = "saved_instance";

    /**
     * The color of the progress already achieved for {@link #mCircularBar}
     */
    private int mReachColor;

    /**
     * The fill color for {@link #mCircularBar}
     */
    private int mFillColor;

    /**
     * The default outline color for {@link #mCircularBar}
     */
    private int mOutlineColor;

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
     * The progress reached color for {@link #mCircularBar} when progress is below 100
     */
    private int mNotCompletedReachColor;

    /**
     * The progress color outline for {@link #mCircularBar} when progress is below 100
     */
    private int mNotCompletedOutlineColor;

    /**
     * The progress size outline for {@link #mCircularBar} when progress is below 100
     */
    private float mNotCompletedOutlineSize;

    /**
     * The progress size outline for {@link #mCircularBar} when this day is in the future
     */
    private float mNotCompletedFutureOutlineSize;

    /**
     * The progress reached color for {@link #mCircularBar} when it is today's date
     */
    private int mSpecialReachColor;

    /**
     * The progress color outline for {@link #mCircularBar} when it is today's date
     */
    private int mSpecialOutlineColor;

    /**
     * The progress fill color for today's date
     */
    private int mSpecialFillColor;

    /**
     * The width of the reached progress
     */
    private float mReachedWidth;

    /**
     * The strings that we set in {@link #mProgressText}
     */
    private static String[] mProgressStrings;

    /**
     * The sibling {@link ProgressView} of this class
     */
    private List<ProgressView> mSiblings;

    /**
     * Boolean that controls if the {@link #mLeftStreak} and  {@link #mRightStreak} showed
     * be shown
     */
    private boolean mShowStreaks;

    /**
     * Boolean that controls if the {@link #mProgressText} is visible or not
     */
    private boolean mShowProgressText;

    /**
     * Boolean that controls if the {@link #mCheckMark} is visible or not
     */
    private boolean mShowProgressPlusMark;

    /**
     * Boolean that controls if we should use the special today colors for this view
     */
    private boolean mIsSpecial;

    /**
     * Boolean that controls if we should use the future size for this view
     */
    private boolean mIsFuture;

    /**
     * The saved attributes coming from {@link SlideChartView} and {@link SlideView}
     */
    private TypedArray mAttributes;

    /**
     * The saved {@link ProgressAttr} coming from {@link SlideChartView} and {@link SlideView}
     */
    private ProgressAttr mProgressAttr;

    /**
     * The left and right streaks
     */
    public enum STREAK {
        LEFT_STREAK,
        RIGHT_STREAK
    }

    private static String INSTANCE_SHOW_STREAKS = "show_streaks";
    private static String INSTANCE_SHOW_PROGRESS_TEXT = "show_progress_text";
    private static String INSTANCE_SHOW_PROGRESS_PLUSMARK = "show_progress_plusmark";
    private static String INSTANCE_COMPLETED_COLOR = "completed_color";
    private static String INSTANCE_COMPLETED_FILL_COLOR = "completed_fill_color";
    private static String INSTANCE_NOT_COMPLETED_COLOR = "not_completed_color";
    private static String INSTANCE_NOT_COMPLETED_OUTLINE_COLOR = "not_completed_outline_color";
    private static String INSTANCE_NOT_COMPLETED_OUTLINE_SIZE = "not_completed_outline_size";
    private static String INSTANCE_NOT_COMPLETED_FUTURE_OUTLINE_SIZE = "not_completed_future_outline_size";
    private static String INSTANCE_NOT_COMPLETED_FILL_COLOR = "not_completed_fill_color";
    private static String INSTANCE_SPECIAL_COMPLETED_COLOR = "special_color";
    private static String INSTANCE_SPECIAL_COMPLETED_OUTLINE_COLOR = "special_outline_color";
    private static String INSTANCE_SPECIAL_COMPLETED_FILL_COLOR = "special_fill_color";
    private static String INSTANCE_REACHED_WIDTH = "reached_width";

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        if (isInEditMode()) {
            return;
        }
        injectViews(context);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_SHOW_STREAKS, mShowStreaks);
        bundle.putBoolean(INSTANCE_SHOW_PROGRESS_TEXT, mShowProgressText);
        bundle.putBoolean(INSTANCE_SHOW_PROGRESS_PLUSMARK, mShowProgressPlusMark);

        bundle.putInt(INSTANCE_COMPLETED_COLOR, mCompletedColor);
        bundle.putInt(INSTANCE_COMPLETED_FILL_COLOR, mCompletedFillColor);
        bundle.putInt(INSTANCE_NOT_COMPLETED_COLOR, mNotCompletedReachColor);
        bundle.putInt(INSTANCE_NOT_COMPLETED_OUTLINE_COLOR, mNotCompletedOutlineColor);
        bundle.putFloat(INSTANCE_NOT_COMPLETED_OUTLINE_SIZE, mNotCompletedOutlineSize);
        bundle.putFloat(INSTANCE_NOT_COMPLETED_FUTURE_OUTLINE_SIZE, mNotCompletedFutureOutlineSize);
        bundle.putInt(INSTANCE_NOT_COMPLETED_FILL_COLOR, mNotCompletedFillColor);
        bundle.putInt(INSTANCE_SPECIAL_COMPLETED_COLOR, mSpecialReachColor);
        bundle.putInt(INSTANCE_SPECIAL_COMPLETED_OUTLINE_COLOR, mSpecialOutlineColor);
        bundle.putInt(INSTANCE_SPECIAL_COMPLETED_FILL_COLOR, mSpecialFillColor);
        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            Resources res = getContext().getResources();
            mShowStreaks = bundle.getBoolean(INSTANCE_SHOW_STREAKS, true);
            mShowProgressText = bundle.getBoolean(INSTANCE_SHOW_PROGRESS_TEXT, true);
            mShowProgressPlusMark = bundle.getBoolean(INSTANCE_SHOW_PROGRESS_PLUSMARK, true);

            mCompletedColor = bundle.getInt(INSTANCE_COMPLETED_COLOR, res.getColor(R.color.default_progress_completed_reach_color));
            mCompletedFillColor = bundle.getInt(INSTANCE_COMPLETED_FILL_COLOR, res.getColor(R.color.default_progress_completed_fill_color));

            mNotCompletedReachColor = bundle.getInt(INSTANCE_NOT_COMPLETED_COLOR, res.getColor(R.color.default_progress_not_completed_reach_color));
            mNotCompletedOutlineColor = bundle.getInt(INSTANCE_NOT_COMPLETED_OUTLINE_COLOR, res.getColor(R.color.default_progress_not_completed_outline_color));
            mNotCompletedOutlineSize = bundle.getFloat(INSTANCE_NOT_COMPLETED_OUTLINE_SIZE, res.getDimension(R.dimen.circular_bar_default_outline_width));
            mNotCompletedFutureOutlineSize = bundle.getFloat(INSTANCE_NOT_COMPLETED_FUTURE_OUTLINE_SIZE, res.getDimension(R.dimen.circular_bar_default_future_outline_width));
            mNotCompletedFillColor = bundle.getInt(INSTANCE_NOT_COMPLETED_FILL_COLOR, res.getColor(R.color.default_progress_not_completed_fill_color));

            mSpecialReachColor = bundle.getInt(INSTANCE_SPECIAL_COMPLETED_COLOR, res.getColor(R.color.default_progress_special_reach_color));
            mSpecialOutlineColor = bundle.getInt(INSTANCE_SPECIAL_COMPLETED_OUTLINE_COLOR, res.getColor(R.color.default_progress_special_outline_color));
            mSpecialFillColor = bundle.getInt(INSTANCE_SPECIAL_COMPLETED_FILL_COLOR, res.getColor(R.color.default_progress_special_fill_color));

            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH, res.getDimension(R.dimen.default_progress_reached_width));

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
        View view = inflater.inflate(R.layout.view_progress, this);
        ButterKnife.inject(this, view);

        mLeftStreak = ButterKnife.findById(this, R.id.progress_streak_left);
        mRightStreak = ButterKnife.findById(this, R.id.progress_streak_right);
        mCheckMark = ButterKnife.findById(this, R.id.check_mark);
        mCircularBar = ButterKnife.findById(this, R.id.circularbar);
        mProgressText = ButterKnife.findById(this, R.id.progress_text);

        mDefaultProgressTypeface = mProgressText.getCurrentTypeface();

        mCircularBar.setStartLineEnabled(false);
        mProgressText.setTypeface(TypefaceTextView.getFont(context, TypefaceType.ROBOTO_LIGHT.getAssetFileName()));
        loadStyledAttributes(mAttributes, mProgressAttr);
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attributes The attributes to read from, do not pass {@link AttributeSet} as inflation needs the context of the {@link android.support.v4.view.PagerAdapter}
     */
    public ProgressView loadStyledAttributes(TypedArray attributes, ProgressAttr progress) {
        mAttributes = attributes;
        mProgressAttr = progress;

        mIsSpecial = progress == null ? false : progress.isSpecial();
        mIsFuture = progress == null ? false : progress.isFuture();

        Resources res = getContext().getResources();
        if (attributes != null) {
            mShowStreaks = attributes.getBoolean(R.styleable.SlidePager_slide_show_streaks, true);
            mShowProgressText = attributes.getBoolean(R.styleable.SlidePager_slide_show_progress_text, true);
            mShowProgressPlusMark = attributes.getBoolean(R.styleable.SlidePager_slide_show_progress_plusmark, true);

            mCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_completed_reach_color, res.getColor(R.color.default_progress_completed_reach_color));
            mCompletedFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_completed_fill_color, res.getColor(R.color.default_progress_completed_fill_color));

            mNotCompletedReachColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_reach_color, res.getColor(R.color.default_progress_not_completed_reach_color));
            mNotCompletedOutlineColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_outline_color, res.getColor(R.color.default_progress_not_completed_outline_color));
            mNotCompletedOutlineSize = attributes.getDimension(R.styleable.SlidePager_slide_progress_not_completed_outline_size, res.getDimension(R.dimen.circular_bar_default_outline_width));
            mNotCompletedFutureOutlineSize = attributes.getDimension(R.styleable.SlidePager_slide_progress_not_completed_future_outline_size, res.getDimension(R.dimen.circular_bar_default_future_outline_width));
            mNotCompletedFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_fill_color, res.getColor(R.color.default_progress_not_completed_fill_color));

            mSpecialReachColor = attributes.getColor(R.styleable.SlidePager_slide_progress_special_reach_color, res.getColor(R.color.default_progress_special_reach_color));
            mSpecialOutlineColor = attributes.getColor(R.styleable.SlidePager_slide_progress_special_outline_color, res.getColor(R.color.default_progress_special_outline_color));
            mSpecialFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_special_fill_color, res.getColor(R.color.default_progress_special_fill_color));

            mReachedWidth = attributes.getDimension(R.styleable.SlidePager_slide_progress_reached_width, res.getDimension(R.dimen.default_progress_reached_width));
            //Do not recycle attributes, we need them for the future views
        } else {
            mShowStreaks = true;
            mShowProgressText = true;
            mShowProgressPlusMark = true;

            mCompletedColor = res.getColor(R.color.default_progress_completed_reach_color);
            mCompletedFillColor = res.getColor(R.color.default_progress_completed_fill_color);

            mNotCompletedReachColor = res.getColor(R.color.default_progress_not_completed_reach_color);
            mNotCompletedOutlineColor = res.getColor(R.color.default_progress_not_completed_outline_color);
            mNotCompletedOutlineSize = res.getDimension(R.dimen.circular_bar_default_outline_width);
            mNotCompletedFutureOutlineSize = res.getDimension(R.dimen.circular_bar_default_future_outline_width);
            mNotCompletedFillColor = res.getColor(R.color.default_progress_not_completed_fill_color);

            mSpecialReachColor = res.getColor(R.color.default_progress_special_reach_color);
            mSpecialOutlineColor = res.getColor(R.color.default_progress_special_outline_color);
            mSpecialFillColor = res.getColor(R.color.default_progress_special_fill_color);

            mReachedWidth = res.getDimension(R.dimen.default_progress_reached_width);
        }
        loadProgressTextLabels(res);

        setCircleColorsAndSize();

        initAnimations();

        return this;
    }

    /**
     * Loads the {@link #mProgressStrings} from {@link com.github.omadahealth.slidepager.lib.R.array#slide_progress_long_text}
     *
     * @param res
     */
    private void loadProgressTextLabels(Resources res) {
        mProgressStrings = res.getStringArray(R.array.slide_progress_long_text);
    }

    /**
     * Initiates the animation listener for the {@link CircularBar} so we can animate the streaks in
     * on animation end
     */
    private void initAnimations() {
        addAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animateStreaks();
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
     * Animates the display of the {@link #mLeftStreak} and {@link #mRightStreak} depending
     * on the values of neighboring {@link ProgressView}s
     */
    public void animateStreaks() {
        int index = getIntTag();
        if (getCircularBar().getProgress() >= 99.95f) {
            showCheckMark(true);

            if (mShowStreaks && mSiblings != null && mSiblings.size() > 0) {
                //Previous exists
                if (getIntTag() - 1 >= 0) {
                    ProgressView previousDay = mSiblings.get(index - 1);
                    //Previous is complete
                    boolean complete = previousDay.getProgress() >= 99.95f;
                    showStreak(complete, ProgressView.STREAK.LEFT_STREAK);

                }

                //Next exists
                if (index + 1 < mSiblings.size()) {
                    ProgressView nextDay = mSiblings.get(index + 1);
                    //Next is complete
                    boolean complete = nextDay.getProgress() >= 99.95f;
                    showStreak(complete, ProgressView.STREAK.RIGHT_STREAK);
                }
            }

            //Set Color
            mCircularBar.setCircleFillColor(mCompletedFillColor);
            mCircularBar.setClockwiseReachedArcColor(mCompletedColor);
        } else {
            if (!mIsSpecial) {
                showCheckMark(false);
            }

            //Set Color
            mCircularBar.setCircleFillColor(mFillColor);
            mCircularBar.setClockwiseReachedArcColor(mReachColor);

            showStreak(false, STREAK.RIGHT_STREAK);
            showStreak(false, STREAK.LEFT_STREAK);
        }
    }

    /**
     * Sets the colors of {@link #mCircularBar}
     */
    public void setCircleColorsAndSize() {
        mFillColor = mIsSpecial ? mSpecialFillColor : mNotCompletedFillColor;
        mReachColor = mIsSpecial ? mSpecialReachColor : mNotCompletedReachColor;
        mOutlineColor = mIsSpecial ? mSpecialOutlineColor : mNotCompletedOutlineColor;

        mCircularBar.setClockwiseReachedArcWidth(mReachedWidth);
        mCircularBar.setCircleFillColor(mFillColor);
        mCircularBar.setClockwiseReachedArcColor(mReachColor);
        mCircularBar.setClockwiseOutlineArcColor(mOutlineColor);
        mCircularBar.setClockwiseOutlineArcWidth(mIsFuture ? mNotCompletedFutureOutlineSize : mNotCompletedOutlineSize);
    }

    /**
     * Calls {@link #setProgressText(String)} )} with {@link #mProgressStrings}
     * array position for this view
     */
    public void setProgressText() {
        setProgressText(mProgressStrings[getIntTag()]);
    }

    /**
     * Sets the text for the {@link #mProgressText} or {@link View#GONE} if {@link #mShowProgressText} is false
     */
    public void setProgressText(String text) {
        if (mShowProgressText) {
            getProgressTextView().setText(text);
            getProgressTextView().setTextColor(mNotCompletedReachColor);
        } else {
            getProgressTextView().setVisibility(View.GONE);
        }
    }

    /**
     * Sets the font type of {@link #mProgressText}
     *
     * @param selected True for {@link TypefaceType#ROBOTO_BOLD}, false for {@link TypefaceType#ROBOTO_THIN}
     */
    public void isSelected(boolean selected) {
        Resources res = getContext().getResources();
        Typeface typeface = TypefaceTextView.getFont(getContext(),
                selected ? TypefaceType.ROBOTO_BOLD.getAssetFileName() : mDefaultProgressTypeface.getAssetFileName());
        mProgressText.setTypeface(typeface);
        mProgressText.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? res.getDimension(R.dimen.selected_progress_view_text_size) : res.getDimension(R.dimen.not_selected_progress_view_text_size));
    }

    /**
     * Animate the progress from start to end for the {@link CircularBar} and the rest of the views in
     * this container
     *
     * @param start    0-100
     * @param progress A {@link ProgressAttr} object, containing the progress end (0-100) and the boolean to know if the day is special
     * @param duration The duration in milliseconds of the animation
     * @param siblings The sibling views we use to evaluate streaks showing
     */
    public void animateProgress(int start, ProgressAttr progress, int duration, final List<View> siblings) {
        if (progress == null) {
            return;
        }
        mIsSpecial = progress.isSpecial();
        setCircleColorsAndSize();
        mReachColor = mIsSpecial ? mSpecialReachColor : mReachColor;

        mSiblings = setSiblings(siblings);
        if (mReachColor != mNotCompletedReachColor) {
            mCircularBar.setClockwiseReachedArcColor(mReachColor);
        } else {
            mCircularBar.setClockwiseReachedArcColor(progress.getProgress() == 100 ? mCompletedColor : mReachColor);
        }
        if (progress.isSpecial() && mShowProgressPlusMark) {
            mCheckMark.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_plus));
            if (progress.getProgress() < 0.01) {
                mCheckMark.setAlpha(1f);
            } else {
                mCheckMark.setAlpha(0f);
            }
        }
        mCircularBar.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (siblings != null) {
                    for (View view : siblings) {
                        if (view instanceof ProgressView) {
                            ((ProgressView) view).animateStreaks();
                        }
                    }
                }

                animateStreaks();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mCircularBar.animateProgress(start, progress.getProgress(), duration);

    }

    /**
     * Resets the progress and animations that have occurred on the
     * {@link #mCircularBar} and {@link #mShowStreaks}
     */
    public void reset() {
        mShowStreaks = false;
        setProgressText();
        mCircularBar.setClockwiseReachedArcColor(mReachColor);
        mCircularBar.setCircleFillColor(mFillColor);
        mCircularBar.setClockwiseOutlineArcColor(mOutlineColor);
        mCircularBar.setProgress(0);
    }

    /**
     * Animates the display and hiding of {@link #mCheckMark}
     *
     * @param show True to show, false to hide
     */
    public void showCheckMark(boolean show) {
        AnimatorSet set = new AnimatorSet();
        //Immediately remove them
        if (!show) {
            mCheckMark.setAlpha(0f);
            return;
        }

        //Return if sideView already shown
        if (mCheckMark.getAlpha() == 1f) {
            return;
        }

        mCheckMark.setImageDrawable(getResources().getDrawable(R.mipmap.checkmark_green));
        float start = 0;
        float end = 1f;
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, EASE_IN_DURATION, ObjectAnimator.ofFloat(mCheckMark, "alpha", start, end)));
        set.setDuration(EASE_IN_DURATION);
        set.start();
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

        //Immediately remove them
        if (!show) {
            sideView.setAlpha(0f);
            sideView.setVisibility(View.INVISIBLE);
            return;
        }

        //Return if sideView already shown
        if (sideView.getAlpha() == 1f) {
            sideView.setVisibility(View.VISIBLE);
            return;
        }

        setStreakHeight(sideView);

        float start = 0;
        float end = 1f;
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

    /**
     * Allows to set the {@link #mLeftStreak} or {@link #mRightStreak} height accordingly to the CircularBar dimension.
     *
     * @param streakHeight The streak we want to set the dimension to
     */
    public void setStreakHeight(View streakHeight) {
        streakHeight.getLayoutParams().height = (int) mCircularBar.getDiameter();
        streakHeight.requestLayout();
    }

    /**
     * Sets the animation listener on {@link #mCircularBar}
     *
     * @param listener
     */
    public void addAnimationListener(Animator.AnimatorListener listener) {
        mCircularBar.removeAllListeners();
        mCircularBar.addListener(listener);
    }

    public TypefaceTextView getProgressTextView() {
        return mProgressText;
    }

    /**
     * Returns the contained {@link CircularBar}
     *
     * @return The {@link CircularBar}
     */
    public CircularBar getCircularBar() {
        return mCircularBar;
    }

    /**
     * Returns the current {@link #mCircularBar#getProgress()} using {@link Math#round(float)}
     *
     * @return The {@link #mCircularBar#getProgress()}
     */
    public int getProgress() {
        return Math.round(mCircularBar != null ? mCircularBar.getProgress() : 0f);
    }

    /**
     * Sets the {@link #mSiblings} after removing any non {@link ProgressView}
     * from the list supplied
     *
     * @param views The views in the layout
     */
    public static List<ProgressView> setSiblings(List<View> views) {
        List<ProgressView> siblings = new ArrayList<>();
        if (views != null) {
            for (View view : views) {
                if (view instanceof ProgressView) {
                    siblings.add((ProgressView) view);
                }
            }
        }
        return siblings;
    }

    /**
     * Gets the tag of this view, which are from [1,7]
     *
     * @return
     */
    public Integer getIntTag() {
        return Integer.parseInt((String) getTag());
    }

}
