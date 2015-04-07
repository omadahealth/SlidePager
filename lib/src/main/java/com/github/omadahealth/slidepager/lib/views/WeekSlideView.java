package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.omadahealth.slidepager.lib.R;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/7/15.
 */
public class WeekSlideView extends LinearLayout {

    private DayProgressView[] mDays = new DayProgressView[7];

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
                        int pos = Integer.parseInt((String) view.getTag());
                    }
                });
            }
        }
    }
}
