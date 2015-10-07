package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
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
import com.github.omadahealth.slidepager.lib.databinding.ViewBarChartProgressBinding;
import com.github.omadahealth.slidepager.lib.utils.BarChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dae.park on 10/5/15.
 */
public class BarChartProgressView extends RelativeLayout {
    /**
     * The tag for logging
     */
    private static final String TAG = "BarChartProgressView";

    /**
     * The left streak
     */
    private ImageView mLeftStreak;

    /**
     * The right streak
     */
    private ImageView mRightStreak;

    /**
     * The completed check mark that goes inside {@link #mBarView}
     */
    private ImageView mCheckMark;

    /**
     * The circular progress bar
     */
    private BarView mBarView;

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
     * The fill color for {@link #mBarView}
     */
    private int mBarColor;


    /**
     * The progress color for {@link #mBarView} when progress is 100
     */
    private int mCompletedColor;

    /**
     * The progress reached color for {@link #mBarView} when progress is below 100
     */
    private int mNotCompletedColor;

    /**
     * The color of the bar in the future days
     */
    private int mFutureColor;

    /**
     * The width of the progress bar
     */
    private float mBarWidth;

    /**
     * The strings that we set in {@link #mProgressText}
     */
    private static String[] mProgressStrings;

    /**
     * The sibling {@link ProgressView} of this class
     */
    private List<BarChartProgressView> mSiblings;

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
    private ChartProgressAttr mChartProgressAttr;

    /**
     * Data binding for this class
     */
    private ViewBarChartProgressBinding mBinding;


    private static String INSTANCE_SHOW_PROGRESS_TEXT = "show_bar_progress_text";
    private static String INSTANCE_COMPLETED_BAR_COLOR = "bar_completed_color";
    private static String INSTANCE_NOT_COMPLETED_BAR_COLOR = "not_completed_color";
    private static String INSTANCE_FUTURE_COLOR = "bar_future_color";
    private static String INSTANCE_BAR_WIDTH = "bar_width";

    public BarChartProgressView(Context context) {
        this(context, null);
    }

    public BarChartProgressView(Context context, AttributeSet attributeSet) {
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
        bundle.putBoolean(INSTANCE_SHOW_PROGRESS_TEXT, mShowProgressText);
        bundle.putInt(INSTANCE_COMPLETED_BAR_COLOR, mCompletedColor);
        bundle.putInt(INSTANCE_NOT_COMPLETED_BAR_COLOR, mNotCompletedColor);
        bundle.putInt(INSTANCE_FUTURE_COLOR, mFutureColor);
        bundle.putFloat(INSTANCE_BAR_WIDTH, mBarWidth);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            Resources res = getContext().getResources();
            mShowProgressText = bundle.getBoolean(INSTANCE_SHOW_PROGRESS_TEXT, true);
            mCompletedColor = bundle.getInt(INSTANCE_COMPLETED_BAR_COLOR, res.getColor(R.color.default_progress_completed_reach_color));
            mNotCompletedColor = bundle.getInt(INSTANCE_NOT_COMPLETED_BAR_COLOR, res.getColor(R.color.default_progress_not_completed_reach_color));
            mFutureColor = bundle.getInt(INSTANCE_FUTURE_COLOR, res.getColor(R.color.default_progress_special_reach_color));
            mBarWidth = bundle.getFloat(INSTANCE_BAR_WIDTH, res.getDimension(R.dimen.bar_view_default_width));

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
        View view = inflater.inflate(R.layout.view_bar_chart_progress, this);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.view_bar_chart_progress, this, true);
        mCheckMark = mBinding.barChartCheckMark;
        mBarView = mBinding.barview;
        mProgressText = mBinding.barChartProgressText;

        mDefaultProgressTypeface = mProgressText.getCurrentTypeface();

        mProgressText.setTypeface(TypefaceTextView.getFont(context, TypefaceType.ROBOTO_LIGHT.getAssetFileName()));
        loadStyledAttributes(mAttributes, mChartProgressAttr);
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attributes The attributes to read from, do not pass {@link AttributeSet} as inflation needs the context of the {@link android.support.v4.view.PagerAdapter}
     */
    public BarChartProgressView loadStyledAttributes(TypedArray attributes, ChartProgressAttr progress) {
        mAttributes = attributes;
        mChartProgressAttr = progress;

        mIsFuture = progress == null ? false : progress.isFuture();

        Resources res = getContext().getResources();
        if (attributes != null) {
            mShowProgressText = attributes.getBoolean(R.styleable.SlidePager_slide_show_progress_text, true);
            mCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_completed_reach_color, res.getColor(R.color.default_progress_completed_reach_color));
            mNotCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_not_completed_reach_color, res.getColor(R.color.default_progress_not_completed_reach_color));
            mFutureColor = attributes.getColor(R.styleable.SlidePager_slide_progress_bar_chart_future_color, res.getColor(R.color.default_progress_chart_bar_color));
            mBarWidth = attributes.getDimension(R.styleable.SlidePager_slide_progress_bar_chart_bar_width, res.getDimension(R.dimen.bar_view_default_width));
            //Do not recycle attributes, we need them for the future views
        } else {
            mShowProgressText = true;
            mCompletedColor = res.getColor(R.color.default_progress_completed_reach_color);
            mNotCompletedColor = res.getColor(R.color.default_progress_not_completed_reach_color);
            mFutureColor = res.getColor(R.color.default_progress_chart_bar_color);
            mBarWidth = res.getDimension(R.dimen.bar_view_default_width);
        }
        loadProgressTextLabels(res);

