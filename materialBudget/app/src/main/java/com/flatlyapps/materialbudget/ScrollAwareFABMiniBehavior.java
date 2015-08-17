package com.flatlyapps.materialbudget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by PaulN on 14/08/2015.
 */
public class ScrollAwareFABMiniBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABMiniBehavior(Context context, AttributeSet attrs) {
        super();
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

        String tag = (String)child.getTag();
        if (tag != null  && tag.equals("open")) {
            if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
            }
            if (dxConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
            }
        }
    }
}
