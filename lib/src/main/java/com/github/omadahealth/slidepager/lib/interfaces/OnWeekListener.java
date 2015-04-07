package com.github.omadahealth.slidepager.lib.interfaces;

/**
 * Created by stoyan on 4/7/15.
 */
public interface OnWeekListener {

    /**
     * Called when a day is clicked in the view
     * @param index The index of the day, ie. Sunday = 0, Monday = 1...
     */
    void onDaySelected(int index);
}
