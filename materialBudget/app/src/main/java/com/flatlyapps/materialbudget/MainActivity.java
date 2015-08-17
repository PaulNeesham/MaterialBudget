package com.flatlyapps.materialbudget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.flatlyapps.materialbudget.add.AddActivity;
import com.flatlyapps.materialbudget.startup.StartUpActivity;
import com.flatlyapps.materialbudget.tab.TabsAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by PaulN on 13/08/2015.
 */
public class MainActivity extends AppCompatActivity {

    public static final String UPDATE_INTENT = "com.budget3 Transactions Updated";
    private ReceiveMessages myReceiver = null;
    private Boolean myReceiverIsRegistered = false;
    private FloatingActionButton fabData;
    private FloatingActionButton fabMini1;
    private FloatingActionButton fabMini2;
    private FloatingActionButton fabMini3;
    private ViewPager tabsPager;
    private TextView fabMiniLabel1;
    private TextView fabMiniLabel2;
    private TextView fabMiniLabel3;

    public void hideLabels() {
        ViewCompat.animate(fabMiniLabel1).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
        ViewCompat.animate(fabMiniLabel2).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
        ViewCompat.animate(fabMiniLabel3).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
    }

    public class ReceiveMessages extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(MainActivity.UPDATE_INTENT)) {
                MainActivity.this.checkForChanges();
            }
        }
    }

    private void checkForChanges() {
        //todo update current views
    }


    @Override
    protected void onPause() {
        if (myReceiverIsRegistered) {
            unregisterReceiver(myReceiver);
            myReceiverIsRegistered = false;
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new ReceiveMessages();
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabData = (FloatingActionButton) findViewById(R.id.fab_add_data);
        fabMini1 = (FloatingActionButton) findViewById(R.id.fab_add_mini_transfer);
        fabMiniLabel1 = (TextView) findViewById(R.id.fab_add_mini_label_transfer);
        fabMiniLabel1.setScaleX(0.0f);
        fabMiniLabel1.setScaleY(0.0f);

        fabMini2 = (FloatingActionButton) findViewById(R.id.fab_add_mini_expense);
        fabMiniLabel2 = (TextView) findViewById(R.id.fab_add_mini_label_expense);
        fabMiniLabel2.setScaleX(0.0f);
        fabMiniLabel2.setScaleY(0.0f);
        fabMini3 = (FloatingActionButton) findViewById(R.id.fab_add_mini_income);
        fabMiniLabel3 = (TextView) findViewById(R.id.fab_add_mini_label_income);
        fabMiniLabel3.setScaleX(0.0f);
        fabMiniLabel3.setScaleY(0.0f);
        fabData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabsPager.getCurrentItem() != 3) {
                    String open = (String) fabData.getTag();
                    if (open == null || open.equals("closed")) {
                        final OvershootInterpolator interpolator = new OvershootInterpolator();
                        ViewCompat.animate(fabData).rotation(135f).withLayer().setDuration(300).setInterpolator(interpolator).start();
                        fabMini1.show();
                        fabMini1.setTag("open");
                        fabMini2.show();
                        fabMini2.setTag("open");
                        fabMini3.show();
                        fabMini3.setTag("open");
                        fabData.setTag("open");
                        fabMiniLabel1.setVisibility(View.VISIBLE);
                        fabMiniLabel2.setVisibility(View.VISIBLE);
                        fabMiniLabel3.setVisibility(View.VISIBLE);
                        ViewCompat.animate(fabMiniLabel1).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                        ViewCompat.animate(fabMiniLabel2).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                        ViewCompat.animate(fabMiniLabel3).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();

                    } else {
                        final OvershootInterpolator interpolator = new OvershootInterpolator();
                        ViewCompat.animate(fabData).rotation(0f).withLayer().setDuration(300).setInterpolator(interpolator).start();
                        fabMini1.hide();
                        fabMini1.setTag("closed");
                        fabMini2.hide();
                        fabMini2.setTag("closed");
                        fabMini3.hide();
                        fabMini3.setTag("closed");
                        fabData.setTag("closed");
                        ViewCompat.animate(fabMiniLabel1).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                        ViewCompat.animate(fabMiniLabel2).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                        ViewCompat.animate(fabMiniLabel3).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                    }
                }
            }
        });

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);
        tabsPager = (ViewPager) findViewById(R.id.tabs_pager);
        tabsPager.setAdapter(tabsAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_layout);
        tabLayout.setupWithViewPager(tabsPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            int current = 0;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabsPager.setCurrentItem(tab.getPosition());
                animateFab(current, tab.getPosition());
                current = tabsPager.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        int shadowColor = getResources().getColor(R.color.primary_dark);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setTintColor(shadowColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (!previouslyStarted) {
            Intent startUpIntent = new Intent(this, StartUpActivity.class);
            startActivity(startUpIntent);
        } else if (!myReceiverIsRegistered) {
            registerReceiver(myReceiver, new IntentFilter(UPDATE_INTENT));
            myReceiverIsRegistered = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_about_us:
                Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutUsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
    int[] iconIntArray = {R.drawable.ic_add_white,R.drawable.ic_add_white,R.drawable.ic_add_white,R.drawable.ic_account_add_white};

    protected void animateFab(final int currentPosition, final int newPosition) {

        fabData.clearAnimation();
        if (fabData.getVisibility() != View.VISIBLE) {
            fabData.setImageResource(iconIntArray[newPosition]);
            fabData.show();
        } else {
            if((newPosition ==3 && currentPosition !=3)|| ((newPosition !=3 && currentPosition ==3))) {
                final ViewPropertyAnimatorListener listener2 = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        fabData.show();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }

                };
                ViewPropertyAnimatorListener listener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        fabData.hide();
                        fabData.setImageResource(iconIntArray[newPosition]);
                        ViewCompat.animate(fabData).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(listener2).start();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }

                };

                ViewCompat.animate(fabData).rotation(0.0F).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(listener).start();
                fabMini1.hide();
                fabMini1.setTag("closed");
                fabMini2.hide();
                fabMini2.setTag("closed");
                fabMini3.hide();
                fabMini3.setTag("closed");
                ViewCompat.animate(fabMiniLabel1).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                ViewCompat.animate(fabMiniLabel2).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                ViewCompat.animate(fabMiniLabel3).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                fabData.setTag("closed");
            }
        }
    }

}
