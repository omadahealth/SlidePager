package com.github.omadahealth.slidepager.lib.interfaces;

import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Created by stoyan on 4/9/15.
 */
public interface PageChildInterface {

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attributes The attributes to read from, do not pass {@link AttributeSet} as inflation needs the context of the {@link android.support.v4.view.PagerAdapter}
     */
    void loadStyledAttributes(TypedArray attributes);
}
