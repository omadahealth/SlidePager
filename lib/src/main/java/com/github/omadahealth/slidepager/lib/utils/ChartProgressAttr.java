package com.github.omadahealth.slidepager.lib.utils;

/**
 * Created by stoyan on 4/15/15.
 */
public class ChartProgressAttr extends ProgressAttr {

    /**
     * The value in double. Used by {@link com.github.omadahealth.slidepager.lib.views.SlideChartView}.
     */
    private Double mValue;

    /**
     * The String diplayed at the bottom of the chart for each progress. Used by {@link com.github.omadahealth.slidepager.lib.views.SlideChartView}.
     */
    private String mBottomText;

    public ChartProgressAttr(int progress, Double value, String bottomText, boolean special, boolean isFuture) {
        super(progress, special, isFuture);
        this.mValue = value;
        this.mBottomText = bottomText;
    }

    public ChartProgressAttr(int progress, Double value, String bottomText, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedDrawable) {
        super(progress, special, isFuture, reachedColor, completedColor, completedDrawable);
        this.mValue = value;
        this.mBottomText = bottomText;
    }

    public ChartProgressAttr(int progress, Double value, String bottomText, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedDrawable, boolean streakLeftOffScreen, boolean streakRightOffScreen) {
        super(progress, special, isFuture, reachedColor, completedColor, completedDrawable, streakLeftOffScreen, streakRightOffScreen);
        this.mValue = value;
        this.mBottomText = bottomText;
    }

    public Double getValue() {
        if(mValue ==null)
            return 0d;
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
