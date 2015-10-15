package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
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
import com.github.omadahealth.slidepager.lib.databinding.ViewBarChartProgressBinding;
import com.github.omadahealth.slidepager.lib.utils.BarChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;



/**
 * Created by dae.park on 10/5/15.
 */
public class BarChartProgressView extends RelativeLayout {
    /**
     * The tag for logging
     */
    private static final String TAG = "BarChartProgressView";

    /**
     * The completed check mark that goes inside {@link #mBarView}
     */
    private ImageView mCheckMark;

    /**
     * The progress bar view
     */
    private BarView mBarView;

    /**
     * The duration of the easing in of the checkmark
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
     * Today's color for {@link #mBarView}
     */
    private int mTodayColor;
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
     * Boolean to indicate if it is after today. Used to set the color of the bar
     */
    private boolean mIsFuture;

    /**
     * Boolean to indicate if it is today. Used to set the color of the bar
     */
    private boolean mIsToday;

    /**
     * The saved attributes coming from {@link SlideChartView} and {@link SlideView}
     */
    private TypedArray mAttributes;

    /**
     * The saved {@link ProgressAttr} coming from {@link SlideChartView} and {@link SlideView}
     */
    private ChartProgressAttr mChartProgressAttr;

    /**
     * max step for the week, used to scale the bars. Max value for the week is the highest bar for the week
     * then other bars are scaled off that
     */
    private int mMaxStep;

    /**
     * True if the null value still shows the bar (as a circle)
     */
    private boolean mBarVisibleNullValue;

    /**
     * true if the progress==100;
     */
    private Boolean mCompleted=null;
    /**
     * Check mark visibility on goal completion
     */
    private boolean mCheckMarkVisible;

    /**
     * If the view resets while {@link #mCheckMark} is easing in, it will use this boolean to know that, and hide it
     */
    private boolean mShowCheckMark=false;


    /**
     * Data binding for this class
     */
    private ViewBarChartProgressBinding mBinding;


