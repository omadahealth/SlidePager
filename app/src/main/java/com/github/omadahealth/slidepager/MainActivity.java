package com.github.omadahealth.slidepager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.SlidePager;
import com.github.omadahealth.slidepager.lib.SlidePagerAdapter;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements OnSlidePageChangeListener {

    private SlidePager mSlidePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidePager = (SlidePager) findViewById(R.id.slidepager);
        mSlidePager.setAdapter(new SlidePagerAdapter(this, getPreviousDate(4), new Date()));
        mSlidePager.setPageTransformer(false, new SlideTransformerImpl());
//        mSlidePager.setOnPageChangeListener(this);
//        animatePage(0);
    }

    @Override
    public int getSlideProgress(int index) {
        return 100;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Returns a date from a time
     * @param weeks Number of weeks before today
     * @return
     */
    private Date getPreviousDate(int weeks){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        cal.add(Calendar.DAY_OF_YEAR, -1 * 7 * weeks);
        return new Date(cal.getTimeInMillis());
    }
}
