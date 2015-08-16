package com.flatlyapps.materialBudget.startup.slide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flatlyapps.materialBudget.R;
import com.flatlyapps.materialBudget.startup.ValidFragment;


/**
 * Created by PaulN on 02/08/2015.
 */
public class FirstSlide extends Fragment implements ValidFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro, container, false);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
