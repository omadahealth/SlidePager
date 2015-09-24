package com.github.omadahealth.slidepager.lib.adapter;

import android.support.v4.view.PagerAdapter;

import com.github.omadahealth.slidepager.lib.SlidePager;
import com.github.omadahealth.slidepager.lib.views.AbstractSlideView;

/**
 * Created by oliviergoutay on 8/5/15.
 */
public abstract class AbstractSlidePagerAdapter<T extends AbstractSlideView> extends PagerAdapter {

    /**
     * Get the current view based on the current position of the {@link SlidePager}
     *
     * @param position The position to get the view from
     * @return The view for the given position, if created yet
     */
    public abstract T getCurrentView(int position);
}
