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
import com.github.omadahealth.slidepager.lib.databinding.ViewBarchartSlideBinding;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlideListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.utils.ChartProgressAttr;
import com.github.omadahealth.slidepager.lib.utils.ProgressAttr;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.github.omadahealth.typefaceview.TypefaceType;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dae.park on 10/5/15.
 */
public class SlideBarChartView extends AbstractSlideView {
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
    private static final int DEFAULT_PROGRESS_ANIMATION_TIME = 150;

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
     * The position of this page within the {@link com.github.omadahealth.slidepager.lib.SlidePager}
     */
    private int mPagePosition;

    /**
     * Max step for the week. used to scale the bars
     */
    private int mMaxStep = 0;

    /**
     * Data binding for this class
     */
    private ViewBarchartSlideBinding mBinding;

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
            mBinding = DataBindingUtil.inflate(inflater, R.layout.view_barchart_slide, this, true);
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

        //Progress top texts
        mProgressTopTextList.add(mBinding.barProgressTopText1);
        mProgressTopTextList.add(mBinding.barProgressTopText2);
        mProgressTopTextList.add(mBinding.barProgressTopText3);
        mProgressTopTextList.add(mBinding.barProgressTopText4);
        mProgressTopTextList.add(mBinding.barProgressTopText5);
        mProgressTopTextList.add(mBinding.barProgressTopText6);
        mProgressTopTextList.add(mBinding.barProgressTopText7);

        //Progress bar
        mChartProgressList.add(mBinding.progress1.loadStyledAttributes(mAttributes, mChartProgressAttr.get(0)));
        mChartProgressList.add(mBinding.progress2.loadStyledAttributes(mAttributes, mChartProgressAttr.get(1)));
        mChartProgressList.add(mBinding.progress3.loadStyledAttributes(mAttributes, mChartProgressAttr.get(2)));
        mChartProgressList.add(mBinding.progress4.loadStyledAttributes(mAttributes, mChartProgressAttr.get(3)));
        mChartProgressList.add(mBinding.progress5.loadStyledAttributes(mAttributes, mChartProgressAttr.get(4)));
        mChartProgressList.add(mBinding.progress6.loadStyledAttributes(mAttributes, mChartProgressAttr.get(5)));
        mChartProgressList.add(mBinding.progress7.loadStyledAttributes(mAttributes, mChartProgressAttr.get(6)));

        //Progress bottom texts
        mProgressBottomTextList.add(mBinding.barProgressBottomText1);
        mProgressBottomTextList.add(mBinding.barProgressBottomText2);
        mProgressBottomTextList.add(mBinding.barProgressBottomText3);
        mProgressBottomTextList.add(mBinding.barProgressBottomText4);
        mProgressBottomTextList.add(mBinding.barProgressBottomText5);
        mProgressBottomTextList.add(mBinding.barProgressBottomText6);
        mProgressBottomTextList.add(mBinding.barProgressBottomText7);

        //Progress vertical bars
        mChartBarList.add(mBinding.barProgress1Bar);
        mChartBarList.add(mBinding.barProgress2Bar);
        mChartBarList.add(mBinding.barProgress3Bar);
        mChartBarList.add(mBinding.barProgress4Bar);
        mChartBarList.add(mBinding.barProgress5Bar);
        mChartBarList.add(mBinding.barProgress6Bar);
        mChartBarList.add(mBinding.barProgress7Bar);
        mChartBarList.add(mBinding.barProgressBottomAxis);

        //Init the tags of the subviews
        SlideTransformer.initTags(this);

        //Init values and days text
        initTopAndBottomTexts();

        //Init bar colors and sizes
        initBarColorsAndSize();

        //Init the maxstep for the week for scaling;
        initMaxStep();
    }

    /**
     * Init the list of {@link TypefaceTextView}: {@link #mProgressTopTextList} and {@link #mProgressBottomTextList}
     */
    private void initTopAndBottomTexts() {
        //Set the top text to be the values
        for (int i = 0; i < mProgressTopTextList.size(); i++) {
            int intValue = (mChartProgressAttr.get(i).getValue()).intValue();
            String topText= intValue==0? "-": ""+intValue;
            mProgressTopTextList.get(i).setText(topText);
            mProgressTopTextList.get(i).setTextColor(mChartProgressAttr.get(i).isSpecial() ? mSpecialBottomTextColor : mTopTextColor);
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

    private void initMaxStep() {
        for (int i = 0; i < 6; i++) {
            int steps = mChartProgressAttr.get(i).getValue().intValue();
            mMaxStep = steps > mMaxStep ? steps : mMaxStep;
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

    @Override
    public void animatePage(final OnSlidePageChangeListener onPageListener, final TypedArray attributes) {
        animatePage(onPageListener, attributes, 0, 0);
    }

    @SuppressWarnings("unchecked")
    public void animatePage(final OnSlidePageChangeListener onPageListener, final TypedArray attributes, final int position, final int delay) {
        final List<View> children = (List<View>) getTag();
        final List<BarChartProgressView> listView = new ArrayList<>();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof BarChartProgressView) {
                    listView.add((BarChartProgressView) child);
                }
            }
            if (position > listView.size() - 1) {
                return;
            }

            if (mMaxStep == 0) {
                initMaxStep();
            }
            ProgressAttr progressAttr;
            for (final BarChartProgressView bar : listView) {
                bar.setMaxSteps(mMaxStep);
                progressAttr = onPageListener.getDayProgress(mPagePosition, bar.getIntTag());
                bar.loadStyledAttributes(attributes, (ChartProgressAttr) progressAttr);
                animateProgress(bar, progressAttr, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bar.animateCheckMark();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }, 0);
//                bar.animate();
            }

//            animateProgress(barChartProgressView, children, progressAttr, new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    animatePage(onPageListener, attributes, position + 1, (position + 1) * 70);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    if(position==6)
//                    {
//                        for (final BarChartProgressView child: listView)
//                        {
//                            child.animateCheckMark();
//                        }
//                    }
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            }, delay);
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

    private void animateProgress(BarChartProgressView view, ProgressAttr progressAttr, Animator.AnimatorListener animatorListener, int delay) {
        if (progressAttr != null) {
            view.animateProgress(0, (ChartProgressAttr) progressAttr, mProgressAnimationTime, delay, animatorListener);
        }
    }

    /**
     * Sets the listener for  click events in this view
     *
     * @param listener
     */
    public void setListener(OnSlideListener listener) {
        this.mCallback = listener;
    }
}
