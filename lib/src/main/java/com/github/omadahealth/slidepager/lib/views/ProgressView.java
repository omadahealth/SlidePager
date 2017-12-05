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
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.omadahealth.circularbarpager.library.CircularBar;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.databinding.ViewProgressBinding;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoyan on 4/2/15.
 */
public class ProgressView extends RelativeLayout {
    /**
     * The tag for logging
     */
    private static final String TAG = "ProgressView";

    /**
     * The default {@link ViewProgressBinding#progressText} is initialized in its attrs
     */
    private TypefaceType mDefaultProgressTypeface;

    /**
     * The duration of the easing in of {@link ViewProgressBinding#progressStreakLeft} and {@link ViewProgressBinding#progressStreakRight}
     */
    private static final int EASE_IN_DURATION = 350;

    /**
     * For save and restore instance of progressbar
     */
    private static final String INSTANCE_STATE = "saved_instance";

    /**
     * The color of the progress already achieved for {@link ViewProgressBinding#circularBar}
     */
    private int mReachColor;

    /**
     * The fill color for {@link ViewProgressBinding#circularBar}
     */
    private int mFillColor;

    /**
     * The default outline color for {@link ViewProgressBinding#circularBar}
     */
    private int mOutlineColor;

    /**
     * The default fill color for {@link ViewProgressBinding#circularBar} when progress is 100
     */
    private int mCompletedFillColor;

    /**
     * The default fill color for {@link ViewProgressBinding#circularBar} when progress is below 100
     */
    private int mNotCompletedFillColor;

    /**
     * The progress color for {@link ViewProgressBinding#circularBar} when progress is 100
     */
    private int mCompletedColor;

    /**
     * The progress checkmark drawable id, if specified by {@link ProgressAttr} or set apart
     */
    private Integer mCompletedDrawable;

    /**
     * The progress reached color for {@link ViewProgressBinding#circularBar} when progress is below 100
     */
    private int mNotCompletedReachColor;

    /**
     * The progress color outline for {@link ViewProgressBinding#circularBar} when progress is below 100
     */
    private int mNotCompletedOutlineColor;

    /**
     * The progress size outline for {@link ViewProgressBinding#circularBar} when progress is below 100
     */
    private float mNotCompletedOutlineSize;

    /**
     * The progress size outline for {@link ViewProgressBinding#circularBar} when this day is in the future
     */
    private float mNotCompletedFutureOutlineSize;

    /**
     * The progress reached color for {@link ViewProgressBinding#circularBar} when it is today's date
     */
    private int mSpecialReachColor;

    /**
     * The progress color outline for {@link ViewProgressBinding#circularBar} when it is today's date
     */
    private int mSpecialOutlineColor;

    /**
     * The progress fill color for today's date
     */
    private int mSpecialFillColor;

    /**
     * The progress color used for {@link android.widget.TextView} under the {@link CircularBar}
     */
    private int mProgressTextColor;

    /**
     * The width of the reached progress
     */
    private float mReachedWidth;

    /**
     * The strings that we set in {@link ViewProgressBinding#progressText}
     */
    private static String[] mProgressStrings;

    /**
     * The sibling {@link ProgressView} of this class
     */
    private List<ProgressView> mSiblings;

    /**
     * Boolean that controls if the {@link ViewProgressBinding#progressStreakLeft} and  {@link ViewProgressBinding#progressStreakRight} showed
     * be shown
     */
    private boolean mShowStreaks;

    /**
     * Boolean that controls if the {@link ViewProgressBinding#progressText} is visible or not
     */
    private boolean mShowProgressText;

    /**
     * True of we want to show {@link ViewProgressBinding#circularBar}
     */
    private static boolean mShowCircularBar = true;

    /**
     * Boolean that controls if the {@link ViewProgressBinding#checkMark} is visible or not
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
     * Indicates if the user configured the style to be reanimating each time we are scrolling the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     * or not.
     */
    private boolean mHasToReanimate;

    public List<Boolean> getHaveToAnimateCirclePieces() {
        return mHaveToAnimateCirclePieces;
    }

    /**
     * list of booleans mapping to parts of circle arcs. true to animate those part false to not.
     */
    private List<Boolean> mHaveToAnimateCirclePieces;

