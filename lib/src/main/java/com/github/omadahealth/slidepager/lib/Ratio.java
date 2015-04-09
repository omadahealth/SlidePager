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

/**
 * Used by {@link SlideTransformer#getViewRatios()} to apply different ratios to the translation
 * depending on the swipe direction
 * <p/>
 * Created by oliviergoutay on 4/1/15.
 */
public class Ratio {

    /**
     * The ratio to apply on a left Swipe in the {@link android.support.v4.view.ViewPager}
     */
    private float mLeftRatio;
    /**
     * The ratio to apply on a right Swipe in the {@link android.support.v4.view.ViewPager}
     */
    private float mRightRatio;

    public Ratio(float leftRatio, float rightRatio) {
        this.mLeftRatio = leftRatio;
        this.mRightRatio = rightRatio;
    }

    /**
     * Get the ratio for the current direction
     *
     * @param swipingRight Pass true if the current swipe goes right, false otherwise.
     * @return The {@link #getLeftRatio()} or {@link #getRightRatio()} depending on the direction
     */
    public float getRatio(boolean swipingRight) {
        return swipingRight ? getRightRatio() : getLeftRatio();
    }

    public float getLeftRatio() {
        return mLeftRatio;
    }

    public void setLeftRatio(float leftRatio) {
        this.mLeftRatio = leftRatio;
    }

    public float getRightRatio() {
        return mRightRatio;
    }

    public void setRightRatio(float rightRatio) {
        this.mRightRatio = rightRatio;
    }
}
