package com.github.omadahealth.slidepager;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.Ratio;
import com.github.omadahealth.slidepager.lib.SlideTransformer;

import java.util.HashMap;

/**
 * Created by oliviergoutay on 4/1/15.
 */
public class SlideTransformerImpl extends SlideTransformer {

    @Override
    public HashMap<Integer, Ratio> getViewRatios() {
        HashMap<Integer, Ratio> ratios = new HashMap<>();
        ratios.put(R.id.imageview_1, new Ratio(4.0f, 1.0f));
        ratios.put(R.id.imageview_2, new Ratio(3.5f, 1.5f));
        ratios.put(R.id.imageview_3, new Ratio(3.0f, 2.0f));
        ratios.put(R.id.imageview_4, new Ratio(2.5f, 2.5f));
        ratios.put(R.id.imageview_5, new Ratio(2.0f, 3.0f));
        ratios.put(R.id.imageview_6, new Ratio(1.5f, 3.5f));
        ratios.put(R.id.imageview_7, new Ratio(1.0f, 4.0f));
        return ratios;
    }

}
