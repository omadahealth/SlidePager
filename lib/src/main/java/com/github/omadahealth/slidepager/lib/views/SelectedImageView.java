package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by stoyan on 4/10/15.
 */
public class SelectedImageView extends ImageView {
    private boolean resetWidth = false;


    private int mSelectedViewId;

    public SelectedImageView(Context context) {
        this(context, null);
    }

    public SelectedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
