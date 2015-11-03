package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;

/**
 * Created by oliviergoutay on 8/5/15.
 */
public abstract class AbstractSlideView extends LinearLayout {

    /**
     * Indicates if this page already called {@link #animatePage(OnSlidePageChangeListener, TypedArray)}
     * to know if we want to reanimate or not.
     */
    protected boolean mHasAnimated;

    public AbstractSlideView(Context context) {
        super(context);
    }

    public AbstractSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void resetPage(TypedArray mAttributes);

    public void animatePage(OnSlidePageChangeListener listener, TypedArray attributes) {
        mHasAnimated = true;
    }

    public abstract void animateSeries(boolean show);

    /**
     * Indicates if this page already called {@link #animatePage(OnSlidePageChangeListener, TypedArray)}
     * to know if we want to reanimate or not.
     */
    public boolean hasAnimated() {
        return mHasAnimated;
    }
}
