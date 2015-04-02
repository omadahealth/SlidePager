package com.github.omadahealth.slidepager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.SlidePager;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    private SlidePager mSlidePager;

    /**
     * The animation time in milliseconds that we take to display the steps taken
     */
    private static final int BAR_ANIMATION_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSlidePager = (SlidePager) findViewById(R.id.slidepager);
        View[] demoViews = initDummyViews();
        mSlidePager.setAdapter(new DemoPagerAdapter(this, demoViews));

        mSlidePager.setPageTransformer(false, new SlideTransformerImpl());

        mSlidePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            @SuppressWarnings("unchecked")
            public void onPageSelected(int position) {
                View selectedView = ((DemoPagerAdapter) mSlidePager.getAdapter()).getViews()[position];

                List<View> children = (List<View>) selectedView.getTag();
                if (children != null) {
                    int i = 0;
                    for (View child : children) {
                        if (child instanceof CircularBar) {
                            CircularBar circularBar = (CircularBar) child;
                            circularBar.animateProgress(0, (14 * i++), BAR_ANIMATION_TIME);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private View[] initDummyViews() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View[] views = new View[4];
        views[0] = inflater.inflate(R.layout.page, null);
        views[1] = inflater.inflate(R.layout.page, null);
        views[2] = inflater.inflate(R.layout.page, null);
        views[3] = inflater.inflate(R.layout.page, null);
        return views;
    }
}