    /**
     * The saved attributes coming from {@link SlideChartView} and {@link SlideView}
     */
    private TypedArray mAttributes;

    /**
     * The saved {@link ProgressAttr} coming from {@link SlideChartView} and {@link SlideView}
     */
    private ProgressAttr mProgressAttr;

    private Double mCompletedPercentage;

    private boolean needToAnimateProgress = true;

    private ViewProgressBinding mBinding;

    /**
     * The left and right streaks
     */
    public enum STREAK {
        LEFT_STREAK,
        RIGHT_STREAK,
        CENTER_STREAK
    }

    private static String INSTANCE_SHOW_STREAKS = "show_streaks";
    private static String INSTANCE_SHOW_PROGRESS_TEXT = "show_progress_text";
    private static String INSTANCE_SHOW_PROGRESS_PLUSMARK = "show_progress_plusmark";
    private static String INSTANCE_REANIMATE = "reanimate";
    private static String INSTANCE_COMPLETED_COLOR = "completed_color";
    private static String INSTANCE_COMPLETED_FILL_COLOR = "completed_fill_color";
    private static String INSTANCE_NOT_COMPLETED_COLOR = "not_completed_color";
    private static String INSTANCE_NOT_COMPLETED_OUTLINE_COLOR = "not_completed_outline_color";
    private static String INSTANCE_NOT_COMPLETED_OUTLINE_SIZE = "not_completed_outline_size";
    private static String INSTANCE_NOT_COMPLETED_FUTURE_OUTLINE_SIZE = "not_completed_future_outline_size";
    private static String INSTANCE_NOT_COMPLETED_FILL_COLOR = "not_completed_fill_color";
    private static String INSTANCE_SPECIAL_COMPLETED_COLOR = "special_color";
    private static String INSTANCE_SPECIAL_COMPLETED_OUTLINE_COLOR = "special_outline_color";
    private static String INSTANCE_TEXT_COLOR = "text_color";
    private static String INSTANCE_SPECIAL_COMPLETED_FILL_COLOR = "special_fill_color";
    private static String INSTANCE_REACHED_WIDTH = "reached_width";
    private static String INSTANCE_COMPLETED_PERCENTAGE = "completed_percentage";

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (isInEditMode()) {
            return;
        }
        mAttributes = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.SlidePager, 0, 0);
        bindViews(context);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_SHOW_STREAKS, mShowStreaks);
        bundle.putBoolean(INSTANCE_SHOW_PROGRESS_TEXT, mShowProgressText);
        bundle.putBoolean(INSTANCE_SHOW_PROGRESS_PLUSMARK, mShowProgressPlusMark);
        bundle.putBoolean(INSTANCE_REANIMATE, mHasToReanimate);

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
        bundle.putInt(INSTANCE_TEXT_COLOR, mProgressTextColor);
        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);

        bundle.putDouble(INSTANCE_COMPLETED_PERCENTAGE, mCompletedPercentage);

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
            mHasToReanimate = bundle.getBoolean(INSTANCE_REANIMATE, true);

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

            mProgressTextColor = bundle.getInt(INSTANCE_TEXT_COLOR, res.getColor(R.color.default_progress_text_color));

            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH, res.getDimension(R.dimen.default_progress_reached_width));

            mCompletedPercentage = bundle.getDouble(INSTANCE_COMPLETED_PERCENTAGE, 99.95f);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Bind the views and load attributes
     *
     * @param context
     */
    private void bindViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.view_progress, this, true);

        mDefaultProgressTypeface = mBinding.progressText.getCurrentTypeface();

        mBinding.circularBar.setStartLineEnabled(false);
        mBinding.progressText.setTypeface(TypefaceTextView.getFont(context, TypefaceType.getTypeface(TypefaceType.getDefaultTypeface(getContext())).getAssetFileName()));
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

        mIsSpecial = progress != null && progress.isSpecial();
        mIsFuture = progress != null && progress.isFuture();
        mCompletedDrawable = progress == null ? null : progress.getCompletedDrawable();
        mHaveToAnimateCirclePieces = progress == null ? null : progress.getHaveToAnimateCirclePieces();

        Resources res = getContext().getResources();
        if (attributes != null) {
            mShowStreaks = attributes.getBoolean(R.styleable.SlidePager_slide_show_streaks, true);
            mShowProgressText = attributes.getBoolean(R.styleable.SlidePager_slide_show_progress_text, true);
            mShowProgressPlusMark = attributes.getBoolean(R.styleable.SlidePager_slide_show_progress_plusmark, true);
            mHasToReanimate = mAttributes.getBoolean(R.styleable.SlidePager_slide_pager_reanimate_slide_view, true);

            mCompletedColor = progress != null && progress.getCompletedColor() != null ?
                    progress.getCompletedColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_completed_reach_color, res.getColor(R.color.default_progress_completed_reach_color));

            mCompletedFillColor = progress != null && progress.getCompletedFillColor() != null ?
                    progress.getCompletedFillColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_completed_fill_color, res.getColor(R.color.default_progress_completed_fill_color));

            mNotCompletedReachColor = progress != null && progress.getReachedColor() != null ?
                    progress.getReachedColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_reach_color, res.getColor(R.color.default_progress_not_completed_reach_color));

            mNotCompletedOutlineColor = progress != null && progress.getNotCompletedOutlineColor() != null ?
                    progress.getNotCompletedOutlineColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_outline_color, res.getColor(R.color.default_progress_not_completed_outline_color));
            mNotCompletedOutlineSize = attributes.getDimension(R.styleable.SlidePager_slide_progress_not_completed_outline_size, res.getDimension(R.dimen.circular_bar_default_outline_width));
            mNotCompletedFutureOutlineSize = attributes.getDimension(R.styleable.SlidePager_slide_progress_not_completed_future_outline_size, res.getDimension(R.dimen.circular_bar_default_future_outline_width));
            mNotCompletedFillColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_fill_color, res.getColor(R.color.default_progress_not_completed_fill_color));

            mSpecialReachColor = progress != null && progress.getSpecialReachColor() != null ?
                    progress.getSpecialReachColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_special_reach_color, res.getColor(R.color.default_progress_special_reach_color));
            mSpecialOutlineColor = progress != null && progress.getSpecialOutlineColor() != null ?
                    progress.getSpecialOutlineColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_special_outline_color, res.getColor(R.color.default_progress_special_outline_color));
            mSpecialFillColor = progress != null && progress.getSpecialFillColor() != null ?
                    progress.getSpecialFillColor()
                    :
                    attributes.getColor(R.styleable.SlidePager_slide_progress_special_fill_color, res.getColor(R.color.default_progress_special_fill_color));

            mCompletedDrawable = progress != null && progress.getCompletedDrawable() != null ?
                    progress.getCompletedDrawable()
                    :
                    R.drawable.checkmark_green;


            mProgressTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_text_color, res.getColor(R.color.default_progress_text_color));

            mReachedWidth = attributes.getDimension(R.styleable.SlidePager_slide_progress_reached_width, res.getDimension(R.dimen.default_progress_reached_width));

            mCompletedPercentage = progress != null && progress.getCompletedPercentage() != null ? progress.getCompletedPercentage() : new Double(attributes.getFloat(R.styleable.SlidePager_slide_progress_completed_precentage, 99.95f));

            //Do not recycle attributes, we need them for the future views
        } else {
            mShowStreaks = true;
            mShowProgressText = true;
            mShowProgressPlusMark = true;
            mHasToReanimate = true;

            mCompletedColor = progress != null && progress.getCompletedColor() != null ?
                    progress.getCompletedColor()
                    :
                    res.getColor(R.color.default_progress_completed_reach_color);
            mCompletedFillColor = res.getColor(R.color.default_progress_completed_fill_color);

            mNotCompletedReachColor = res.getColor(R.color.default_progress_not_completed_reach_color);
            mNotCompletedOutlineColor = res.getColor(R.color.default_progress_not_completed_outline_color);
            mNotCompletedOutlineSize = res.getDimension(R.dimen.circular_bar_default_outline_width);
            mNotCompletedFutureOutlineSize = res.getDimension(R.dimen.circular_bar_default_future_outline_width);
            mNotCompletedFillColor = res.getColor(R.color.default_progress_not_completed_fill_color);

            mSpecialReachColor = res.getColor(R.color.default_progress_special_reach_color);
            mSpecialOutlineColor = res.getColor(R.color.default_progress_special_outline_color);
            mSpecialFillColor = res.getColor(R.color.default_progress_special_fill_color);

            mProgressTextColor = res.getColor(R.color.default_progress_text_color);

            mCompletedPercentage = 99.95d;

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
        if (mProgressStrings == null) {
            mProgressStrings = res.getStringArray(R.array.slide_progress_long_text);
        }
        if (mShowProgressText) {
            getProgressTextView().setVisibility(View.VISIBLE);
        } else {
            getProgressTextView().setVisibility(View.GONE);
        }
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
     * Animates the display of the {@link ViewProgressBinding#progressStreakLeft} and {@link ViewProgressBinding#progressStreakRight} depending
     * on the values of neighboring {@link ProgressView}s
     */
    public void animateStreaks() {
        int index = getIntTag();
        if (getCircularBar().getProgress() >= mCompletedPercentage) {
            showCheckMark(true);

            if (mShowStreaks && mSiblings != null && mSiblings.size() > 0) {
                //Previous exists?
                //Next exists
                boolean previousComplete = false;
                boolean nextComplete = false;
                if (getIntTag() - 1 >= 0) {
                    ProgressView previousDay = mSiblings.get(index - 1);
                    //Previous is complete
                    previousComplete = previousDay.getProgress() >= mCompletedPercentage;
                } else {
                    if (mProgressAttr != null) {
                        previousComplete = mProgressAttr.isStreakLeftOffScreen();
                    }
                }

                if (index + 1 < mSiblings.size()) {
                    ProgressView nextDay = mSiblings.get(index + 1);
                    //Next is complete
                    nextComplete = nextDay.getProgress() >= mCompletedPercentage;
                } else {
                    if (mProgressAttr != null) {
                        nextComplete = mProgressAttr.isStreakRightOffScreen();
                    }
                }

                //Show/Hide left
                showStreak(!previousComplete && nextComplete, ProgressView.STREAK.LEFT_STREAK);
                //Show/Hide center
                showStreak(previousComplete && nextComplete, STREAK.CENTER_STREAK);
                //Show/Hide right
                showStreak(previousComplete && !nextComplete, ProgressView.STREAK.RIGHT_STREAK);
            }

            //Set Color
            mBinding.circularBar.setCircleFillColor(mCompletedFillColor);
            mBinding.circularBar.setClockwiseReachedArcColor(mCompletedColor);
        } else {
            if (!mIsSpecial) {
                showCheckMark(false);
            }

            //Set Color
            mBinding.circularBar.setCircleFillColor(mFillColor);
            mBinding.circularBar.setClockwiseReachedArcColor(mReachColor);

            showStreak(false, STREAK.RIGHT_STREAK);
            showStreak(false, STREAK.LEFT_STREAK);
            showStreak(false, STREAK.CENTER_STREAK);
        }
    }

    /**
     * Sets the colors of {@link ViewProgressBinding#circularBar}
     */
    public void setCircleColorsAndSize() {
        if (mShowCircularBar) {
            mBinding.progressCircularbarStreaksLayout.setVisibility(View.VISIBLE);
            mFillColor = mIsSpecial ? mSpecialFillColor : mNotCompletedFillColor;
            mReachColor = mIsSpecial ? mSpecialReachColor : mNotCompletedReachColor;
            mOutlineColor = mIsSpecial ? mSpecialOutlineColor : mNotCompletedOutlineColor;

            mBinding.circularBar.setClockwiseReachedArcWidth(mReachedWidth);
            mBinding.circularBar.setCircleFillColor(mFillColor);
            mBinding.circularBar.setClockwiseReachedArcColor(mReachColor);
            mBinding.circularBar.setClockwiseOutlineArcColor(mOutlineColor);
            mBinding.circularBar.setClockwiseOutlineArcWidth(mIsFuture ? mNotCompletedFutureOutlineSize : mNotCompletedOutlineSize);
        } else {
            mBinding.progressCircularbarStreaksLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Calls {@link #setProgressText(String)} )} with {@link #mProgressStrings}
     * array position for this view
     */
    public void setProgressText() {
        setProgressText(mProgressStrings[getIntTag()]);
    }

    /**
     * Sets the text for the {@link ViewProgressBinding#progressText} or {@link View#GONE} if {@link #mShowProgressText} is false
     */
    public void setProgressText(String text) {
        getProgressTextView().setText(text);
        getProgressTextView().setTextColor(mProgressTextColor);
    }

    /**
     * Sets the font type of {@link ViewProgressBinding#progressText}
     *
     * @param selected True for {@link TypefaceType#getDefaultTypeface(Context)} in bold, false for normal
     */
    public void isSelected(boolean selected) {
        Resources res = getContext().getResources();
        Typeface typeface = TypefaceTextView.getFont(getContext(), TypefaceType.getTypeface(TypefaceType.getDefaultTypeface(getContext())).getAssetFileName());
        mBinding.progressText.setTypeface(typeface, selected ? Typeface.BOLD : Typeface.NORMAL);
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
    public void animateProgress(int start, final ProgressAttr progress, int duration, final List<View> siblings) {
        if (progress == null || !mShowCircularBar) {
            return;
        }
        //Set CheckMark drawable for non-special
        mCompletedDrawable = progress.getCompletedDrawable() != null ? progress.getCompletedDrawable() : mCompletedDrawable;

        //Set Checkmark drawable for special and not completed
        if (mCompletedDrawable != null) {
            mBinding.checkMark.setImageDrawable(getResources().getDrawable(mCompletedDrawable));
        } else {
            mBinding.checkMark.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_green));
        }
        boolean reanimate = false;
        if (mHaveToAnimateCirclePieces != null) {
            if (mBinding.circularBar.getCirclePieceFillList().size() != progress.getHaveToAnimateCirclePieces().size()) {
                reanimate = true;
            } else {
                for (int i = 0; i < progress.getHaveToAnimateCirclePieces().size(); i++) {
                    if (mBinding.circularBar.getCirclePieceFillList().get(i) != progress.getHaveToAnimateCirclePieces().get(i)) {
                        reanimate = true;
                        break;
                    }
                }
            }
        }

        if (!mHasToReanimate && (getProgress() - progress.getProgress()) == 0 && !reanimate) {
            animateStreaks();
            return;
        }


        mProgressAttr = progress;

        mIsSpecial = progress.isSpecial();
        setCircleColorsAndSize();

        mSiblings = setSiblings(siblings);

        setUpColors(progress);
        if (progress.isSpecial() && mShowProgressPlusMark && progress.getProgress() < 99.9) {
            mBinding.checkMark.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_plus));

            //Set drawable alpha depending on progress
            if (progress.getProgress() < 0.01) {
                mBinding.checkMark.setAlpha(1f);
            } else {
                mBinding.checkMark.setAlpha(0f);
            }
        }

        mBinding.circularBar.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //Fill color
                if (progress.getCompletedFillColor() != null) {
                    mCompletedFillColor = progress.getCompletedFillColor();
                }
                mBinding.circularBar.setCircleFillColor(mCompletedFillColor);

                //Streaks
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


        if (mHaveToAnimateCirclePieces == null) {
            mBinding.circularBar.animateProgress(start, progress.getProgress(), duration);
        } else {
            if (reanimate) {
                mBinding.circularBar.animateProgress(mHaveToAnimateCirclePieces, duration);
            }

        }
    }

    /**
     * Set up custom colors based on {@link ProgressAttr} parameters
     */
    public void setUpColors(ProgressAttr progress) {
        //Reached color
        if (progress.getReachedColor() != null) {
            mReachColor = progress.getReachedColor();
        } else {
            mReachColor = mIsSpecial ? mSpecialReachColor : mReachColor;
        }
        if (mReachColor != mNotCompletedReachColor) {
            mBinding.circularBar.setClockwiseReachedArcColor(mReachColor);
        } else {
            mBinding.circularBar.setClockwiseReachedArcColor(progress.getProgress() == 100 ? mCompletedColor : mReachColor);
        }

        //CompletedColor
        if (progress.getCompletedColor() != null) {
            mCompletedColor = progress.getCompletedColor();
        }
        mBinding.circularBar.setClockwiseReachedArcColor(mCompletedColor);
    }

    /**
     * Resets the progress and animations that have occurred on the
     * {@link ViewProgressBinding#circularBar} and {@link #mShowStreaks}
     */
    public void reset() {
        mShowStreaks = false;
        setProgressText();
    }

    /**
     * Animates the display and hiding of {@link ViewProgressBinding#checkMark}
     *
     * @param show True to show, false to hide
     */
    public void showCheckMark(boolean show) {
        AnimatorSet set = new AnimatorSet();
        //Immediately remove them
        if (!show) {
            mBinding.checkMark.setAlpha(0f);
            return;
        }

        //Return if sideView already shown
        if (mBinding.checkMark.getAlpha() == 1f) {
            return;
        }

        float start = 0;
        float end = 1f;
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, EASE_IN_DURATION, ObjectAnimator.ofFloat(mBinding.checkMark, "alpha", start, end)));
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
                sideView = mBinding.progressStreakLeft;
                break;
            case RIGHT_STREAK:
                sideView = mBinding.progressStreakRight;
                break;
            case CENTER_STREAK:
                sideView = mBinding.progressStreakCenter;
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
     * Allows to set the {@link ViewProgressBinding#progressStreakLeft} or {@link ViewProgressBinding#progressStreakRight} height accordingly to the CircularBar dimension.
     *
     * @param streakHeight The streak we want to set the dimension to
     */
    public void setStreakHeight(View streakHeight) {
        streakHeight.getLayoutParams().height = (int) mBinding.circularBar.getDiameter();
        streakHeight.requestLayout();
    }

    /**
     * Sets the animation listener on {@link ViewProgressBinding#circularBar}
     *
     * @param listener
     */
    public void addAnimationListener(Animator.AnimatorListener listener) {
        mBinding.circularBar.removeAllListeners();
        mBinding.circularBar.addListener(listener);
    }

    public TypefaceTextView getProgressTextView() {
        return mBinding.progressText;
    }

    /**
     * Returns the contained {@link CircularBar}
     *
     * @return The {@link CircularBar}
     */
    public CircularBar getCircularBar() {
        return mBinding.circularBar;
    }

    /**
     * Returns the current {@link ViewProgressBinding#circularBar#getProgress()} using {@link Math#round(float)}
     *
     * @return The {@link ViewProgressBinding#circularBar#getProgress()}
     */
    public int getProgress() {
        return Math.round(mBinding.circularBar != null ? mBinding.circularBar.getProgress() : 0f);
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
        return Integer.parseInt((String) (getTag() == null ? "0" : getTag()));
    }

    public static boolean isShowCircularBar() {
        return mShowCircularBar;
    }

    public static void setShowCircularBar(boolean showCircularBar) {
        mShowCircularBar = showCircularBar;
    }

    public void setCompletedColor(int completedColor) {
        this.mCompletedColor = completedColor;
    }

    public void setNotCompletedReachColor(int mNotCompletedReachColor) {
        this.mNotCompletedReachColor = mNotCompletedReachColor;
    }

    public void setCompletedFillColor(int mCompletedFillColor) {
        this.mCompletedFillColor = mCompletedFillColor;
    }

    public void setCompletedDrawable(Integer completedDrawable) {
        this.mCompletedDrawable = completedDrawable;
        if (mCompletedDrawable != null) {
            mBinding.checkMark.setImageDrawable(getResources().getDrawable(mCompletedDrawable));
        }
    }

    /**
     * Set the {@link #mCompletedColor} manually or by data binding, for the 100% color
     */
    @BindingAdapter("slide_progress_completed_reach_color")
    public static void setSlideProgressCompletedReachColor(ProgressView progressView, @ColorInt int color) {
        progressView.setCompletedColor(color);
    }

    /**
     * Allows the user to set a specific set of {@link #mProgressStrings} to be displayed below each {@link ProgressView}
     *
     * @param progressStrings The new array of {@link #mProgressStrings}
     */
    public static synchronized void setProgressStrings(String[] progressStrings) {
        mProgressStrings = progressStrings;
    }
}
