package com.github.omadahealth.slidepager.lib.utils;

/**
 * Created by stoyan on 4/15/15.
 */
public class DayProgress {

    private int mProgress;



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
