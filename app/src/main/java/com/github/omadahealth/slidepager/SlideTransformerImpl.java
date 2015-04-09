/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Omada Health, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.omadahealth.slidepager;

import com.github.omadahealth.demo.R;
import com.github.omadahealth.slidepager.lib.Ratio;

import java.util.LinkedHashMap;

/**
 * Created by oliviergoutay on 4/1/15.
 */
public class SlideTransformerImpl extends com.github.omadahealth.slidepager.lib.SlideTransformer {

    @Override
    public LinkedHashMap<Integer, Ratio> getViewRatios() {
        LinkedHashMap<Integer, Ratio> ratios = new LinkedHashMap<>();
        ratios.put(R.id.day_progress_1, new Ratio(4.0f, 1.0f));
        ratios.put(R.id.day_progress_2, new Ratio(3.5f, 1.5f));
        ratios.put(R.id.day_progress_3, new Ratio(3.0f, 2.0f));
        ratios.put(R.id.day_progress_4, new Ratio(2.5f, 2.5f));
        ratios.put(R.id.day_progress_5, new Ratio(2.0f, 3.0f));
        ratios.put(R.id.day_progress_6, new Ratio(1.5f, 3.5f));
        ratios.put(R.id.day_progress_7, new Ratio(1.0f, 4.0f));

        ratios.put(R.id.left_textview, new Ratio(4.0f, 1.0f));
        ratios.put(R.id.right_textview, new Ratio(1.0f, 4.0f));

        return ratios;
    }

}
