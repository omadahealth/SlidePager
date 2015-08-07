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
