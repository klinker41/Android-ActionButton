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
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * Class which allows simple adding an interaction of an onscreen, circular image button
 */
public class ActionButton extends ImageButton {

    private static final String LOGTAG = "ActionButton";

    // some default constants for initializing the ActionButton
    public static final int DEFAULT_DISTANCE_FROM_BOTTOM = 50;
    public static final int DEFAULT_DISTANCE_FROM_RIGHT = 60;
    public static final int DEFAULT_SLIDE_ANIMATION_TIME = 150;
    public static final int DEFAULT_FADE_ANIMATION_TIME = 700;
    public static final int DEFAULT_WIDTH = 50;
    public static final int DEFAULT_HEIGHT = 50;
    public static final int DEFAULT_COLOR = 0xFFCC0000;
    public static final int DEFAULT_COLOR_SELECTED = 0xFFD94B4B;

    // animations that can be set for the ActionButton when it is showing and hiding
    public enum ActionButtonAnimation {
        SLIDE_FROM_BOTTOM, FADE
    }

    // set up default values
    private int distanceFromBottom = DEFAULT_DISTANCE_FROM_BOTTOM;
    private int distanceFromRight = DEFAULT_DISTANCE_FROM_RIGHT;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private boolean isShowing = false;
    private ActionButtonAnimation animation = ActionButtonAnimation.SLIDE_FROM_BOTTOM;

    /**
     * Default constructor
     *
     * @param context the context where ActionButton will be used
     */
    public ActionButton(Context context) {
        super(context);

        // set colors to their defaults in case user doesn't specifically implement them
        setColors(DEFAULT_COLOR, DEFAULT_COLOR_SELECTED);
    }

    /**
     * Creates a simple circle background to be applied behind the button
     *
     * @param color the main color of the circle
     * @param selectedColor the color to be displayed when button has been clicked
     */
    public void setColors(int color, int selectedColor) {
        // create an oval and set it to the main color
        ShapeDrawable normal = new ShapeDrawable(new OvalShape());
        normal.getPaint().setColor(color);

        // create a second oval and set it to selected color
        ShapeDrawable selected = new ShapeDrawable(new OvalShape());
        selected.getPaint().setColor(selectedColor);

        // create a state drawable which displays appropriate drawable according to the
        // current state of the ActionButton
        StateListDrawable back = new StateListDrawable();
        back.addState(new int[] {android.R.attr.state_pressed},
                selected);
        back.addState(new int[] {},
                normal);

        // set the background for this button
        setBackgroundDrawable(back);
    }

    /**
     * Sets how far away from the bottom of the screen the button should be displayed.
     * Distance should be the value in DP, it will be converted to the appropriate pixel value
     *
     * @param distance the distance from the bottom in dp
     */
    public void setDistanceFromBottom(int distance) {
        this.distanceFromBottom = distance;
    }

    /**
     * Sets how far away from the right side of the screen the button should be displayed.
     * Distance should be the value in DP, it will be converted to the appropriate pixel value
     *
     * @param distance the distance from the right in dp
     */
    public void setDistanceFromRight(int distance) {
        this.distanceFromRight = distance;
    }

    /**
     * Sets the width of the button. Distance should be the value in DP, it will be
     * converted to the appropriate pixel value
     *
     * @param width the width of the circle in dp
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the width of the button
     *
     * @return width of the button
     */
    public int getButtonWidth() {
        return this.width;
    }

    /**
     * Sets the height of the button. Distance should be the value in DP, it will be
     * converted to the appropriate pixel value
     *
     * @param height the height of the circle in dp
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the height of the button
     *
     * @return height of the button
     */
    public int getButtonHeight() {
        return this.height;
    }

    /**
     * Tells whether or not the button is currently showing on the screen.
     *
     * @return true if ActionButton is showing, false otherwise
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * Sets the animation for when the button shows and hides
     *
     * @param anim the animation to be used
     */
    public void setAnimation(ActionButtonAnimation anim) {
        this.animation = anim;
    }

    /**
     * Animates the ActionButton onto the screen so that the user may interact.
     * Animation occurs from the bottom of the screen, moving up until it reaches the
     * appropriate distance from the bottom.
     */
    public void show() {
        final Activity activity = (Activity) getContext();

        // set the correct width and height for ActionButton
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(toPx(activity, width), toPx(activity, height));
        this.setLayoutParams(params);

        // get the current content FrameLayout and add ActionButton to the top
        FrameLayout parent = (FrameLayout) activity.findViewById(android.R.id.content);
        parent.addView(this);

        // get the size of the screen so we know where to animate from and to
        float frameWidth = parent.getWidth();
        float frameHeight = parent.getHeight();

        // perform the animation with an object animator, default to sliding up from bottom
        setTranslationX(frameWidth - toPx(activity, distanceFromRight));
        ObjectAnimator animator;
        switch (animation) {
            case FADE:
                setTranslationY(frameHeight - toPx(activity, distanceFromBottom) - toPx(activity, distanceFromBottom));
                animator = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f);
                animator.setDuration(DEFAULT_FADE_ANIMATION_TIME);
                break;
            default:
                animator = ObjectAnimator.ofFloat(this, View.Y, frameHeight, frameHeight - toPx(activity, distanceFromBottom) - toPx(activity, height));
                animator.setInterpolator(new DecelerateInterpolator());
                animator.setDuration(DEFAULT_SLIDE_ANIMATION_TIME);
        }

        animator.start();

        isShowing = true;
    }

    /**
     * Animates the ActionButton off of the screen. Animation will go from its current position and
     * down until it is no longer being shown to the user.
     */
    public void hide() {
        final Activity activity = (Activity) getContext();

        // get size of screen
        FrameLayout parent = (FrameLayout) activity.findViewById(android.R.id.content);
        float frameWidth = parent.getWidth();
        float frameHeight = parent.getHeight();

        // perform animation
        setTranslationX(frameWidth - toPx(activity, distanceFromRight));
        ObjectAnimator animator;
        int animTime;
        switch (animation) {
            case FADE:
                animator = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f, 0.0f);
                animTime = DEFAULT_FADE_ANIMATION_TIME;
                break;
            default:
                animator = ObjectAnimator.ofFloat(this, View.Y, frameHeight - toPx(activity, distanceFromBottom) - toPx(activity, height), frameHeight + toPx(activity, height));
                animator.setInterpolator(new AccelerateInterpolator());
                animTime = DEFAULT_SLIDE_ANIMATION_TIME;
        }

        animator.setDuration(animTime);
        animator.start();

        // After animation has finished, remove the ActionButton from the content frame
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((FrameLayout) activity.findViewById(android.R.id.content)).removeView(ActionButton.this);
            }
        }, animTime);

        isShowing = false;
    }

    /**
     * Converts a dip value into a pixel value
     *
     * @param context the current context of the application
     * @param num the number to be converted to PX
     * @return the value of the number in PX
     */
    public static int toPx(Context context, int num) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, context.getResources().getDisplayMetrics());
    }
}
