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

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class ActionButton extends ImageButton {

    private static final String LOGTAG = "ActionButton";

    private static final int DEFAULT_DISTANCE_FROM_BOTTOM = 100;
    public static final int DEFAULT_DISTANCE_FROM_RIGHT = 60;
    private static final int DEFAULT_ANIMATION_TIME = 150;
    public static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;

    private static final int DEFAULT_COLOR = 0xFFCC0000;
    private static final int DEFAULT_COLOR_SELECTED = 0xFFD94B4B;

    public ActionButton(Context context) {
        super(context);
        setColors(DEFAULT_COLOR, DEFAULT_COLOR_SELECTED);
    }

    public ActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setColors(int color, int selectedColor) {
        ShapeDrawable normal = new ShapeDrawable(new OvalShape());
        normal.getPaint().setColor(color);

        ShapeDrawable selected = new ShapeDrawable(new OvalShape());
        selected.getPaint().setColor(selectedColor);

        StateListDrawable back = new StateListDrawable();
        back.addState(new int[] {android.R.attr.state_pressed},
                selected);
        back.addState(new int[] {},
                normal);
        setBackgroundDrawable(back);
    }

    private int distanceFromBottom = DEFAULT_DISTANCE_FROM_BOTTOM;
    public void setDistanceFromBottom(int distance) {
        this.distanceFromBottom = distance;
    }

    private int distanceFromRight = DEFAULT_DISTANCE_FROM_RIGHT;
    public void setDistanceFromRight(int distance) {
        this.distanceFromRight = distance;
    }

    private int width = DEFAULT_WIDTH;
    public void setWidth(int width) {
        this.width = width;
    }

    private int height = DEFAULT_HEIGHT;
    public void setHeight(int height) {
        this.height = height;
    }

    private boolean isShowing = false;
    public boolean isShowing() {
        return isShowing;
    }

    public void show() {
        final Activity activity = (Activity) getContext();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(toDp(activity, width), toDp(activity, height));
        this.setLayoutParams(params);

        FrameLayout parent = (FrameLayout) activity.findViewById(android.R.id.content);
        parent.addView(this);

        // get the size of the screen so we know where to animate from and to
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float screenWidth = size.x;
        float screenHeight = size.y;

        // perform the animation with an object animator
        setTranslationX(screenWidth - toDp(activity, distanceFromRight));
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, View.Y, screenHeight, screenHeight - toDp(activity, distanceFromBottom) - toDp(activity, DEFAULT_HEIGHT));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(DEFAULT_ANIMATION_TIME);
        animator.start();

        isShowing = true;
    }

    public void hide() {
        final Activity activity = (Activity) getContext();

        // get size of screen
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float screenWidth = size.x;
        float screenHeight = size.y;

        // perform animation
        setTranslationX(screenWidth - toDp(activity, distanceFromRight));
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, View.Y, screenHeight - toDp(activity, distanceFromBottom) - toDp(activity, DEFAULT_HEIGHT), screenHeight + toDp(activity, height));
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(DEFAULT_ANIMATION_TIME);
        animator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((FrameLayout) activity.findViewById(android.R.id.content)).removeView(ActionButton.this);
            }
        }, DEFAULT_ANIMATION_TIME);

        isShowing = false;
    }

    private static final int toDp(Context context, int num) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, context.getResources().getDisplayMetrics());
    }
}
