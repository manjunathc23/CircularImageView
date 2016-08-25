package com.github.manjunathc23.params;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;

public class CircularImageOptionsParams {

    public static final int INVALID_RES = -1;

    @ColorRes
    private int borderColorRes;

    @ColorRes
    private int fillColorRes;

    @ColorInt
    private int fillColor;

    @ColorInt
    private int borderColor;

    @DimenRes
    private int borderWidth;

    private boolean showBorderOverlay;

    private boolean disableCircularTransformation;

    @ColorRes
    public int getBorderColorRes() {
        return borderColorRes;
    }

    @ColorInt
    public int getFillColor() {
        return fillColor;
    }

    @ColorRes
    public int getFillColorRes() {
        return fillColorRes;
    }

    @ColorInt
    public int getBorderColor() {
        return borderColor;
    }

    @DimenRes
    public int getBorderWidth() {
        return borderWidth;
    }

    public boolean isShowBorderOverlay() {
        return showBorderOverlay;
    }

    public boolean isDisableCircularTransformation() {
        return disableCircularTransformation;
    }

    private CircularImageOptionsParams(@ColorRes int borderColorRes,
                                       @ColorInt int fillColor,
                                       @ColorRes int fillColorRes,
                                       @ColorInt int borderColor,
                                       @DimenRes int borderWidth,
                                       boolean showBorderOverlay,
                                       boolean disableCircularTransformation) {
        //Use CircularImageOptionsParams.CircularImageBuilder
        // class to construct the params object

        this.borderColorRes = borderColorRes;
        this.fillColor = fillColor;
        this.fillColorRes = fillColorRes;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.showBorderOverlay = showBorderOverlay;
        this.disableCircularTransformation = disableCircularTransformation;
    }

    public static class CircularImageBuilder {

        public CircularImageBuilder() {
            borderColorRes = INVALID_RES;
            fillColorRes = INVALID_RES;
            fillColor = INVALID_RES;
            borderColor = INVALID_RES;
            borderWidth = INVALID_RES;
        }

        @ColorRes
        private int borderColorRes;

        @ColorRes
        private int fillColorRes;

        @ColorInt
        private int fillColor;

        @ColorInt
        private int borderColor;

        @DimenRes
        private int borderWidth;

        private boolean showBorderOverlay;

        private boolean disableCircularTransformation;

        public CircularImageBuilder setBorderColorRes(@ColorRes int borderColorRes) {
            this.borderColorRes = borderColorRes;
            return this;
        }

        public CircularImageBuilder setFillColor(@ColorInt int fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        public CircularImageBuilder setFillColorRes(@ColorRes int fillColorRes) {
            this.fillColorRes = fillColorRes;
            return this;
        }

        public CircularImageBuilder setBorderColor(@ColorInt int borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public CircularImageBuilder setBorderWidth(@DimenRes int borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public CircularImageBuilder setShowBorderOverlay(boolean showBorderOverlay) {
            this.showBorderOverlay = showBorderOverlay;
            return this;
        }

        public CircularImageBuilder setDisableCircularTransformation(boolean disableCircularTransformation) {
            this.disableCircularTransformation = disableCircularTransformation;
            return this;
        }

        public CircularImageOptionsParams build() {
            return new CircularImageOptionsParams(borderColorRes, fillColor, fillColorRes,
                    borderColor, borderWidth, showBorderOverlay, disableCircularTransformation);
        }
    }
}
