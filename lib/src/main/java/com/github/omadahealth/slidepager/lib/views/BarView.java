package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.omadahealth.slidepager.lib.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dae.park on 10/5/15.
 */
public class BarView extends View implements Animator.AnimatorListener {
    /**
     * TAG for logging
     */
    private static final String TAG = "BarView";

    /**
     * The context of this view
     */
    private Context mContext;

    /**
     * The default {@link #mMax} of the bar,
     */
    public static final int DEFAULT_BAR_MAX = 100;

    /**
     * The max progress, default is 100
     */
    private int mMax = DEFAULT_BAR_MAX;

    /**
     * Current progress, can not exceed the {@link #mMax} progress.
     */
    private boolean mCompleted;

    /**
     * The clockwise progress area bar color
     */
    private int mBarColor;

    /**
     * The clockwise width of the reached area
     */
    private float mBarWidth;

    /**
     * The Painter of the fill circle.
     */
    private Paint mBarFillPaint;

    /**
     * The bar area rect.
     */
    private RectF mBarRectF = new RectF(0, 0, 0, 0);

    /**
     * The suffix of the number.
     */
    private String mSuffix = "%";

    /**
     * The prefix.
     */
    private String mPrefix = "";

    /**
     * A list of listeners we call on animations
     */
    private List<Animator.AnimatorListener> mListeners;


    /**
     * The defaults for width and color of the reached and outline arcs
     */
    private final int default_bar_color = Color.parseColor("#00c853");
    private final int default_circle_fill_color = Color.parseColor("#00000000");//fully transparent
    private final float default_bar_width;

    /**
     * For save and restore instance of progressbar
     */
    private static final String INSTANCE_STATE = "saved_instance";

    private static final String INSTANCE_BAR_WIDTH = "bar_width";
    private static final String INSTANCE_BAR_COLOR = "bar_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_COMPLETED = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        default_bar_width = dp2px(10f);

        mListeners = new ArrayList<>();
        loadStyledAttributes(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateDrawRectF();


        //Draw the bar
        canvas.drawRoundRect(mBarRectF, mBarWidth / 2, mBarWidth / 2, mBarFillPaint);

    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_BAR_WIDTH, getBarWidth());
        bundle.putInt(INSTANCE_BAR_COLOR, getBarColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putBoolean(INSTANCE_COMPLETED, getCompleted());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mBarWidth = bundle.getFloat(INSTANCE_BAR_WIDTH);
            mBarColor = bundle.getInt(INSTANCE_BAR_COLOR);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setCompleted(bundle.getBoolean(INSTANCE_COMPLETED));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attrs        The attributes to read from
     * @param defStyleAttr The styles to read from
     */
    public void loadStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            final TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularViewPager,
                    defStyleAttr, 0);
            mBarColor = attributes.getColor(R.styleable.CircularViewPager_progress_arc_clockwise_color, default_bar_color);
            mBarWidth = attributes.getDimension(R.styleable.CircularViewPager_progress_arc_clockwise_width, default_bar_width);
            setMax(attributes.getInt(R.styleable.CircularViewPager_progress_arc_max, 100));

            attributes.recycle();

