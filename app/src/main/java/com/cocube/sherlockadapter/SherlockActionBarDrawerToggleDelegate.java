/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.cocube.sherlockadapter;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SherlockActionBarDrawerToggleDelegate implements ActionBarDrawerToggle.Delegate {

    private static final int[] THEME_ATTRS = new int[]{
            com.actionbarsherlock.R.attr.homeAsUpIndicator
    };

    private Activity mActivity;
    private final ActionBar mActionBar;

    public SherlockActionBarDrawerToggleDelegate(Activity activity) {
        if (!(activity instanceof SherlockFragmentActivity)) {
            throw new IllegalArgumentException("Activity must be a Sherlock activity.");
        }

        if (!(activity instanceof ActionBarDrawerToggle.DelegateProvider)) {
            throw new IllegalArgumentException("Activity must implement ActionBarDrawerToggle.DelegateProvider.");
        }

        mActivity = activity;
        mActionBar = ((SherlockFragmentActivity) activity).getSupportActionBar();
    }

    @Override
    public void setActionBarUpIndicator(Drawable drawable, int contentDescRes) {

    }

    @Override
    public void setActionBarDescription(int contentDescRes) {

    }

    @Override
    public Drawable getThemeUpIndicator() {
        return mActivity.getResources().getDrawable(getThemeUpIndicatorResId());
    }

    public int getThemeUpIndicatorResId() {
        final TypedArray a = mActivity.obtainStyledAttributes(THEME_ATTRS);
        final int result = a.getResourceId(0, 0);
        a.recycle();
        return result;
    }
}