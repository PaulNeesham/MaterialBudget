package com.flatlyapps.materialBudget.tab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Paul on 04/07/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private final Context context;


    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position){

        if (position == 0) {
            return SummaryCardsFragment.getTitle();
        } else if (position == 1) {
            return DataListFragment.getTitle();
        } else if (position == 2) {
            return RecurListFragment.getTitle();
        } else {
            return AccountCardsFragment.getTitle();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return SummaryCardsFragment.newInstance();
        } else if (position == 1) {
            return DataListFragment.newInstance();
        } else if (position == 2) {
            return RecurListFragment.newInstance();
        } else {
            return AccountCardsFragment.newInstance();
        }
    }

}