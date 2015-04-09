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
package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.interfaces.OnWeekListener;
import com.github.omadahealth.typefaceview.TypefaceTextView;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/7/15.
 */
public class WeekSlideView extends LinearLayout {
    /**
     * An array that holds all the {@link DayProgressView} for this layout
     */
    private DayProgressView[] mDays = new DayProgressView[7];

    /**
     * The callback listener for when views are clicked
     */
    private OnWeekListener mCallback;

    /**
     * The left textview
     */
    private TypefaceTextView mLeftTextView;

    /**
     * The right textview
     */
    private TypefaceTextView mRightTextView;

    public WeekSlideView(Context context) {
        this(context, null);
    }

    public WeekSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Initiate the view and start butterknife injection
     *
     * @param context
     */
    private void init(Context context) {
        if (!isInEditMode()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_week_slide, this);
            ButterKnife.inject(this, view);

            injectDays();
            setListeners();
        }
    }

    /**
     * Inject the views into {@link #mDays}
     */
    private void injectDays() {
        mDays[0] = ButterKnife.findById(this, R.id.day_progress_1);
        mDays[1] = ButterKnife.findById(this, R.id.day_progress_2);
        mDays[2] = ButterKnife.findById(this, R.id.day_progress_3);
        mDays[3] = ButterKnife.findById(this, R.id.day_progress_4);
        mDays[4] = ButterKnife.findById(this, R.id.day_progress_5);
        mDays[5] = ButterKnife.findById(this, R.id.day_progress_6);
        mDays[6] = ButterKnife.findById(this, R.id.day_progress_7);
    }

    /**
     * Set up listeners for all the views in {@link #mDays}
     */
    private void setListeners() {
        for (DayProgressView dayProgressView : mDays) {
            if (dayProgressView != null) {
                dayProgressView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = Integer.parseInt((String) view.getTag());
                        if(mCallback != null){
                            mCallback.onDaySelected(index);
                        }
                    }
                });
            }
        }
    }

    /**
     * Sets the listener for click events in this view
     * @param listener
     */
    public void setListener(OnWeekListener listener) {
        this.mCallback = listener;
    }
}
