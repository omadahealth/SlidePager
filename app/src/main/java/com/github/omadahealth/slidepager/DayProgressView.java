package com.github.omadahealth.slidepager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.omadahealth.demo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by stoyan on 4/2/15.
 */
public class DayProgressView extends RelativeLayout {

    @InjectView(R.id.day_progress_streak_left)
    ImageView mLeftStreak;
    @InjectView(R.id.day_progress_streak_right)
    ImageView mRightStreak;
    @InjectView(R.id.circularbar)
    CircularBar mCircularBar;

    public DayProgressView(Context context) {
        this(context, null);
    }

    public DayProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_day_progress, this);
            ButterKnife.inject(this, view);
        }
    }

    public ImageView getLeftStreak() {
        return mLeftStreak;
    }

    public ImageView getRightStreak() {
        return mRightStreak;
    }

    public CircularBar getCircularBar() {
        return mCircularBar;
    }
}
