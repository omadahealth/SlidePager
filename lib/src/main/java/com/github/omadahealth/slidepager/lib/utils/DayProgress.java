package com.github.omadahealth.slidepager.lib.utils;

/**
 * Created by stoyan on 4/15/15.
 */
public class DayProgress {

    /**
     * Progress from 0-100
     */
    private int mProgress;

    /**
     * Whether this is a special day, and is colored in differently
     * when the page translates. Check :
     * {@link com.github.omadahealth.slidepager.lib.views.DayProgressView#mTodayFillColor}
     * {@link com.github.omadahealth.slidepager.lib.views.DayProgressView#mTodayOutlineColor}
     * {@link com.github.omadahealth.slidepager.lib.views.DayProgressView#mTodayReachColor}
     */
    private boolean mSpecial;

    public DayProgress(int progress, boolean special) {
        this.mProgress = progress;
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
}
