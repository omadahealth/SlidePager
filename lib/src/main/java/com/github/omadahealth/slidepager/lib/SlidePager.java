package com.github.omadahealth.slidepager.lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by oliviergoutay on 4/1/15.
 */
public class SlidePager extends ViewPager {

    public SlidePager(Context context) {
        super(context);
    }

    public SlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        if (!(transformer instanceof SlideTransformer)) {
            throw new IllegalArgumentException("Transformer should be a subclass of SlideTransformer");
        }
        super.setPageTransformer(reverseDrawingOrder, transformer);
    }
}
