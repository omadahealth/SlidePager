package com.github.omadahealth.slidepager.lib;

/**
 * Used by {@link SlideTransformer#getViewRatios()} to apply different ratios to the translation
 * depending on the swipe direction
 * <p/>
 * Created by oliviergoutay on 4/1/15.
 */
public class Ratio {

    private float mLeftRatio;
    private float mRightRatio;

    public Ratio(float leftRatio, float rightRatio) {
        this.mLeftRatio = leftRatio;
        this.mRightRatio = rightRatio;
    }

    public float getRatio(boolean swipingRight){
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
