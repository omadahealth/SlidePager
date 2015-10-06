/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Omada Health, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.omadahealth.slidepager.lib;

import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.github.omadahealth.slidepager.lib.views.SelectedImageView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This Transformer allows to apply different ratios to different {@link android.view.View} in the
 * {@link android.support.v4.view.ViewPager} pages.
 * <p/>
 * Created by oliviergoutay and stoyand on 4/1/15.
 */
public class SlideTransformer implements ViewPager.PageTransformer {
    /**
     * The tag for logging
     */
    private static final String TAG = "SlideTransformer";

    /**
     * The default ratio to apply translations with.
     */
    public static final Float DEFAULT_TRANSLATION_RATIO = 1.0f;

    /**
     * The {@link LinkedHashMap} that contains all the ratio to appy to the view resourceIds
     */
    private static LinkedHashMap<Integer, Ratio> mRatios;

    /**
     * Allows to save the last position of the {@link android.view.View} to deduce the current direction of the swipe.
     * Used in {@link #transformPage(android.view.View, float)}
     */
    private float lastPosition = 0f;

    /**
     * Modified in {@link #transformPage(android.view.View, float)} by using {@link #lastPosition} to know the direction of the swipe.
     */
    private boolean swipingRight = false;

    /**
     * This method allows to create a Slide effect on the {@link android.support.v4.view.ViewPager} transitions.
     * You need to implement this class and implement the {@link #getViewRatios()} method, providing a map between
     * view-id and ratio.
     *
     * @param view     The {@link android.view.View} currently animating
     * @param position The position reached by this view
     */
    @SuppressWarnings("unchecked")
    @Override
    public void transformPage(View view, float position) {
        if (view == null) {
            return;
        }

        if (view.getTag() == null) {
            initTags(view);
        }

        swipingRight = lastPosition < position;
        lastPosition = position;
        //Stop the root page from moving during animation
        lockPage(view, position);

        if (view.getTag() instanceof List) {
            List<View> children = (List<View>) view.getTag();

            if (getViewRatios() != null) {
                for (View child : children) {
                    int id = child instanceof SelectedImageView ? ((SelectedImageView) child).getSelectedViewId() : child.getId();
                    float ratio = getViewRatios().containsKey(child.getId()) ? getViewRatios().get(id).getRatio(swipingRight) : DEFAULT_TRANSLATION_RATIO;
                    float translation = position * ((float) view.getWidth() / ratio);
                    if (child instanceof SelectedImageView) {
                        Float offset = (Float) child.getTag(R.id.selected_day_image_view);
                        if (offset == null) {
                            offset = 0f;
                        }
                        translation += offset;
                    }
                    child.setTranslationX(translation);
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
    public static void initTags(View view) {
        List<View> subViews = new ArrayList<>();
        if (getViewRatios() != null) {
            for (Map.Entry<Integer, Ratio> entry : getViewRatios().entrySet()) {
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
     * as a key and the scaling for it's translation int {@link #transformPage(android.view.View, float)}.
     * If the view is not in the map, then its translation ratio defaults to {@link #DEFAULT_TRANSLATION_RATIO}
     * </p>
     *
     * @return The map of the view-id/translation-ratio
     */
    public static LinkedHashMap<Integer, Ratio> getViewRatios() {
        if (mRatios == null) {
            mRatios = new LinkedHashMap<>();
            mRatios.put(R.id.left_text_view, new Ratio(4.0f, 1.0f));
            mRatios.put(R.id.right_text_view, new Ratio(1.0f, 4.0f));

            mRatios.put(R.id.progress_1, new Ratio(4.0f, 1.0f));
            mRatios.put(R.id.progress_2, new Ratio(3.5f, 1.5f));
            mRatios.put(R.id.progress_3, new Ratio(3.0f, 2.0f));
            mRatios.put(R.id.progress_4, new Ratio(2.5f, 2.5f));
            mRatios.put(R.id.progress_5, new Ratio(2.0f, 3.0f));
            mRatios.put(R.id.progress_6, new Ratio(1.5f, 3.5f));
            mRatios.put(R.id.progress_7, new Ratio(1.0f, 4.0f));

            mRatios.put(R.id.selected_day_image_view, mRatios.get(R.id.progress_4));
        }

        return mRatios;
    }

    /**
     * Stops the {@link android.support.v4.view.ViewPager} from scrolling the root frame
     * while we perform animations on its children
     * </p>
     * This method can sometimes have an issue, not resetting the alpha and visibility to visible.
     * A fix has been applied to this issue in {@link com.github.omadahealth.slidepager.lib.views.SlideView#resetPage(TypedArray)}.
     *
     * @param view     The root view
     * @param position The position the ViewPager is in
     */
    private void lockPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);
        if (Math.abs(position) >= 1f) {
            view.setAlpha(0.0F);
            view.setVisibility(View.INVISIBLE);
        } else if (position == 0.0F) {
            view.setAlpha(1.0F);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setAlpha(1.0F - (Math.abs(position) * 2));
            view.setVisibility(View.VISIBLE);
        }
    }

}