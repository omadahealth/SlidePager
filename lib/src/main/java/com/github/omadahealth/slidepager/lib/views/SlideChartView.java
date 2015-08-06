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
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.SlideTransformer;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.typefaceview.TypefaceTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/7/15.
 */
public class SlideChartView extends AbstractSlideView {
    /**
     * The tag for logging
     */
    private static final String TAG = "SlideView";

    /**
     * An array that holds all the {@link TypefaceTextView} that are at the top
     */
    private List<TypefaceTextView> mProgressTopTextList = new ArrayList<>(7);

    /**
     * An array that holds all the {@link ProgressView} for this layout
     */
    private List<ProgressView> mProgressList = new ArrayList<>(7);

    /**
     * An array that holds all the {@link TypefaceTextView} that are at the bottom
     */
    private List<TypefaceTextView> mProgressBottomTextList = new ArrayList<>(7);

    /**
     * An array that holds all the {@link android.widget.ImageView} that are part of the chart bar
     */
    private List<ImageView> mChartBarList = new ArrayList<>(15);

    /**
     * The strings that we set in {@link #mProgressBottomTextList}
     */
    private static String[] mProgressStrings;

    /**
     * The list of {@link ProgressAttr} to associate with {@link #mProgressList}.
     * Used in {@link #injectViewsAndAttributes()}
     */
    private List<ChartProgressAttr> mProgressAttr;

    /**
     * The callback listener for when views are clicked
     */
    private OnSlideListener mCallback;

    /**
     * The default animation time
     */
    private static final int DEFAULT_PROGRESS_ANIMATION_TIME = 1000;

    /**
     * The xml attributes of this view
     */
    private TypedArray mAttributes;

    /**
     * The {@link android.graphics.Color} link of the special day: {@link ProgressAttr#isSpecial()}
     */
    private int mSpecialBottomTextColor;

    /**
     * The {@link android.graphics.Color} link of the top texts: {@link SlideChartView#mProgressTopTextList}
     */
    private int mTopTextColor;

    /**
     * The {@link android.graphics.Color} link of the Bottom texts: {@link SlideChartView#mProgressBottomTextList}
     */
    private int mBottomTextColor;

    /**
     * The {@link android.graphics.Color} link of the Bar: {@link SlideChartView#mProgressBottomTextList}
     */
    private int mChartBarColor;

    /**
     * The position of this page within the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     */
    private int mPagePosition;

    /**
     * The animation time in milliseconds that we animate the progress
     */
    private int mProgressAnimationTime = DEFAULT_PROGRESS_ANIMATION_TIME;

    /**
     * A user defined {@link ViewPager.OnPageChangeListener}
     */
    private OnSlidePageChangeListener<ChartProgressAttr> mUserPageListener;

    public SlideChartView(Context context) {
        this(context, null, -1, null);
    }

    public SlideChartView(Context context, TypedArray attributes, int pagePosition, OnSlidePageChangeListener pageListener) {
        super(context, null);
        init(context, attributes, pagePosition, pageListener);
    }

