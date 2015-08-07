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

    public abstract void animatePage(OnSlidePageChangeListener listener, TypedArray attributes);

    public abstract void animateSeries(boolean show);
}
