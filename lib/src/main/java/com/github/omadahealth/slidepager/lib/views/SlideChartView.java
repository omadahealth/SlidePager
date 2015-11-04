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
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.SlideTransformer;
import com.github.omadahealth.slidepager.lib.databinding.ViewChartSlideBinding;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoyan on 4/7/15.
 */
public class SlideChartView extends AbstractSlideView {
    /**
     * The tag for logging
     */
    private static final String TAG = "SlideBarChartView";

    /**
     * The duration for the animation for {@link #mSelectedView} when {@link OnSlideListener#onDaySelected(int, int)}
     * is not allowed
     */
    private static final long NOT_ALLOWED_SHAKE_ANIMATION_DURATION = 500;

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
     * The callback listener for when views are clicked
     */
    private OnSlideListener mCallback;

    /**
     * The list of {@link ProgressAttr} to associate with {@link #mProgressList}.
     * Used in {@link #injectViewsAndAttributes()}
     */
    private List<ChartProgressAttr> mProgressAttr;

    /**
     * The int tag of the selected {@link ProgressView} we store so that we can restore the previous selected day.
     */
    private static int mSelectedView;

    /**
     * The default animation time
     */
    private static final int DEFAULT_PROGRESS_ANIMATION_TIME = 1000;

    /**
     * True of we want to shake the view in {@link #toggleSelectedViews(int)}
     */
    private boolean mShakeIfNotSelectable;

    /**
     * The xml attributes of this view
     */
    private TypedArray mAttributes;

