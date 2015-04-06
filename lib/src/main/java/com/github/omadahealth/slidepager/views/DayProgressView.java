package com.github.omadahealth.slidepager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.omadahealth.slidepager.lib.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/2/15.
 */
public class DayProgressView extends RelativeLayout {
//    @InjectView(R.id.day_progress_streak_left)
    ImageView mLeftStreak;
//    @InjectView(R.id.day_progress_streak_right)
    ImageView mRightStreak;
//    @InjectView(R.id.circularbar)
    CircularBar mCircularBar;
//    @InjectView(R.id.day_of_week)
    TextView mDayOfWeek;

    private static final int EASE_IN_DURATION = 500;

    public enum STREAK {
        LEFT_STREAK,
        RIGHT_STREAK
    }

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

    /**
     * Initiate the view and start butterknife injection
     * @param context
     */
    private void init(Context context) {
        if (!isInEditMode()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_day_progress, this);
            ButterKnife.inject(this, view);

            mLeftStreak = ButterKnife.findById(this, R.id.day_progress_streak_left);
            mRightStreak = ButterKnife.findById(this, R.id.day_progress_streak_right);
            mCircularBar = ButterKnife.findById(this, R.id.circularbar);
            mDayOfWeek = ButterKnife.findById(this, R.id.day_of_week);
        }
    }

    /**
     * Animate the progress from start to end for the {@link CircularBar} and the rest of the views in
     * this container
     * @param start 0-100
     * @param end 0-100
     */
    public void animateProgress(int start, int end, int duration){
        mCircularBar.setClockwiseReachedArcColor(end == 100 ? getContext().getResources().getColor(R.color.reached_green_color) :
                getContext().getResources().getColor(R.color.gray));
        mCircularBar.animateProgress(start, end, duration);
    }

    /**
     * Show or hide the streaks between the view
     * @param show True if to show, false otherwise
     * @param side The side to animate and change visibility
     */
    public void show(final boolean show, STREAK side) {
        AnimatorSet set = new AnimatorSet();
        View sideView = null;
        switch (side) {
            case LEFT_STREAK:
                sideView = mLeftStreak;
                break;
            case RIGHT_STREAK:
                sideView = mRightStreak;
                break;
            default:
                return;
        }
        float start = show ? 0 : 1f;
        float end = show ? 1f : 0;
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, EASE_IN_DURATION, ObjectAnimator.ofFloat(sideView, "alpha", start, end)));
        set.setDuration(EASE_IN_DURATION);
        final View finalSideView = sideView;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (show) {
                    finalSideView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show) {
                    finalSideView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    public void addAnimationListener(Animator.AnimatorListener listener){
        mCircularBar.addListener(listener);
    }

    public TextView getDayOfWeek() {
        return mDayOfWeek;
    }

    public CircularBar getCircularBar() {
        return mCircularBar;
    }
}
