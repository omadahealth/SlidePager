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
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.databinding.ViewSlideBinding;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoyan on 4/7/15.
 */
public class SlideView extends AbstractSlideView {
    /**
     * The tag for logging
     */
    private static final String TAG = "SlideView";

    /**
     * The duration for the animation for {@link ViewSlideBinding#selectedDayImageView} when {@link OnSlideListener#onDaySelected(int, int)}
     * is allowed
     */
    private static final int SELECTION_ANIMATION_DURATION = 500;

    /**
     * The duration for the animation for {@link ViewSlideBinding#selectedDayImageView} when {@link OnSlideListener#onDaySelected(int, int)}
     * is not allowed
     */
    private static final long NOT_ALLOWED_SHAKE_ANIMATION_DURATION = 500;

    /**
     * An array that holds all the {@link ProgressView} for this layout
     */
    private List<ProgressView> mProgressList = new ArrayList<>(7);

    /**
     * Indicates if the user configured the style to be reanimating each time we are scrolling the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     * or not.
     */
    private boolean mHasToReanimate;

    /**
     * True of we want to show {@link ViewSlideBinding#leftTextView}
     */
    private boolean mShowLeftText;

    /**
     * True of we want to show {@link ViewSlideBinding#rightTextView}
     */
    private boolean mShowRightText;

    /**
     * True of we want to shake the view in {@link #toggleSelectedViews(int)}
     */
    private boolean mShakeIfNotSelectable;

    /**
     * True if we want to show {@link #mSelectedView}
     */
    private boolean mShowSelectedBar;

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
     * The {@link AnimatorSet} used to animate the Slider selected day
     */
    private AnimatorSet mAnimationSet;

    /**
     * The int tag of the selected {@link ProgressView} we store so that we can
     * translate the {@link ViewSlideBinding#selectedDayImageView} to the same day when we transition between
     * different instances of this class
     */
    private static int mSelectedView;

    /**
     * The position of this page within the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     */
    private int mPagePosition;

    /**
     * The text for the {@link ViewSlideBinding#leftTextView}
     */
    private String mLeftText;

    /**
     * The text for the {@link ViewSlideBinding#rightTextView}
     */
    private String mRightText;

    /**
     * The animation time in milliseconds that we animate the progress
     */
    private int mProgressAnimationTime = DEFAULT_PROGRESS_ANIMATION_TIME;

    /**
     * A user defined {@link ViewPager.OnPageChangeListener}
     */
    private OnSlidePageChangeListener mUserPageListener;

    /**
     * The binding object created in {@link #init(Context, TypedArray, int, OnSlidePageChangeListener)}
     */
    private ViewSlideBinding mBinding;

    public SlideView(Context context) {
        this(context, null, -1, null, null, null);
    }

    public SlideView(Context context, TypedArray attributes, int pagePosition, OnSlidePageChangeListener pageListener, String leftText, String rightText) {
        super(context, null);
        this.mLeftText = leftText;
        this.mRightText = rightText;
        init(context, attributes, pagePosition, pageListener);
    }

    private void loadStyledAttributes(TypedArray attributes) {
        mAttributes = attributes;
        if (mAttributes != null) {
            mHasToReanimate = mAttributes.getBoolean(R.styleable.SlidePager_slide_pager_reanimate_slide_view, true);
            mShowLeftText = attributes.getBoolean(R.styleable.SlidePager_slide_show_week, true);
            mShowRightText = attributes.getBoolean(R.styleable.SlidePager_slide_show_date, true);
            mShakeIfNotSelectable = attributes.getBoolean(R.styleable.SlidePager_slide_shake_if_not_selectable, true);
            mShowSelectedBar = attributes.getBoolean(R.styleable.SlidePager_slide_show_selected_bar, true);

            mBinding.selectedDayImageView.setVisibility(mShowSelectedBar ? VISIBLE : GONE);
            mBinding.leftTextView.setVisibility(mShowLeftText && mLeftText != null ? VISIBLE : GONE);
            mBinding.rightTextView.setVisibility(mShowRightText && mRightText != null ? VISIBLE : GONE);

            if (mShowLeftText && mBinding.leftTextView != null && mLeftText != null) {
                mBinding.leftTextView.setText(mLeftText, true);
            }

            if (mShowRightText && mBinding.rightTextView != null && mRightText != null) {
                mBinding.rightTextView.setText(mRightText);
            }
        }
    }