    /**
     * Indicates if the user configured the style to be reanimating each time we are scrolling the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     * or not.
     */
    private boolean mHasToReanimate;

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
     * The {@link android.graphics.Color} link of the Bar: {@link SlideChartView#mChartBarList}
     */
    private int mChartBarColor;

    /**
     * The {@link android.support.annotation.DimenRes} link of the Bar: {@link SlideChartView#mChartBarList}
     */
    private float mChartBarSize;

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

    /**
     * The binding object created in {@link #init(Context, TypedArray, int, OnSlidePageChangeListener)}
     */
    private ViewChartSlideBinding mBinding;

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
            mHasToReanimate = mAttributes.getBoolean(R.styleable.SlidePager_slide_pager_reanimate_slide_view, true);
            mTopTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_bar_top_text_color, getResources().getColor(R.color.default_progress_chart_bar_top_text));
            mSpecialBottomTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_bar_bottom_special_text_color, getResources().getColor(R.color.default_progress_chart_bar_special_bottom_text));
            mBottomTextColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_bar_bottom_text_color, getResources().getColor(R.color.default_progress_chart_bar_bottom_text));
            mChartBarColor = attributes.getColor(R.styleable.SlidePager_slide_progress_chart_color, getResources().getColor(R.color.default_progress_chart_bar_color));
            mChartBarSize = attributes.getDimension(R.styleable.SlidePager_slide_progress_chart_bar_size, getResources().getDimension(R.dimen.default_progress_chart_bar_size));
            mShakeIfNotSelectable = attributes.getBoolean(R.styleable.SlidePager_slide_shake_if_not_selectable, true);
        }
    }

    /**
     * Bind the views, set the listeners and attrs
     *
     * @param context
     */
    private void init(Context context, TypedArray attributes, int pagePosition, OnSlidePageChangeListener pageListener) {
        if (!isInEditMode()) {
            this.mPagePosition = pagePosition;
            this.mUserPageListener = pageListener;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mBinding = DataBindingUtil.inflate(inflater, R.layout.view_chart_slide, this, true);

            mAttributes = attributes;
            loadStyledAttributes(attributes);
            injectViewsAndAttributes();
            setListeners();
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

        //Progress top texts
        mProgressTopTextList.add(mBinding.progressTopText1);
        mProgressTopTextList.add(mBinding.progressTopText2);
        mProgressTopTextList.add(mBinding.progressTopText3);
        mProgressTopTextList.add(mBinding.progressTopText4);
        mProgressTopTextList.add(mBinding.progressTopText5);
        mProgressTopTextList.add(mBinding.progressTopText6);
        mProgressTopTextList.add(mBinding.progressTopText7);

        //Progress circles
        mProgressList.add(mBinding.progress1.loadStyledAttributes(mAttributes, mProgressAttr.get(0)));
        mProgressList.add(mBinding.progress2.loadStyledAttributes(mAttributes, mProgressAttr.get(1)));
        mProgressList.add(mBinding.progress3.loadStyledAttributes(mAttributes, mProgressAttr.get(2)));
        mProgressList.add(mBinding.progress4.loadStyledAttributes(mAttributes, mProgressAttr.get(3)));
        mProgressList.add(mBinding.progress5.loadStyledAttributes(mAttributes, mProgressAttr.get(4)));
        mProgressList.add(mBinding.progress6.loadStyledAttributes(mAttributes, mProgressAttr.get(5)));
        mProgressList.add(mBinding.progress7.loadStyledAttributes(mAttributes, mProgressAttr.get(6)));

        //Progress bottom texts
        mProgressBottomTextList.add(mBinding.progressBottomText1);
        mProgressBottomTextList.add(mBinding.progressBottomText2);
        mProgressBottomTextList.add(mBinding.progressBottomText3);
        mProgressBottomTextList.add(mBinding.progressBottomText4);
        mProgressBottomTextList.add(mBinding.progressBottomText5);
        mProgressBottomTextList.add(mBinding.progressBottomText6);
        mProgressBottomTextList.add(mBinding.progressBottomText7);

        //Chart bars
        mChartBarList.add(mBinding.progress1BarTop);
        mChartBarList.add(mBinding.progress2BarTop);
        mChartBarList.add(mBinding.progress3BarTop);
        mChartBarList.add(mBinding.progress4BarTop);
        mChartBarList.add(mBinding.progress5BarTop);
        mChartBarList.add(mBinding.progress6BarTop);
        mChartBarList.add(mBinding.progress7BarTop);
        mChartBarList.add(mBinding.progress1BarBottom);
        mChartBarList.add(mBinding.progress2BarBottom);
        mChartBarList.add(mBinding.progress3BarBottom);
        mChartBarList.add(mBinding.progress4BarBottom);
        mChartBarList.add(mBinding.progress5BarBottom);
        mChartBarList.add(mBinding.progress6BarBottom);
        mChartBarList.add(mBinding.progress7BarBottom);
        mChartBarList.add(mBinding.progressBottomAxis);

        //Init the tags of the subviews
        SlideTransformer.initTags(this);

        //Init values and days text
        initTopAndBottomTexts();

        //Init bar colors and sizes
        initBarColorsAndSize();
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
    private void initBarColorsAndSize() {
        for (ImageView imageView : mChartBarList) {
            imageView.setBackgroundColor(mChartBarColor);
            if (imageView.getId() != R.id.progress_bottom_axis) {
                imageView.setMinimumHeight((int) (mChartBarSize / 2.0f));
            }
        }
    }

    /**
     * Set up listeners for all the views in {@link #mProgressList}
     */
    private void setListeners() {
        for (final ProgressView progressView : mProgressList) {
            if (progressView != null) {

                progressView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = progressView.getIntTag();
                        boolean allowed = true;
                        if (mCallback != null) {
                            allowed = mCallback.isDaySelectable(mPagePosition, index);
                        }
                        if (allowed) {
                            if (mCallback != null) {
                                mCallback.onDaySelected(mPagePosition, index);
                            }
                            toggleSelectedViews(index);
                        } else {
                            if (mShakeIfNotSelectable) {
                                YoYo.with(Techniques.Shake)
                                        .duration(NOT_ALLOWED_SHAKE_ANIMATION_DURATION)
                                        .playOn(SlideChartView.this);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Sets the selected {@link ProgressView}
     *
     * @param selected The index of the selected view in {@link #mProgressList}
     */
    private void toggleSelectedViews(int selected) {
        mSelectedView = selected;
        for (int i = 0; i < mProgressList.size(); i++) {
            ProgressView day = mProgressList.get(i);
            TypefaceTextView currentBottomText = mProgressBottomTextList.get(i);

            if (day.getIntTag() == mSelectedView) {
                day.isSelected(true);
                currentBottomText.setTypeface(currentBottomText.getTypeface(), Typeface.BOLD);
                continue;
            }

            day.isSelected(false);
            currentBottomText.setTypeface(TypefaceTextView.getFont(getContext(), TypefaceType.ROBOTO_LIGHT.getAssetFileName()), Typeface.NORMAL);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void animatePage(OnSlidePageChangeListener listener, TypedArray attributes) {
        super.animatePage(listener, attributes);
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    ProgressAttr progressAttr = listener.getDayProgress(mPagePosition, ((ProgressView) child).getIntTag());
                    ((ProgressView) child).loadStyledAttributes(attributes, progressAttr);
                    animateProgress((ProgressView) child, children, progressAttr);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void resetStreaks(boolean show) {
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    final ProgressView progressView = (ProgressView) child;
                    progressView.showStreak(show, ProgressView.STREAK.RIGHT_STREAK);
                    progressView.showStreak(show, ProgressView.STREAK.LEFT_STREAK);

                    if (mHasToReanimate) {
                        progressView.showCheckMark(show);
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void animateStreaks(OnSlidePageChangeListener listener, TypedArray attributes) {
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    final ProgressView progressView = (ProgressView) child;
                    ProgressAttr progressAttr = listener.getDayProgress(mPagePosition, ((ProgressView) child).getIntTag());
                    ((ProgressView) child).loadStyledAttributes(attributes, progressAttr);
                    progressView.animateStreaks();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void resetPage(TypedArray mAttributes) {
        this.setVisibility(View.VISIBLE);
        this.setAlpha(1f);

        loadStyledAttributes(mAttributes);
        resetStreaks(false);
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    ProgressView progressView = (ProgressView) child;
                    progressView.reset();
                }
            }
        }
    }

    private void animateProgress(ProgressView view, List<View> children, ProgressAttr chartProgressAttr) {
        if (chartProgressAttr != null) {
            view.animateProgress(0, chartProgressAttr, mProgressAnimationTime, children);
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
