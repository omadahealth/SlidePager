package com.github.omadahealth.slidepager.lib.utils;

import java.util.List;

/**
 * Created by stoyan on 4/15/15.
 */
public class ProgressAttr {

    /**
     * Progress from 0-100
     */
    private int mProgress;


    /**
     * list of booleans mapping to parts of circle arcs. true to animate those part false to not.
     */
    private List<Boolean> mHaveToAnimateCirclePieces;

    /**
     * Whether this is a special day, and is colored in differently
     * when the page translates. Check :
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialFillColor}
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialOutlineColor}
     * {@link com.github.omadahealth.slidepager.lib.views.ProgressView#mSpecialReachColor}
     */
    private boolean mSpecial;

    /**
     * Determines if this {@link ProgressAttr} is in the future or not, specifying different thickness for the {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     */
    private boolean mIsFuture;

    /**
     * Determines the color for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * reachedColor. Uses the default style color if set to null.
     */
    private Integer mReachedColor;

    /**
     * Determines the color for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * completedColor. Uses the default style color if set to null.
     */
    private Integer mCompletedColor;

    /**
     * Determines the color filled for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * completedColor. Uses the default style color if set to null.
     */
    private Integer mCompletedFillColor;

    /**
     * Determines the drawable for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * when progress is 100%. Uses the default drawable if set to null.
     */
    private Integer mCompletedDrawable;

    /**
     * Determine if a {@link com.github.omadahealth.slidepager.lib.views.ProgressView} should display a streak leftwards off the current page.
     * True it should, false it shouldn't
     */
    private boolean mStreakLeftOffScreen = false;

    /**
     * Determine if a {@link com.github.omadahealth.slidepager.lib.views.ProgressView} should display a streak leftwards off the current page
     * True it should, false it shouldn't
     */
    private boolean mStreakRightOffScreen = false;

    /**
     * New completed percentage value. Default is 100
     */
    private Double mCompletedPercentage = null;

    /**
     * Determines the color for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * notCompletedOutlineColor. Uses the default style color if set to null.
     */
    private Integer mNotCompletedOutlineColor;

    /**
     * Determines the color for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * specialDayFillColor. Uses the default style color if set to null.
     */
    private Integer mSpecialFillColor;
    /**
     * Determines the color for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * specialDayOutlineColor. Uses the default style color if set to null.
     */
    private Integer mSpecialOutlineColor;
    /**
     * Determines the color for this specific {@link com.github.omadahealth.slidepager.lib.views.ProgressView} {@link com.github.omadahealth.circularbarpager.library.CircularBar}
     * specialDayReachColor. Uses the default style color if set to null.
     */
    private Integer mSpecialReachColor;

    public ProgressAttr(int progress, boolean special, boolean isFuture) {
        this.mProgress = progress;
        this.mSpecial = special;
        this.mIsFuture = isFuture;
        this.mReachedColor = null;
        this.mCompletedColor = null;
        this.mCompletedDrawable = null;
    }


    public ProgressAttr(int progress, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedDrawable) {
        this.mProgress = progress;
        this.mSpecial = special;
        this.mIsFuture = isFuture;
        this.mReachedColor = reachedColor;
        this.mCompletedColor = completedColor;
        this.mCompletedDrawable = completedDrawable;
    }

    public ProgressAttr(int progress, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedDrawable, boolean streakLeftOffScreen, boolean streakRightOffScreen) {
        this.mProgress = progress;
        this.mSpecial = special;
        this.mIsFuture = isFuture;
        this.mReachedColor = reachedColor;
        this.mCompletedColor = completedColor;
        this.mCompletedDrawable = completedDrawable;
        this.mStreakLeftOffScreen = streakLeftOffScreen;
        this.mStreakRightOffScreen = streakRightOffScreen;
    }

    public ProgressAttr(int progress, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedFillColor, Integer completedDrawable, boolean streakLeftOffScreen, boolean streakRightOffScreen) {
        this.mProgress = progress;
        this.mSpecial = special;
        this.mIsFuture = isFuture;
        this.mReachedColor = reachedColor;
        this.mCompletedColor = completedColor;
        this.mCompletedFillColor = completedFillColor;
        this.mCompletedDrawable = completedDrawable;
        this.mStreakLeftOffScreen = streakLeftOffScreen;
        this.mStreakRightOffScreen = streakRightOffScreen;
    }

