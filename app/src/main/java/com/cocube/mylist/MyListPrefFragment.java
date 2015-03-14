package com.cocube.mylist;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cocube.R;

/**
 * Created by namh on 2015-03-07.
 */
public class MyListPrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.layout.fragment_mylist);
    }

}
