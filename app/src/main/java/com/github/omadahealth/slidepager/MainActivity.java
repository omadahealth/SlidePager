package com.github.omadahealth.slidepager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.omada.progressslider.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Sliders> sliders = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Sliders slider = new Sliders(this);
            slider.init(1.5f);
            sliders.add(slider);
        }


    }
}
