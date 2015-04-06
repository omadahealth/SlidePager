package com.github.omadahealth.slidepager.lib;

import android.support.v4.view.ViewPager;

/**
 * Created by stoyan on 4/6/15.
 */
public interface OnSlidePageChangeListener extends ViewPager.OnPageChangeListener {

    /**
     * Override this method to get all the progress for the current {@link SlidePager} page.
     *
     * @param index The index for the day (0-7)
     * @return The progress value for the specified day
     */
    public int getSlideProgress(int index);

}
