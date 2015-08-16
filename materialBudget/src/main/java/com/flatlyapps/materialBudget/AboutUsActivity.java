package com.flatlyapps.materialBudget;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by PaulN on 02/08/2015.
 */
public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);

        int shadowColor = getResources().getColor(R.color.primary_dark);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setTintColor(shadowColor);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LibsFragment fragment = new LibsBuilder()
                .withFields(R.string.class.getFields())
                .withLibraries("systembartint", "AppIntro")
                .withAutoDetect(false)
                .withAboutIconShown(true)
                .withAboutDescription("blah blah blah")
                .withAboutVersionShown(true)
                .fragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

}
