package com.github.omadahealth.slidepager.lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by oliviergoutay on 4/1/15.
 */
public class SlidePager extends ViewPager{

    public SlidePager(Context context) {
        this(context, null);
    }

    public SlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        if (!(transformer instanceof SlideTransformer)) {
            throw new IllegalArgumentException("Transformer should be a subclass of SlideTransformer");
        }

        if(getAdapter() == null){
            super.setPageTransformer(reverseDrawingOrder, transformer);
            return;
        }

        if(getAdapter() instanceof SlidePagerAdapter){
            transformer.transformPage(((SlidePagerAdapter) getAdapter()).getCurrentView(0), 0);
            super.setPageTransformer(reverseDrawingOrder, transformer);
        }
    }

}
