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
     * Whether this is a special day, and is colored in differently
     * when the page translates. Check :
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialFillColor}
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialOutlineColor}
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialReachColor}
     */
    private boolean mSpecial;

    public ProgressAttr(int progress, boolean special) {
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