    /**
     * Bind the view and init the listeners and attrs
     *
     * @param context
     */
    private void init(Context context, TypedArray attributes, int pagePosition, OnSlidePageChangeListener pageListener) {
        if (!isInEditMode()) {
            this.mPagePosition = pagePosition;
            this.mUserPageListener = pageListener;
            this.mAttributes = attributes;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mBinding = DataBindingUtil.inflate(inflater, R.layout.view_slide, this, true);

            mAnimationSet = new AnimatorSet();
            injectViews();
            setListeners();
            loadStyledAttributes(attributes);
        }
    }

    /**
     * Inject the views into {@link #mProgressList}
     */
    private void injectViews() {
        mProgressList.add(mBinding.progress1.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 0) : null));
        mProgressList.add(mBinding.progress2.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 1) : null));
        mProgressList.add(mBinding.progress3.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 2) : null));
        mProgressList.add(mBinding.progress4.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 3) : null));
        mProgressList.add(mBinding.progress5.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 4) : null));
        mProgressList.add(mBinding.progress6.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 5) : null));
        mProgressList.add(mBinding.progress7.loadStyledAttributes(mAttributes, mUserPageListener != null ? mUserPageListener.getDayProgress(mPagePosition, 6) : null));

        mBinding.leftTextView.setTypeface(TypefaceTextView.getFont(getContext(), TypefaceType.ROBOTO_LIGHT.getAssetFileName()));
        mBinding.rightTextView.setTypeface(TypefaceTextView.getFont(getContext(), TypefaceType.ROBOTO_LIGHT.getAssetFileName()));

        mBinding.selectedDayImageView.setSelectedViewId(mProgressList.get(SlideView.getSelectedView()).getId());
    }

    /**
     * Animates the translation of the {@link ViewSlideBinding#selectedDayImageView}
     *
     * @param view          The view to use to set the animation position
     * @param startPosition The starting x position for the animated view
     */
    public void animateSelectedTranslation(final View view, float startPosition) {
        final Float offset = -1f * this.getWidth() + view.getWidth() / 2 + view.getX();
        mBinding.selectedDayImageView.setTag(R.id.selected_day_image_view, offset);
        mBinding.selectedDayImageView.setSelectedViewId(view.getId());


        if (mAnimationSet == null) {
            mAnimationSet = new AnimatorSet();
        }
        mAnimationSet.playSequentially(Glider.glide(Skill.QuadEaseInOut, SELECTION_ANIMATION_DURATION, ObjectAnimator.ofFloat(mBinding.selectedDayImageView, "x", startPosition, offset)));
        mAnimationSet.setDuration(SELECTION_ANIMATION_DURATION);
        mAnimationSet.start();
    }

    /**
     * Calls {@link #animateSelectedTranslation(View, float)} with the start
     * position set to the {@link ViewSlideBinding#selectedDayImageView#getX()}
     *
     * @param view The view to use to set the animation position
     */
    public void animateSelectedTranslation(View view) {
        animateSelectedTranslation(view, mBinding.selectedDayImageView.getX());
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
                            animateSelectedTranslation(view);
                        } else {
                            if (mShakeIfNotSelectable) {
                                YoYo.with(Techniques.Shake)
                                        .duration(NOT_ALLOWED_SHAKE_ANIMATION_DURATION)
                                        .playOn(SlideView.this);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Animates the {@link ProgressView}s of this page and sets their attributes.
     *
     * @param listener   A listener to get {@link OnSlidePageChangeListener#getDayProgress(int, int)}
     * @param attributes The attributes to use
     */
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
            animateSelectedTranslation(mProgressList.get(mSelectedView));

            if (mCallback != null) {
                mCallback.onDaySelected(mPagePosition, mSelectedView);
            }
        }
    }

    /**
     * Displays or hides the streaks of all the {@link ProgressView}s
     *
     * @param show True to animate them visible, false to immediately hide them
     */
    @Override
    @SuppressWarnings("unchecked")
    public void resetStreaks(boolean show) {
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    final ProgressView progressView = (ProgressView) child;
                    progressView.showStreak(show, ProgressView.STREAK.RIGHT_STREAK);
                    progressView.showStreak(show, ProgressView.STREAK.LEFT_STREAK);
                    progressView.showStreak(show, ProgressView.STREAK.CENTER_STREAK);

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

    /**
     * Resets the state of this page, usually called when we change pages in the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     *
     * @param attributes The attributes to reset the views to
     */
    @SuppressWarnings("unchecked")
    public void resetPage(TypedArray attributes) {
        this.setVisibility(View.VISIBLE);
        this.setAlpha(1f);

        loadStyledAttributes(attributes);
        mSelectedView = getSelectableIndex();

        resetStreaks(false);
        getSelectedImageView().resetView();
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        if (children != null) {
            for (final View child : children) {
                if (child instanceof ProgressView) {
                    ProgressView progressView = (ProgressView) child;
                    progressView.reset();
                }
            }
        }
        toggleSelectedViews(mSelectedView);

    }

    /**
     * Animates the progress of a {@link ProgressView}
     *
     * @param view         The view to animate
     * @param children     The sibling views we use to evaluate streaks showing
     * @param progressAttr The {@link ProgressAttr} for this view got from the listener
     */
    private void animateProgress(ProgressView view, List<View> children, ProgressAttr progressAttr) {
        if (progressAttr != null) {
            ProgressAttr progress = progressAttr;
            view.animateProgress(0, progress, mProgressAnimationTime, children);
        }
    }

    /**
     * Public method for animating an individual {@link ProgressView}
     *
     * @param index    The index from [0,6] of the view
     * @param progress The {@link ProgressAttr} to animate
     */
    @SuppressWarnings("unchecked")
    public void animateProgressView(int index, ProgressAttr progress) {
        final List<View> children = (List<View>) getTag(R.id.slide_transformer_tag_key);
        ProgressView view = getProgressView(index);
        if (view != null && view.isShowCircularBar()) {
            view.animateProgress(view.getProgress(), progress, mProgressAnimationTime, children);
        }
    }

    /**
     * Sets the selected {@link ProgressView}
     *
     * @param selected The index of the selected view in {@link #mProgressList}
     */
    private void toggleSelectedViews(int selected) {
        mSelectedView = selected;
        for (ProgressView day : mProgressList) {
            if (day.getIntTag() == mSelectedView) {
                day.isSelected(true);
                if (mCallback != null) {
                    String label = mCallback.getDayTextLabel(mPagePosition, day.getIntTag());
                    if (label != null) {
                        day.setProgressText(label);
                    } else {
                        day.setProgressText();
                    }

                } else {
                    day.setProgressText();
                }
                continue;
            }
            day.isSelected(false);
            day.setProgressText();
        }
    }

    /**
     * Checks to see what index is selectable on {@link #animatePage(OnSlidePageChangeListener, TypedArray)}
     * so that we don't automatically select a non selectable date in {@link com.github.omadahealth.slidepager.lib.SlidePager#resetPage(int)}
     *
     * @return The largest index selectable before {#mSelectedView}, if not the largest index selectable in {@link SlideView} or 0 if non are
     */
    public int getSelectableIndex() {
        if (mCallback == null) {
            return mSelectedView;
        }
        if (mCallback.isDaySelectable(mPagePosition, mSelectedView)) {
            return mSelectedView;
        }

        //Try any position lower than the current one
        for (int i = mSelectedView; i >= 0; i--) {
            if (mCallback.isDaySelectable(mPagePosition, i)) {
                return i;
            }
        }

        //Try any position greater
        if (mProgressList != null) {
            for (int i = mProgressList.size() - 1; i >= mSelectedView; i--) {
                if (mCallback.isDaySelectable(mPagePosition, i)) {
                    return i;
                }
            }
        }

        //Default to 0
        return 0;
    }

    /**
     * Sets the listener for click events in this view
     *
     * @param listener
     */
    public void setListener(OnSlideListener listener) {
        this.mCallback = listener;
    }

    /**
     * Returns the {@link ProgressView} at the given index
     *
     * @param index The index from [0,6]
     * @return The view
     */
    public ProgressView getProgressView(int index) {
        if (mProgressList == null || index < 0 || index > mProgressList.size() - 1) {
            return null;
        }
        return mProgressList.get(index);
    }

    /**
     * Returns the view that displays the currently selected {@link ProgressView}
     *
     * @return
     */
    public SelectedImageView getSelectedImageView() {
        return mBinding.selectedDayImageView;
    }

    /**
     * Gets the currently selected index
     *
     * @return
     */
    public synchronized static int getSelectedView() {
        return mSelectedView;
    }

    /**
     * Sets the currently selected index
     *
     * @param selectedView From [0,6]
     */
    public synchronized static void setSelectedView(int selectedView) {
        SlideView.mSelectedView = selectedView;
    }
}
