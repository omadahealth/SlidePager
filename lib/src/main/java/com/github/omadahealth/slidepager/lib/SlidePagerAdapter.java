package com.github.omadahealth.slidepager.lib;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by stoyan on 4/3/15.
 */
public abstract class SlidePagerAdapter  extends PagerAdapter {
    /**
     * Returns the view in the adapter based on position
     * @param position The position
     * @return
     */
    public abstract View getCurrentView(int position);
}
