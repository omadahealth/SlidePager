package com.github.omadahealth.slidepager.lib;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Transformer allows to apply different ratios to different {@link android.view.View} in the
 * {@link android.support.v4.view.ViewPager} pages.
 * <p/>
 * Created by oliviergoutay and stoyand on 4/1/15.
 */
public abstract class SlideTransformer implements ViewPager.PageTransformer {
    /**
     * The default ratio to apply translations with.
     */
    public static final Float DEFAULT_TRANSLATION_RATIO = 1.0f;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("unchecked")
    @Override
    public void transformPage(View view, float position) {
        if (view.getTag() == null) {
            initTags(view);
        }

        if (view.getTag() instanceof List) {
            List<View> children = (List<View>) view.getTag();

            if (getViewRatios() != null) {
                for (View child : children) {
                    float ratio = getViewRatios().containsKey(child.getId()) ? getViewRatios().get(child.getId()) : DEFAULT_TRANSLATION_RATIO;
                    child.setTranslationX(-position * ((float) view.getWidth() / ratio));
                }
            }
        } else {
            throw new IllegalArgumentException("Tags are not set properly");
        }
    }

    /**
     * Init the view tags to improve performances ==> it will call {@link android.view.View#findViewById(int)}
     * only once per View.
     *
     * @param view The {@link android.view.View} we want to search for the ids get into {@link #getViewRatios()} as the keys
     */
    protected void initTags(View view) {
        List<View> subViews = new ArrayList<>();

        if (getViewRatios() != null) {
            for (Map.Entry<Integer, Float> entry : getViewRatios().entrySet()) {
                View subView = view.findViewById(entry.getKey());
                if (subView != null) {
                    subViews.add(subView);
                }
            }
        }

        view.setTag(subViews);
    }

    /**
     * Override this method to return a {@link java.util.HashMap} of the resource ID's of each view
     * as a key and the scalling for it's translation int {@link #transformPage(android.view.View, float)}.
     * If the view is not in the map, then its translation ratio defaults to {@link #DEFAULT_TRANSLATION_RATIO}
     * </p>
     * @return The map of the view-id/translation-ratio
     */
    public abstract HashMap<Integer, Float> getViewRatios();

}