package com.github.omadahealth.slidepager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by oliviergoutay on 4/1/15.
 */
public class Sliders extends RelativeLayout implements SliderInterface {

    private float mRatio;

    public Sliders(Context context) {
        super(context);
    }

    public Sliders(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Sliders(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(float ratio) {
        this.mRatio = ratio;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onTranslate(View view, float progress) {
        this.setTranslationX(-progress * ((float) view.getWidth() / mRatio));
    }

    @Override
    public void onStart(View view) {
    }

    @Override
    public void onStop(View view) {

    }
}
