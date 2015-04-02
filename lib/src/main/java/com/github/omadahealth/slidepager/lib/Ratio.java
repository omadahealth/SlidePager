package com.github.omadahealth.slidepager.lib;

/**
 * Used by {@link SlideTransformer#getViewRatios()} to apply different ratios to the translation
 * depending on the swipe direction
 * <p/>
 * Created by oliviergoutay on 4/1/15.
 */
public class Ratio {

    /**
     * The ratio to apply on a left Swipe in the {@link android.support.v4.view.ViewPager}
     */
    private float mLeftRatio;
    /**
     * The ratio to apply on a right Swipe in the {@link android.support.v4.view.ViewPager}
     */
    private float mRightRatio;

    public Ratio(float leftRatio, float rightRatio) {
        this.mLeftRatio = leftRatio;
        this.mRightRatio = rightRatio;
    }

    /**
     * Get the ratio for the current direction
     *
     * @param swipingRight Pass true if the current swipe goes right, false otherwise.
     * @return The {@link #getLeftRatio()} or {@link #getRightRatio()} depending on the direction
     */
    public float getRatio(boolean swipingRight) {
        return swipingRight ? getRightRatio() : getLeftRatio();
    }

    public float getLeftRatio() {
        return mLeftRatio;
    }

    public void setLeftRatio(float leftRatio) {
        this.mLeftRatio = leftRatio;
    }

    public float getRightRatio() {
        return mRightRatio;
    }

    public void setRightRatio(float rightRatio) {
        this.mRightRatio = rightRatio;
    }
}
