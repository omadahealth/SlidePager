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

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.github.omada.progressslider.lib.R;

/**
 * Created by oliviergoutay on 12/9/14.
 */
public class ShootViewPagerTransformer implements ViewPager.PageTransformer {

    /**
     * Used for adding a fadein/fadeout animation in the ViewPager transition.
     * Must be used with {@link android.support.v4.view.ViewPager#setPageTransformer(boolean, android.support.v4.view.ViewPager.PageTransformer)}
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void transformPage(View view, float position) {
        if (position < 0) {
            view.setScrollX((int) ((float) (view.getWidth()) * position));
        } else if (position > 0) {
            view.setScrollX(-(int) ((float) (view.getWidth()) * -position));
        } else {
            view.setScrollX(view.getWidth() / 2);
        }

        int pageWidth = view.getWidth();
        TextView top = (TextView) view.findViewById(R.id.user_top_textview);
        TextView center = (TextView) view.findViewById(R.id.value_info_textview);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]


            top.setTranslationX((float) (-(1 - position) * 0.5 * pageWidth));

            center.setTranslationX((float) (-(1 - position) * pageWidth));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }

}
