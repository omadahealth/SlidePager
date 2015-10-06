package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.SlideTransformer;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.BarChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.Utilities;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dae.park on 10/5/15.
 */
public class SlideBarChartView extends AbstractSlideView{
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
    private List<BarChartProgressView> mChartProgressList = new ArrayList<>(7);

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
     * The list of {@link ProgressAttr} to associate with {@link #mChartProgressList}.
     * Used in {@link #injectViewsAndAttributes()}
     */
    private List<ChartProgressAttr> mChartProgressAttr;

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

    public SlideBarChartView(Context context) {
        this(context, null, -1, null);
    }

    public SlideBarChartView(Context context, TypedArray attributes, int pagePosition, OnSlidePageChangeListener pageListener) {
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
            mShakeIfNotSelectable = attributes.getBoolean(R.styleable.SlidePager_slide_shake_if_not_selectable, true);
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
            View view = inflater.inflate(R.layout.view_barchart_slide, this);
//            ButterKnife.inject(this, view);

            mAttributes = attributes;
            loadStyledAttributes(attributes);
            injectViewsAndAttributes();
            setListeners();
        }
    }

    /**
     * Inject the views into {@link #mChartProgressList}
     */
    private void injectViewsAndAttributes() {
        if (mUserPageListener == null) {
            return;
        }

        //Init the ProgressAttr for this page
        mChartProgressAttr = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mChartProgressAttr.add(mUserPageListener.getDayProgress(mPagePosition, i));
        }

//        //Progress top texts
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_1));
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_2));
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_3));
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_4));
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_5));
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_6));
//        mProgressTopTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_top_text_7));
//
//        //Progress bar
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_1).loadStyledAttributes(mAttributes, mChartProgressAttr.get(0)));
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_2).loadStyledAttributes(mAttributes, mChartProgressAttr.get(1)));
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_3).loadStyledAttributes(mAttributes, mChartProgressAttr.get(2)));
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_4).loadStyledAttributes(mAttributes, mChartProgressAttr.get(3)));
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_5).loadStyledAttributes(mAttributes, mChartProgressAttr.get(4)));
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_6).loadStyledAttributes(mAttributes, mChartProgressAttr.get(5)));
//        mChartProgressList.add(ButterKnife.<BarChartProgressView>findById(this, R.id.progress_7).loadStyledAttributes(mAttributes, mChartProgressAttr.get(6)));
//
//        //Progress bottom texts
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_1));
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_2));
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_3));
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_4));
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_5));
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_6));
//        mProgressBottomTextList.add(ButterKnife.<TypefaceTextView>findById(this, R.id.bar_progress_bottom_text_7));
//
//
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_1_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_2_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_3_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_4_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_5_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_6_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_7_bar));
//        mChartBarList.add(ButterKnife.<ImageView>findById(this, R.id.bar_progress_bottom_axis));

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
            String oneDecimal = Utilities.formatWeight(mChartProgressAttr.get(i).getValue());
            mProgressTopTextList.get(i).setText(oneDecimal);
            mProgressTopTextList.get(i).setTextColor(mTopTextColor);
        }

        //Set the bottom texts to be the day values and set the color if special
        for (int i = 0; i < mProgressBottomTextList.size(); i++) {
            TypefaceTextView currentTextView = mProgressBottomTextList.get(i);
            currentTextView.setTextColor(mChartProgressAttr.get(i).isSpecial() ? mSpecialBottomTextColor : mBottomTextColor);
            currentTextView.setText(mChartProgressAttr.get(i).getBottomText());
        }
    }

    /**
     * Init the {@link android.graphics.Color} of the {@link #mChartBarList}
     */
    private void initBarColorsAndSize() {
        for (ImageView imageView : mChartBarList) {
            imageView.setBackgroundColor(mChartBarColor);
        }
    }

    /**
     * Set up listeners for all the views in {@link #mChartProgressList}
     */
    private void setListeners() {
        for (final BarChartProgressView barChartProgressView : mChartProgressList) {
            if (barChartProgressView != null) {

                barChartProgressView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = barChartProgressView.getIntTag();
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
                                        .playOn(SlideBarChartView.this);
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
     * @param selected The index of the selected view in {@link #mChartProgressList}
     */
    private void toggleSelectedViews(int selected) {
        mSelectedView = selected;
        for (int i = 0; i < mChartProgressList.size(); i++) {
            BarChartProgressView day = mChartProgressList.get(i);
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
    public void animatePage(OnSlidePageChangeListener listener, TypedArray attributes) {
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof BarChartProgressView) {
                    ((BarChartProgressView) child).loadStyledAttributes(attributes, (ChartProgressAttr) listener.getDayProgress(mPagePosition, ((BarChartProgressView) child).getIntTag()));
                    animateProgress((BarChartProgressView) child, children, listener);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void animateSeries(boolean show) {
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof BarChartProgressView) {
                    final BarChartProgressView barChartProgressView = (BarChartProgressView) child;
                    barChartProgressView.showCheckMark(show);
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
                if (child instanceof BarChartProgressView) {
                    BarChartProgressView barChartProgressView = (BarChartProgressView) child;
                    barChartProgressView.reset();
                }
            }
        }
    }

    private void animateProgress(BarChartProgressView view, List<View> children, OnSlidePageChangeListener listener) {
        if (listener != null) {
            ProgressAttr progress = listener.getDayProgress(mPagePosition, view.getIntTag());
            view.animateProgress(0, (ChartProgressAttr) progress, mProgressAnimationTime, children);
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