            initializePainters();
        }
    }

    /**
     * Measures the space available for our view in {@link #onMeasure(int, int)}
     *
     * @param measureSpec The width or height of the view
     * @param isWidth     True if the measureSpec param is the width, false otherwise
     * @return The usable dimension of the spec
     */
    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    /**
     * Calculates the coordinates of {@link #mBarRectF}
     */
    private void calculateDrawRectF() {
        setPadding(getPaddingLeft(), (int) mBarWidth / 2, getPaddingRight(), getPaddingBottom());
        mBarRectF = getBarRect(mBarWidth / 2);

    }

    /**
     * Calculates the coordinates of {@link android.graphics.RectF} that
     * are perfectly within the available window
     *
     * @param offset Half the width of the pain stroke
     * @return The rectF
     */
    private RectF getBarRect(float offset) {
        RectF workingSurface = new RectF();
        workingSurface.left = getPaddingLeft();
        workingSurface.top = getPaddingTop();
        workingSurface.right = getWidth() - getPaddingRight();
        workingSurface.bottom = getHeight() - getPaddingBottom();

        float width = workingSurface.right - workingSurface.left;
        float height = workingSurface.bottom - workingSurface.top;

        float xCenter = width / 2;
        float left = (xCenter - offset) > 0 ? xCenter - offset : 0;
        float right = (xCenter + offset) < width ? xCenter + offset : width;
        //float left, float top, float right, float bottom
        return new RectF(left, workingSurface.top, right, height);
    }

    /**
     * Initializes the paints used for the bars
     */
    private void initializePainters() {
        mBarFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarFillPaint.setColor(mBarColor);
        mBarFillPaint.setAntiAlias(true);
        mBarFillPaint.setStrokeWidth(mBarWidth);
        mBarFillPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        invalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * Animate the change in progress of this view
     *
     * @param start    The value to start from, between 0-100
     * @param end      The value to set it to, between 0-100
     * @param duration The the time to run the animation over
     */
    public void animateProgress(int start, int end, int duration) {
        ViewGroup parent = (ViewGroup) getParent();
        int heightToReach = (parent.getMeasuredHeight() * end) / 100;

        AnimatorSet set = new AnimatorSet();
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, duration, ObjectAnimator.ofInt(this, "minimumHeight", start, heightToReach)));
        set.setDuration(duration);
        set = addListenersToSet(set);
        set.start();
    }

    /**
     * Adds the current listeners to the {@link com.nineoldandroids.animation.AnimatorSet}
     * before animation starts
     *
     * @param set The set to add listeners to
     * @return The set with listeners added
     */
    protected AnimatorSet addListenersToSet(AnimatorSet set) {
        if (mListeners != null && set != null) {
            set.addListener(this);
            for (Animator.AnimatorListener listener : mListeners) {
                set.addListener(listener);
            }
        }
        return set;
    }

    /**
     * Method to add a listener to call on animations
     *
     * @param listener The listener to call
     */
    public void addListener(Animator.AnimatorListener listener) {
        mListeners.add(listener);
    }

    /**
     * Removes the listener provided
     *
     * @param listener The listener to remove
     * @return True if it was in the list and removed, false otherwise
     */
    public boolean removeListener(Animator.AnimatorListener listener) {
        return mListeners.remove(listener);
    }

    /**
     * Removes all animation listeners
     */
    public void removeAllListeners() {
        mListeners = new ArrayList<>();
    }

    /**
     * Get the suffix
     *
     * @return
     */
    public String getSuffix() {
        return mSuffix;
    }

    /**
     * Get the prefix
     *
     * @return
     */
    public String getPrefix() {
        return mPrefix;
    }


    /**
     * The clockwise outline arc color
     *
     * @return
     */
    public int getBarColor() {
        return mBarColor;
    }


    /**
     * The current progress
     *
     * @return
     */
    public boolean getCompleted() {
        return mCompleted;
    }

    /**
     * Get the max of the reached arc, defaults to 100
     *
     * @return
     */
    public int getMax() {
        return mMax;
    }

    /**
     * Get the width of the {@link #mBarWidth}
     *
     * @return
     */
    public float getBarWidth() {
        return mBarWidth;
    }


    /**
     * Sets the {@link #mBarColor} and invalidates the view
     *
     * @param color The hex color to set
     */
    public void setBarColor(int color) {
        this.mBarColor = color;
        initializePainters();
        invalidate();
    }


    /**
     * Sets the {@link #mBarWidth} and invalidates the view
     *
     * @param width The height in dp to set
     */
    public void setBarWidth(float width) {
        mBarWidth = width;
        invalidate();
    }


    /**
     * Sets the {@link #mMax} and invalidates the view
     *
     * @param max The height in dp to set
     */
    public void setMax(int max) {
        if (max > 0) {
            this.mMax = max;
            invalidate();
        }
    }

    /**
     * Sets the {@link #mSuffix}
     *
     * @param suffix The suffix
     */
    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }

    /**
     * Sets the {@link #mPrefix}
     *
     * @param prefix The prefix
     */
    public void setPrefix(String prefix) {
        if (prefix == null)
            mPrefix = "";
        else {
            mPrefix = prefix;
        }
    }

    /**
     * @param completed
     */
    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    /**
     * Convert from dp to pixels according to device density
     *
     * @param dp The length in dip to convert
     * @return The pixel equivalent for this device
     */
    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * Convert from text sp to dp according to device density
     *
     * @param sp The length in sp to convert
     * @return The pixel equivalent for this device
     */
    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


}
