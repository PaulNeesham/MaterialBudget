package com.flatlyapps.materialbudget.startup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlyapps.materialbudget.R;
import com.flatlyapps.materialbudget.startup.slide.FirstSlide;

import java.util.List;
import java.util.Vector;

/**
 * Created by PaulN on 02/08/2015.
 */
public class StartUpActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;
    private ViewPager pager;
    private final List<Fragment> fragments = new Vector<>();
    private int slidesNumber;
    private DefaultIndicatorController mController;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        this.setContentView(R.layout.intro_layout);
        final ImageView nextButton = (ImageView)this.findViewById(R.id.next);
        final TextView doneButton = (TextView)this.findViewById(R.id.done);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(@NonNull View v) {
                if(((ValidFragment)mPagerAdapter.getItem(pager.getCurrentItem())).isValid()) {
                    StartUpActivity.this.onNextPressed();
                }
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(@NonNull View v) {
                if (((ValidFragment) mPagerAdapter.getItem(pager.getCurrentItem())).isValid()) {
                    StartUpActivity.this.onDonePressed();
                }
            }
        });
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), this.fragments);
        this.pager = (ViewPager)this.findViewById(R.id.view_pager);
        this.pager.setAdapter(this.mPagerAdapter);
        this.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                StartUpActivity.this.mController.selectPosition(position);
                if (position == StartUpActivity.this.slidesNumber - 1) {
                    nextButton.setVisibility(View.GONE);
                    doneButton.setVisibility(View.VISIBLE);
                } else {
                    doneButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.init();
        this.slidesNumber = this.fragments.size();
        if(this.slidesNumber == 1) {
            nextButton.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
        }

        this.initController();
    }

    private void init() {
        addSlide(new FirstSlide());
        addSlide(new FirstSlide());
    }

    private void onNextPressed(){
        StartUpActivity.this.pager.setCurrentItem(StartUpActivity.this.pager.getCurrentItem() + 1);
    }

    private void onDonePressed() {
        loadMainActivity();
    }

    private void loadMainActivity() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
        edit.apply();
        finish();
    }

    private void initController() {
        if(this.mController == null) {
            this.mController = new DefaultIndicatorController();
        }

        FrameLayout indicatorContainer = (FrameLayout)this.findViewById(R.id.indicator_container);
        indicatorContainer.addView(this.mController.newInstance(this));
        this.mController.initialize(this.slidesNumber);
    }

    private void addSlide(@NonNull Fragment fragment) {
        this.fragments.add(fragment);
        this.mPagerAdapter.notifyDataSetChanged();
    }

}
