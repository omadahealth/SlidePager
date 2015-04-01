package com.github.omadahealth.slidepager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.SlidePager;

public class MainActivity extends ActionBarActivity {

    private SlidePager mSlidePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSlidePager = (SlidePager) findViewById(R.id.slidepager);
        View[] demoViews = initDummyViews();
        mSlidePager.setAdapter(new DemoPagerAdapter(this, demoViews));

        mSlidePager.setPageTransformer(false, new SlideTransformerImpl());
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
