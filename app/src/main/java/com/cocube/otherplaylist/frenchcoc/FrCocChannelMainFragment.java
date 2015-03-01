/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
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

package com.cocube.otherplaylist.frenchcoc;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.cocube.R;
import com.cocube.viewpager.PagerSlidingTabStrip;

public class FrCocChannelMainFragment extends SherlockFragment{

    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FrCocChannelPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF2d3586;
    private String mCurrentYoutubeId;
    private int mYoutubeState;
    private int mCurrentPage;

    private int mCurrentPagerItemIndex = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.main_fragment, container, false);


        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();


//        setContentView(R.layout.main_with_menu);

        // tabs --> pager --> adapter

        tabs = (PagerSlidingTabStrip) getView().findViewById(R.id.tabs);

        pager = (ViewPager) getView().findViewById(R.id.pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        adapter = new FrCocChannelPagerAdapter(getView().getContext(), getChildFragmentManager());

        pager.setAdapter(adapter);
        pager.setCurrentItem(mCurrentPagerItemIndex);

        tabs.setViewPager(pager);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        mCurrentPagerItemIndex = pager.getCurrentItem();

    }


    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

}