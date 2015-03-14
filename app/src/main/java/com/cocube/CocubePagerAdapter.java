package com.cocube;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cocube.parser.ParserInfo;

import java.util.ArrayList;

/**
 * Created by namh on 13. 12. 4.
 */
public class CocubePagerAdapter extends FragmentPagerAdapter {


    private static final String LOG_TAG = "CocubePagerAdapter";
    private final ArrayList<String> PAGE_TITLES = new ArrayList<String>();
    private final Context mContext;
    //{"Latest", "Most Viewed", "KR SoloQ Highlight", "KR SoloQ Full Movie"};


    public CocubePagerAdapter(Context context, FragmentManager fm) {

        super(fm);

        mContext = context;
        Resources resources = mContext.getResources();


        PAGE_TITLES.add(resources.getString(R.string.page_name_1));
        PAGE_TITLES.add(resources.getString(R.string.page_name_2));
        PAGE_TITLES.add(resources.getString(R.string.page_name_3));
        PAGE_TITLES.add(resources.getString(R.string.page_name_4));
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
                return VideoListFragment.newInstance(position);


            default:
                return VideoListFragment.newInstance(position);

        }

    }

    @Override
    public int getItemPosition(Object item) {
        VideoListFragment fragment = (VideoListFragment) item;
        int orderBy = fragment.getOrderBy();
        if (orderBy == ParserInfo.SORT_TYPE_NONE
                || orderBy == LolTvPreference.getOrderBy(mContext)) {
            return POSITION_UNCHANGED;
        } else {
            // after this, onCreateView() of Fragment is called.
            return POSITION_NONE;   // notifyDataSetChanged
        }


    }


//    /**
//     * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
//     * same object as the {@link View} added to the {@link ViewPager}.
//     */
//    @Override
//    public boolean isViewFromObject(View view, Object o) {
//        return o == view;
//    }

//
//    /**
//     * Instantiate the {@link View} which should be displayed at {@code position}. Here we
//     * inflate a layout from the apps resources and then change the text view to signify the position.
//     */
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        // Inflate a new layout from our resources
//        View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
//                container, false);
//        // Add the newly created View to the ViewPager
//        container.addView(view);
//
//        // Retrieve a TextView from the inflated View, and update it's text
//        TextView title = (TextView) view.findViewById(R.id.item_title);
//        title.setText(String.valueOf(position + 1));
//
//        Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");
//
//        // Return the View
//        return view;
//    }
//
//    /**
//     * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
//     * {@link View}.
//     */
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//        Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
//    }
}
