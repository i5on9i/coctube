package com.cocube.otherplaylist.ongamenet;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cocube.LolTvPreference;
import com.cocube.R;
import com.cocube.parser.ParserInfo;

import java.util.ArrayList;

/**
 * Created by namh on 13. 12. 4.
 */
public class OnGameNetPagerAdapter extends FragmentPagerAdapter {


    private final ArrayList<String> PAGE_TITLES = new ArrayList<String>();
    private final Context mContext;
    //{"Latest", "Most Viewed", "KR SoloQ Highlight", "KR SoloQ Full Movie"};


    public OnGameNetPagerAdapter(Context context, FragmentManager fm) {

        super(fm);

        mContext = context;
        Resources resources = mContext.getResources();


        PAGE_TITLES.add(resources.getString(R.string.page_name_ongamenet_1));
        PAGE_TITLES.add(resources.getString(R.string.page_name_ongamenet_2));

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_TITLES.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                return OnGameNetVideoListFragment.newInstance(position);


            default:
                return OnGameNetVideoListFragment.newInstance(position);

        }

    }

    @Override
    public int getItemPosition(Object item) {
        OnGameNetVideoListFragment fragment = (OnGameNetVideoListFragment) item;
        int orderBy = fragment.getOrderBy();
        if (orderBy == ParserInfo.SORT_TYPE_NONE
                || orderBy == LolTvPreference.getOrderBy(mContext)) {
            return POSITION_UNCHANGED;
        } else {
            // after this, onCreateView() of Fragment is called.
            return POSITION_NONE;   // notifyDataSetChanged
        }


    }


}