        setBarColorsAndSize();

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
                //animateCheckMark();
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
    public void animateCheckMark() {
        int index = getIntTag();
        if (getBarView().getCompleted()) {

            //Show checkmark if the next sibling doesn't have a 100% progress
            //But if this day is the last day of the week, should check next week
            if (mSiblings != null && mSiblings.size() > 0) {
                //next exists
                if (index + 1 < mSiblings.size()) {
                    BarChartProgressView nextDay = mSiblings.get(index + 1);
                    //Next is complete
                    boolean complete = nextDay.getCompleted();
                    if (complete) {
                        showCheckMark(false);
                    } else {
                        showCheckMark(true);
                    }
                }//TODO figure out a way check the first day of next week to show/not show checkmark
            }

            //Set Color
            mBarView.setBarColor(mCompletedColor);

        } else {
            showCheckMark(false);
            //Set Color
            mBarView.setBarColor(mNotCompletedColor);

        }
    }

    /**
     * Sets the colors of {@link #mBarView}
     */
    public void setBarColorsAndSize() {
        mBarColor = mIsFuture ? mFutureColor : mNotCompletedColor;

        mBarView.setBarWidth(mBarWidth);
        mBarView.setBarColor(mBarColor);

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
        getProgressTextView().setText(text);
        getProgressTextView().setTextColor(mCompletedColor);
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
     * Animate the progress from start to end for the {@link BarView} and the rest of the views in
     * this container
     *
     * @param start    0-100
     * @param progress A {@link BarChartProgressAttr} object, containing the progress end (0-100) and the boolean to know if the day is special
     * @param duration The duration in milliseconds of the animation
     * @param siblings The sibling views we use to evaluate streaks showing
     */
    public void animateProgress(int start, ChartProgressAttr progress, int duration, final List<View> siblings) {
        if (progress == null) {
            return;
        }
        mIsFuture = progress.isFuture();
        setBarColorsAndSize();
        mBarColor = mIsFuture ? mFutureColor : mNotCompletedColor;

        mSiblings = setSiblings(siblings);
        mBarView.setBarColor(progress.getProgress() == 100 ? mCompletedColor : mBarColor);

        mBarView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (siblings != null) {
                    for (View view : siblings) {
                        if (view instanceof ProgressView) {
                            ((BarChartProgressView) view).animateCheckMark();
                        }
                    }
                }
                animateCheckMark();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mBarView.setCompleted(progress.getProgress() == 100);
        //TODO real maximum for dividing
        mBarView.animateProgress(start, (int) (progress.getValue() / 200.0f * 100.0f), duration);

    }

    /**
     * Resets the progress and animations that have occurred on the
     * {@link #mBarView} and {@link #mShowStreaks}
     */
    public void reset() {
        setProgressText();
        mBarView.setBarColor(mBarColor);
        mBarView.setCompleted(false);

        if (mAttributes != null && mChartProgressAttr != null) {
            loadStyledAttributes(mAttributes, mChartProgressAttr);
        }
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

        mCheckMark.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_green));
        float start = 0;
        float end = 1f;
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, EASE_IN_DURATION, ObjectAnimator.ofFloat(mCheckMark, "alpha", start, end)));
        set.setDuration(EASE_IN_DURATION);
        set.start();
    }


    /**
     * Sets the animation listener on {@link #mBarView}
     *
     * @param listener
     */
    public void addAnimationListener(Animator.AnimatorListener listener) {
        mBarView.removeAllListeners();
        mBarView.addListener(listener);
    }

    public TypefaceTextView getProgressTextView() {
        return mProgressText;
    }

    /**
     * Returns the contained {@link CircularBar}
     *
     * @return The {@link CircularBar}
     */
    public BarView getBarView() {
        return mBarView;
    }

    /**
     * Returns the current {@link #mBarView#getCompleted()} using {@link Math#round(float)}
     *
     * @return The {@link #mBarView#getCompleted()}
     */
    public boolean getCompleted() {
        return mBarView != null && mBarView.getCompleted();
    }

    /**
     * Sets the {@link #mSiblings} after removing any non {@link ProgressView}
     * from the list supplied
     *
     * @param views The views in the layout
     */
    public static List<BarChartProgressView> setSiblings(List<View> views) {
        List<BarChartProgressView> siblings = new ArrayList<>();
        if (views != null) {
            for (View view : views) {
                if (view instanceof BarChartProgressView) {
                    siblings.add((BarChartProgressView) view);
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
