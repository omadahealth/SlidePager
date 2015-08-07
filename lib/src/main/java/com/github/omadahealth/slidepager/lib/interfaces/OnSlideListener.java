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
package com.github.omadahealth.slidepager.lib.interfaces;

/**
 * Created by stoyan on 4/7/15.
 */
public interface OnSlideListener {

    /**
     * Called when one of the {@link com.github.omadahealth.slidepager.lib.views.ProgressView} is
     * clicked by the user
     * @param index The index of the day, ie. Sunday = 0, Monday = 1...
     */
    void onDaySelected(int page, int index);

    /**
     * Called when one of the {@link com.github.omadahealth.slidepager.lib.views.ProgressView} is
     * clicked by the user
     * @param index The index of the day, ie. Sunday = 0, Monday = 1...
     * @return True if selected page index is allowed, false otherwise
     */
    boolean isDaySelectable(int page, int index);

    /**
     * Called when the {@link com.github.omadahealth.slidepager.lib.views.ProgressView} has been
     * selected, returns the text it should display as its label
     * @param page The page of the view
     * @param index The index of the view
     * @return The text label
     */
    String getDayTextLabel(int page, int index);
}
