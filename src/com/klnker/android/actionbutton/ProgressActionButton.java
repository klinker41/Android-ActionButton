/*
 * Copyright 2013 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klnker.android.actionbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Expansion of ActionButton which allows a progress to be drawn around it
 */
public class ProgressActionButton extends ActionButton {

    private static final String LOGTAG = "ProgressActionButton";

    public static final int DEFAULT_PROGRESS_COLOR = 0xCCFFFFFF;
    public static final int DEFAULT_MAX_PROGRESS = 100;

    private int maxProgress = DEFAULT_MAX_PROGRESS;
    private int progressColor = DEFAULT_PROGRESS_COLOR;
    private int currentProgress = 0;
    private RectF oval;
    private Paint progressPaint;

    /**
     * Default Constructor
     *
     * @param context the context of your activity
     */
    public ProgressActionButton(Context context) {
        super(context);
        setOval();

        // set up the paint to draw the progress oval with
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(toDp(context, 3));
    }

    /**
     * sets the color of the progress bar stroke
     * @param color color int to be used
     */
    private void setProgressColor(int color) {
        this.progressColor = color;
    }

    /**
     * Set the current progress
     * @param progress progress out of the max progress
     */
    public void setProgress(int progress) {
        this.currentProgress = progress;

        // invalidate view so it is redrawn
        postInvalidate();

        // if we are done, post a hiding of the view to the ui thread
        if (progress == maxProgress) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            });
        }
    }

    /**
     * gets the current amount of progress
     *
     * @return the current progress completed
     */
    public int getProgress() {
        return this.currentProgress;
    }

    /**
     * Sets the max progress to go through
     *
     * @param progress the max progress of the animation
     */
    public void setMaxProgress(int progress) {
        this.maxProgress = progress;
    }

    /**
     * Gets the max progress
     *
     * @return the max progress to go through
     */
    public int getMaxProgress() {
        return this.maxProgress;
    }

    /**
     * Sets the stroke width for the progress circle
     * @param stroke the stroke width, which will be converted to dips automatically
     */
    public void setStrokeWidth(int stroke) {
        progressPaint.setStrokeWidth(toDp(getContext(), stroke));
    }

    /**
     * Sets up the oval that we will draw the progress circle with accordance to
     */
    private void setOval() {
        this.oval = new RectF(
                toDp(getContext(), 5),
                toDp(getContext(), 5),
                toDp(getContext(), getButtonWidth() - 5),
                toDp(getContext(), getButtonHeight() - 5)
        );
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        setOval();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        setOval();
    }

    /**
     * Draws the normal button, plus the progress around it
     *
     * @param canvas the canvas to use for drawing
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float progress = ((float) currentProgress) / maxProgress;
        float startAngle = -90;
        float sweepAngle = 360 * progress;

        canvas.drawArc(oval, startAngle, sweepAngle, false, progressPaint);
    }
}
