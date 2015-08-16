package com.flatlyapps.materialbudget.startup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flatlyapps.materialbudget.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PaulN on 02/08/2015.
 */
class DefaultIndicatorController {
    private Context mContext;
    private LinearLayout mDotLayout;
    private List<ImageView> mDots;
    private int mSlideCount;

    DefaultIndicatorController() {
    }

    public View newInstance(@NonNull Context context) {
        this.mContext = context;
        this.mDotLayout = (LinearLayout) View.inflate(context, R.layout.default_indicator, null);
        return this.mDotLayout;
    }

    public void initialize(int slideCount) {
        this.mDots = new ArrayList<>();
        this.mSlideCount = slideCount;

        for(int i = 0; i < slideCount; ++i) {
            ImageView dot = new ImageView(this.mContext);
            dot.setImageDrawable(getDrawable(R.drawable.indicator_dot_grey));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            this.mDotLayout.addView(dot, params);
            this.mDots.add(dot);
        }

        this.selectPosition(0);
    }

    public void selectPosition(int index) {
        for(int i = 0; i < this.mSlideCount; ++i) {
            int drawableId = i == index? R.drawable.indicator_dot_white: R.drawable.indicator_dot_grey;
            Drawable drawable = getDrawable(drawableId);
            this.mDots.get(i).setImageDrawable(drawable);
        }
    }

    @SuppressLint("NewApi")
    private Drawable getDrawable(@DrawableRes int drawableId) {
        return Build.VERSION.SDK_INT >= 21?this.mContext.getDrawable(drawableId):this.mContext.getResources().getDrawable(drawableId);
    }
}