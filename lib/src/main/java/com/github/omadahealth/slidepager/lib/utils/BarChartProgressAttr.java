package com.github.omadahealth.slidepager.lib.utils;

/**
 * Created by dae.park on 10/5/15.
 */
public class BarChartProgressAttr extends ProgressAttr {

    /**
     * The value in double. Used by {@link com.github.omadahealth.slidepager.lib.views.SlideBarChartView}.
     */
    private Double mValue;

    /**
     * The String diplayed at the bottom of the chart for each progress. Used by {@link com.github.omadahealth.slidepager.lib.views.SlideBarChartView}.
     */
    private String mBottomText;

    public BarChartProgressAttr(int progress, Double value, String bottomText, boolean special, boolean isFuture) {
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