    private static String INSTANCE_COMPLETED_BAR_COLOR = "bar_completed_color";
    private static String INSTANCE_NOT_COMPLETED_BAR_COLOR = "not_completed_color";
    private static String INSTANCE_FUTURE_COLOR = "bar_future_color";
    private static String INSTANCE_BAR_WIDTH = "bar_width";
    private static String INSTANCE_TODAY_COLOR = "today_color";
    private static String INSTANCE_SHOW_NULL_VAL = "show_null_val";
    private static String INSTANCE_SHOW_CHECKMARK = "show_checkmark";



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
        bundle.putInt(INSTANCE_COMPLETED_BAR_COLOR, mCompletedColor);
        bundle.putInt(INSTANCE_NOT_COMPLETED_BAR_COLOR, mNotCompletedColor);
        bundle.putInt(INSTANCE_FUTURE_COLOR, mFutureColor);
        bundle.putInt(INSTANCE_TODAY_COLOR, mTodayColor);
        bundle.putFloat(INSTANCE_BAR_WIDTH, mBarWidth);
        bundle.putBoolean(INSTANCE_SHOW_NULL_VAL, mBarVisibleNullValue);
        bundle.putBoolean(INSTANCE_SHOW_CHECKMARK, mCheckMarkVisible);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            Resources res = getContext().getResources();
            mCompletedColor = bundle.getInt(INSTANCE_COMPLETED_BAR_COLOR, res.getColor(R.color.default_progress_completed_reach_color));
            mNotCompletedColor = bundle.getInt(INSTANCE_NOT_COMPLETED_BAR_COLOR, res.getColor(R.color.default_progress_not_completed_reach_color));
            mTodayColor = bundle.getInt(INSTANCE_TODAY_COLOR, res.getColor(R.color.default_progress_special_reach_color));
            mFutureColor = bundle.getInt(INSTANCE_FUTURE_COLOR, res.getColor(R.color.default_progress_chart_bar_color));
            mBarWidth = bundle.getFloat(INSTANCE_BAR_WIDTH, res.getDimension(R.dimen.bar_view_default_width));
            mBarVisibleNullValue = bundle.getBoolean(INSTANCE_SHOW_NULL_VAL, true);
            mCheckMarkVisible = bundle.getBoolean(INSTANCE_SHOW_CHECKMARK, true);
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
            mCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_bar_chart_completed_color, res.getColor(R.color.default_progress_completed_reach_color));
            mNotCompletedColor = attributes.getColor(R.styleable.SlidePager_slide_progress_bar_chart_not_completed_color, res.getColor(R.color.default_progress_not_completed_reach_color));
            mFutureColor = attributes.getColor(R.styleable.SlidePager_slide_progress_bar_chart_future_color, res.getColor(R.color.default_progress_chart_bar_color));
            mTodayColor = attributes.getColor(R.styleable.SlidePager_slide_progress_bar_chart_today_color, res.getColor(R.color.default_progress_special_reach_color));
            mBarWidth = attributes.getDimension(R.styleable.SlidePager_slide_progress_bar_chart_bar_width, res.getDimension(R.dimen.bar_view_default_width));
            mBarVisibleNullValue = attributes.getBoolean(R.styleable.SlidePager_slide_progress_bar_chart_null_value_bar_display, true);
            mCheckMarkVisible = attributes.getBoolean(R.styleable.SlidePager_slide_progress_bar_chart_check_mark_visible, true);
            //Do not recycle attributes, we need them for the future views

        } else {
            mCompletedColor = res.getColor(R.color.default_progress_completed_reach_color);
            mNotCompletedColor = res.getColor(R.color.default_progress_not_completed_reach_color);
            mFutureColor = res.getColor(R.color.default_progress_chart_bar_color);
            mTodayColor = res.getColor(R.color.default_progress_special_reach_color);
            mBarWidth = res.getDimension(R.dimen.bar_view_default_width);
            mBarVisibleNullValue = true;
            mCheckMarkVisible = true;
        }

        setBarColorsAndSize();

        initAnimations();

        return this;
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
     * Animates the display of the check mark depending on the progress
     */
    public void animateCheckMark() {
        if (getBarView().getCompleted()) {
            showCheckMark(true);
        } else {
            showCheckMark(false);
        }
    }

    /**
     * Sets the colors of {@link #mBarView}
     */
    public void setBarColorsAndSize() {
        if (mIsToday) {
            mBarColor = !getCompleted() ? mTodayColor : mCompletedColor;
        } else {
            mBarColor = mIsFuture ? mFutureColor : getCompleted() ? mCompletedColor : mNotCompletedColor;
        }
        mBarView.setBarWidth(mBarWidth);
        mBarView.setBarColor(mBarColor);

    }


    public void isSelected(boolean selected) {

    }

    /**
     * Animate the progress from start to end for the {@link BarView} and the rest of the views in
     * this container
     *
     * @param start    0-100
     * @param progress A {@link BarChartProgressAttr} object, containing the progress end (0-100) and the boolean to know if the day is special
     * @param duration The duration in milliseconds of the animation
     */
    public void animateProgress(int start, ChartProgressAttr progress, int duration, int delay, Animator.AnimatorListener animatorListener) {
        if (progress == null) {
            return;
        }
        mIsFuture = progress.isFuture();
        mIsToday = progress.isSpecial();
        showCheckMark(false);
        setBarColorsAndSize();
        mBarView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
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

        int endValue = (int) ((progress.getValue() / (double) mMaxStep) * 100.0);
        if(!mBarVisibleNullValue){
            endValue=-1;
        }
        mBarView.animateProgress(0, endValue, duration, delay);

    }

    /**
     * Resets the progress and animations that have occurred on the
     * {@link #mBarView}
     */
    public void reset() {
        mBarView.setBarColor(mBarColor);
        showCheckMark(false);
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
        if(!mCheckMarkVisible) {
            return;
        }
        mShowCheckMark=show;
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

        mCheckMark.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_steps));
        float start = 0;
        float end = 1f;
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, EASE_IN_DURATION, ObjectAnimator.ofFloat(mCheckMark, "alpha", start, end)));
        set.setDuration(EASE_IN_DURATION);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // resets the checkmark
                if(!mShowCheckMark){
                    mCheckMark.setAlpha(0f);
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
     * Sets the animation listener on {@link #mBarView}
     *
     * @param listener
     */
    public void addAnimationListener(Animator.AnimatorListener listener) {
        mBarView.removeAllListeners();
        mBarView.addListener(listener);
    }


    /**
     * Returns the contained {@link BarView}
     *
     * @return The {@link BarView}
     */
    public BarView getBarView() {
        return mBarView;
    }

    /**
     * Returns the current {@link #mBarView#getCompleted()} using {@link Math#round(float)}
     *
     * @return The {@link ChartProgressAttr#getProgress()}
     */
    public boolean getCompleted() {
        if(mCompleted==null) {
            mCompleted = mChartProgressAttr != null ? mChartProgressAttr.getProgress() == 100 : null;
        }
        return mCompleted==null ? false : mCompleted;
    }

    /**
     * Gets the tag of this view, which are from [1,7]
     *
     * @return
     */
    public Integer getIntTag() {
        return Integer.parseInt((String) getTag());
    }

    /**
     * used to let the each day's views know the week's max step for bar scaling
     *
     * @param maxSteps
     */
    public void setMaxSteps(int maxSteps) {
        this.mMaxStep = maxSteps;
    }
}
