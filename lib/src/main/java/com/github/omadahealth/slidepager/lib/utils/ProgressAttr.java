package com.github.omadahealth.slidepager.lib.utils;

/**
 * Created by stoyan on 4/15/15.
 */
public class ProgressAttr {

    /**
     * Progress from 0-100
     */
    private int mProgress;

    /**
     * The value in double. Used by {@link com.github.omadahealth.slidepager.lib.views.SlideChartView}.
     */
    private Double mValue;

    /**
     * Whether this is a special day, and is colored in differently
     * when the page translates. Check :
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialFillColor}
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialOutlineColor}
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialReachColor}
     */
    private boolean mSpecial;

    /**
     * The String diplayed at the bottom of the chart for each progress. Used by {@link com.github.omadahealth.slidepager.lib.views.SlideChartView}.
     */
    private String mBottomText;

    public ProgressAttr(int progress, boolean special) {
        this.mProgress = progress;
        this.mSpecial = special;
    }

    public ProgressAttr(int progress, Double value, String bottomText, boolean special) {
        this.mProgress = progress;
        this.mValue = value;
        this.mBottomText = bottomText;
        this.mSpecial = special;
    }

    public boolean isSpecial() {
        return mSpecial;
    }

    public void setSpecial(boolean special) {
        this.mSpecial = special;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        this.mValue = value;
    }

    public String getBottomText() {
        return mBottomText;
    }

    public void setBottomText(String bottomText) {
        this.mBottomText = bottomText;
    }
}
