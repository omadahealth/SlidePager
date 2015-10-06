package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by stoyan on 4/10/15.
 */
public class SelectedImageView extends ImageView {
    /**
     * The tag for logging
     */
    private static final String TAG = "SelectedImageView";

    /**
     * Whether we should reset the width of this view to be twice the width of the screen.
     * Needed to prevent the view from clipping when we animate to left and right positions
     */
    private boolean resetWidth = false;

    /**
     * The resource id of the {@link ProgressView} that this view is set to, used to control
     * the translation of this view at the same rate in {@link com.github.omadahealth.slidepager.lib.SlideTransformer}
     */
    private int mSelectedViewId;

    public SelectedImageView(Context context) {
        this(context, null);
    }

    public SelectedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.post(new Runnable() {
            @Override
            public void run() {
                if (!resetWidth) {
                    getLayoutParams().width = 2 * getMeasuredWidth();
                    resetWidth = true;
                }
                resetView();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Resets the {@link #getLayoutParams()}  of the view
     */
    public void resetView() {
        setX(getLayoutParams().width / -4);
        requestLayout();
    }

    /**
     * Returns the resource id of the {@link ProgressView} that this view is set to, used to control
     * the translation of this view at the same rate in {@link com.github.omadahealth.slidepager.lib.SlideTransformer}
     *
     * @return Returns the resource id of the {@link ProgressView} that this view is set to
     */
    public int getSelectedViewId() {
        return mSelectedViewId;
    }

    /**
     * Sets {@link #mSelectedViewId}
     *
     * @param selectedViewId the resource id of the {@link ProgressView} that this view is ot be set to
     */
    public void setSelectedViewId(int selectedViewId) {
        this.mSelectedViewId = selectedViewId;
    }
}
