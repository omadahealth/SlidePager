package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.github.omadahealth.slidepager.lib.R;

/**
 * Created by stoyan on 4/10/15.
 */
public class SelectedImageView extends ImageView {
    private boolean resetWidth = false;

    private Drawable mDividerDrawable;
    private Drawable mSelectedDrawable;

    private int mSelectedViewId;

    public SelectedImageView(Context context) {
        this(context, null);
    }

    public SelectedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (!resetWidth) {
            getLayoutParams().width = 2 * w;
            resetWidth = true;
        }
        resetView();
    }

    /**
     * Initiate the view and drawables
     */
    private void init() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            mDividerDrawable = getResources().getDrawable(R.mipmap.day_divider, null);
            mSelectedDrawable = getResources().getDrawable(R.mipmap.ic_current_day, null);
        }else{
            mDividerDrawable = getResources().getDrawable(R.mipmap.day_divider);
            mSelectedDrawable = getResources().getDrawable(R.mipmap.ic_current_day);
        }
    }

    /**
     * Sets the drawable resource for {@link #setBackground(Drawable)} depending on the
     * state of the view
     * @param selected True to set background to {@link #mSelectedDrawable}, false for {@link #mDividerDrawable}
     */
    public void setSelectedDrawable(boolean selected){
        Drawable drawable = selected ? mSelectedDrawable : mDividerDrawable;
        if(Build.VERSION.SDK_INT  > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    /**
     * Resets the {@link #getLayoutParams()}  of the view
     */
    public void resetView(){
        setX(getLayoutParams().width / -4);
        requestLayout();
    }


    public int getSelectedViewId() {
        return mSelectedViewId;
    }

    public void setSelectedViewId(int selectedViewId) {
        this.mSelectedViewId = selectedViewId;
    }
}
