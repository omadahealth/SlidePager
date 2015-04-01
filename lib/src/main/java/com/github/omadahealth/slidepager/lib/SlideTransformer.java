package com.github.omadahealth.slidepager.lib;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by oliviergoutay and stoyand on 4/1/15.
 */
public abstract class SlideTransformer implements ViewPager.PageTransformer {

    protected HashMap<Integer, Float> mRatios;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("unchecked")
    @Override
    public void transformPage(View view, float position) {
        if (view.getTag() == null) {
            initTags(view);
        }

        if (view.getTag() instanceof List) {
            List<View> children = (List<View>) view.getTag();

            for (View child : children) {
                float ratio = mRatios.get(child.getId());
                child.setTranslationX(-position * ((float) view.getWidth() / ratio));
            }
        } else {
            throw new IllegalArgumentException("Tags are not set properly");
        }
    }

    protected void initTags(View view) {
        List<View> subViews = new ArrayList<>();

        Iterator<Map.Entry<Integer, Float>> iterator = mRatios.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Float> entry = iterator.next();
            View subView = view.findViewById(entry.getKey());
            if (subView != null) {
                subViews.add(subView);
            }
        }

        view.setTag(subViews);
    }

    public void setViewRatios(HashMap<Integer, Float> ratios) {
        this.mRatios = ratios;
    }

}