    private void loadStyledAttributes(TypedArray attributes) {
        mAttributes = attributes;
        if (mAttributes != null) {
            mTopTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_bar_top_text_color, getResources().getColor(R.color.default_progress_chart_bar_top_text));
            mSpecialBottomTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_bar_bottom_special_text_color, getResources().getColor(R.color.default_progress_chart_bar_special_bottom_text));
            mBottomTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_bar_bottom_text_color, getResources().getColor(R.color.default_progress_chart_bar_bottom_text));
            mChartBarColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_color, getResources().getColor(R.color.default_progress_chart_bar_color));
        }
    }

    /**
     * Initiate the view and start butterknife injection
     *
     * @param context
     */
    private void init(Context context, TypedArray attributes, int pagePosition, OnSlidePageChangeListener pageListener) {
        if (!isInEditMode()) {
            this.mPagePosition = pagePosition;
            this.mUserPageListener = pageListener;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_chart_slide, this);
            ButterKnife.inject(this, view);

            mAttributes = attributes;
            loadStyledAttributes(attributes);
            injectViewsAndAttributes();
        }
    }

    /**
     * Inject the views into {@link #mProgressList}
     */
    private void injectViewsAndAttributes() {
        if (mUserPageListener == null) {
            return;
        }

        //Init the ProgressAttr for this page
        mProgressAttr = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mProgressAttr.add(mUserPageListener.getDayProgress(mPagePosition, i));
        }

        //Init the array of strings to default to
        if (mProgressStrings == null) {
            mProgressStrings = getResources().getStringArray(R.array.slide_progress_text);
        }

        //Progress top texts
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_1));
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_2));
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_3));
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_4));
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_5));
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_6));
        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_top_text_7));

        //Progress circles
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_1).loadStyledAttributes(mAttributes, mProgressAttr.get(0)));
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_2).loadStyledAttributes(mAttributes, mProgressAttr.get(1)));
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_3).loadStyledAttributes(mAttributes, mProgressAttr.get(2)));
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_4).loadStyledAttributes(mAttributes, mProgressAttr.get(3)));
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_5).loadStyledAttributes(mAttributes, mProgressAttr.get(4)));
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_6).loadStyledAttributes(mAttributes, mProgressAttr.get(5)));
        mProgressList.add(ButterKnife.<ProgressView>findById(this, R.id.progress_7).loadStyledAttributes(mAttributes, mProgressAttr.get(6)));

        //Progress bottom texts
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_1));
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_2));
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_3));
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_4));
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_5));
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_6));
        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.progress_bottom_text_7));

        //Chart bars
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_1_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_2_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_3_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_4_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_5_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_6_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_7_bar_top));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_1_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_2_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_3_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_4_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_5_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_6_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_7_bar_bottom));
        mChartBarList.add(ButterKnife.<ImageView>findById(this,R.id.progress_bottom_axis));

        //Init the tags of the subviews
        SlideTransformer.initTags(this);

        //Init values and days text
        initTopAndBottomTexts();

        //Init bar colors
        initBarColors();
    }

    /**
     * Init the list of {@link TypefaceTextView}: {@link #mProgressTopTextList} and {@link #mProgressBottomTextList}
     */
    private void initTopAndBottomTexts() {
        //Set the top text to be the values
        for (int i = 0; i < mProgressTopTextList.size(); i++) {
            String oneDecimal = Utilities.formatWeight(mProgressAttr.get(i).getValue());
            mProgressTopTextList.get(i).setText(oneDecimal);
            mProgressTopTextList.get(i).setTextColor(mTopTextColor);
        }

        //Set the bottom texts to be the day values and set the color if special
        for (int i = 0; i < mProgressBottomTextList.size(); i++) {
            TypefaceTextView currentTextView = mProgressBottomTextList.get(i);
            currentTextView.setTextColor(mProgressAttr.get(i).isSpecial() ? mSpecialBottomTextColor : mBottomTextColor);
            currentTextView.setText(mProgressAttr.get(i).getBottomText());
        }
    }

    /**
     * Init the {@link android.graphics.Color} of the {@link #mChartBarList}
     */
    private void initBarColors() {
        for(ImageView imageView : mChartBarList) {
            imageView.setBackgroundColor(mChartBarColor);
        }
    }

    @SuppressWarnings("unchecked")
    public void animatePage(OnSlidePageChangeListener listener, TypedArray attributes) {
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    ((ProgressView) child).loadStyledAttributes(attributes, listener.getDayProgress(mPagePosition, ((ProgressView) child).getIntTag()));
                    animateProgress((ProgressView) child, children, listener);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void animateSeries(boolean show) {
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    final ProgressView progressView = (ProgressView) child;
                    progressView.showStreak(show, ProgressView.STREAK.RIGHT_STREAK);
                    progressView.showStreak(show, ProgressView.STREAK.LEFT_STREAK);
                    progressView.showCheckMark(show);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void resetPage(TypedArray mAttributes) {
        this.setVisibility(View.VISIBLE);
        this.setAlpha(1f);

        loadStyledAttributes(mAttributes);
        animateSeries(false);
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    ProgressView progressView = (ProgressView) child;
                    progressView.reset();
                }
            }
        }
    }

    private void animateProgress(ProgressView view, List<View> children, OnSlidePageChangeListener listener) {
        if (listener != null) {
            ProgressAttr progress = listener.getDayProgress(mPagePosition, view.getIntTag());
            view.animateProgress(0, progress, mProgressAnimationTime, children);
        }
    }

    /**
     * Sets the listener for click events in this view
     *
     * @param listener
     */
    public void setListener(OnSlideListener listener) {
        this.mCallback = listener;
    }
}