    public ProgressAttr(int progress, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedFillColor, Integer notCompletedOutlineColor, Integer specialOutlineColor, Integer specialReachColor, Integer specialFillColor, Integer completedDrawable, Double completedPercentage) {
        this.mProgress = progress;
        this.mSpecial = special;
        this.mIsFuture = isFuture;
        this.mReachedColor = reachedColor;
        this.mCompletedColor = completedColor;
        this.mCompletedFillColor = completedFillColor;
        this.mNotCompletedOutlineColor = notCompletedOutlineColor;
        this.mSpecialFillColor = specialFillColor;
        this.mSpecialOutlineColor = specialOutlineColor;
        this.mSpecialReachColor = specialReachColor;
        this.mCompletedDrawable = completedDrawable;
        this.mCompletedPercentage = completedPercentage;
    }
    public ProgressAttr(int progress, List<Boolean> haveToAnimateCirclePieces, boolean special, boolean isFuture, Integer reachedColor, Integer completedColor, Integer completedFillColor, Integer notCompletedOutlineColor, Integer specialOutlineColor, Integer specialReachColor, Integer specialFillColor, Integer completedDrawable, Double completedPercentage) {
        this.mProgress = progress;
        this.mHaveToAnimateCirclePieces = haveToAnimateCirclePieces;
        this.mSpecial = special;
        this.mIsFuture = isFuture;
        this.mReachedColor = reachedColor;
        this.mCompletedColor = completedColor;
        this.mCompletedFillColor = completedFillColor;
        this.mNotCompletedOutlineColor = notCompletedOutlineColor;
        this.mSpecialFillColor = specialFillColor;
        this.mSpecialOutlineColor = specialOutlineColor;
        this.mSpecialReachColor = specialReachColor;
        this.mCompletedDrawable = completedDrawable;
        this.mCompletedPercentage = completedPercentage;
    }


    public boolean isSpecial() {
        return mSpecial;
    }

    public void setSpecial(boolean special) {
        this.mSpecial = special;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public List<Boolean> getHaveToAnimateCirclePieces() {
        return mHaveToAnimateCirclePieces;
    }

    public void setHaveToAnimateCirclePieces(List<Boolean> haveToAnimateCirclePieces) {
        this.mHaveToAnimateCirclePieces = haveToAnimateCirclePieces;
    }

    public boolean isFuture() {
        return mIsFuture;
    }

    public void setIsFuture(boolean isFuture) {
        this.mIsFuture = isFuture;
    }

    public Integer getReachedColor() {
        return mReachedColor;
    }

    public void setReachedColor(Integer reachedColor) {
        this.mReachedColor = reachedColor;
    }

    public Integer getCompletedColor() {
        return mCompletedColor;
    }

    public void setCompletedColor(Integer completedColor) {
        this.mCompletedColor = completedColor;
    }

    public Integer getCompletedFillColor() {
        return mCompletedFillColor;
    }

    public void setCompletedFillColor(Integer completedFillColor) {
        this.mCompletedFillColor = completedFillColor;
    }

    public Integer getCompletedDrawable() {
        return mCompletedDrawable;
    }

    public void setCompletedDrawable(Integer completedDrawable) {
        this.mCompletedDrawable = completedDrawable;
    }

    public boolean isStreakLeftOffScreen() {
        return mStreakLeftOffScreen;
    }

    public boolean isStreakRightOffScreen() {
        return mStreakRightOffScreen;
    }

    public Double getCompletedPercentage() {
        return mCompletedPercentage;
    }

    public void setCompletedPercentage(Double completedPercentage) {
        this.mCompletedPercentage = completedPercentage;
    }

    public Integer getNotCompletedOutlineColor() {
        return mNotCompletedOutlineColor;
    }

    public void setNotCompletedOutlineColor(Integer notCompletedOutlineColor) {
        this.mNotCompletedOutlineColor = notCompletedOutlineColor;
    }

    public Integer getSpecialFillColor() {
        return mSpecialFillColor;
    }

    public void setSpecialFillColor(Integer specialFillColor) {
        this.mSpecialFillColor = specialFillColor;
    }

    public Integer getSpecialOutlineColor() {
        return mSpecialOutlineColor;
    }

    public void setSpecialOutlineColor(Integer specialOutlineColor) {
        this.mSpecialOutlineColor = specialOutlineColor;
    }

    public Integer getSpecialReachColor() {
        return mSpecialReachColor;
    }

    public void setSpecialReachColor(Integer specialReachColor) {
        this.mSpecialReachColor = specialReachColor;
    }
}
