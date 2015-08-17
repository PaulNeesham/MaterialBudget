package com.flatlyapps.materialbudget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by PaulN on 14/08/2015.
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    private final Context context;

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
        this.context = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
            ((MainActivity) context).hideLabels();
            child.setTag("closed");
            final OvershootInterpolator interpolator = new OvershootInterpolator();

            ViewCompat.animate(child).rotation(0f).withLayer().setDuration(300).setInterpolator(interpolator).start();
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }

        if (dxConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
            ((MainActivity) context).hideLabels();
            child.setTag("closed");
            final OvershootInterpolator interpolator = new OvershootInterpolator();

            ViewCompat.animate(child).rotation(0f).withLayer().setDuration(300).setInterpolator(interpolator).start();
        } else if (dxConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }
}
