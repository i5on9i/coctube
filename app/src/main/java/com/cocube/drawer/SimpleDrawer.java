package com.cocube.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cocube.MainFragment;
import com.cocube.R;
import com.cocube.like.LikeVideoListFragment;
import com.cocube.otherplaylist.kaza.KazaMainFragment;
import com.cocube.otherplaylist.mr.MrMainFragment;
import com.cocube.otherplaylist.ongamenet.OnGameNetMainFragment;
import com.cocube.otherplaylist.protato.ProtatoMainFragment;
import com.cocube.sherlockadapter.SherlockActionBarDrawerToggle;

import java.util.ArrayList;

import utils.IntentUtils;


public class SimpleDrawer extends DrawerLayout implements
        ListView.OnItemClickListener {


    private NavDrawerItem mLikeMenuItem;

    private final String TRADITIONAL_ARAB_FONT = "tradobdo.ttf";
    private final int HOME_ICON_PADDING = 5;

    private MenuDrawer mMenuDrawer;
    private SherlockFragmentActivity mActivity;
    private SherlockActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;
    private int mContentFrameId = R.id.content_frame;
    private String[] mNavMenuTitles;
    private SherlockFragment mFragment;


    public SimpleDrawer(Context context) {
        super(context);
        init(context, null, 0);

    }

    public SimpleDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public SimpleDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);


    }


    private void init(Context context, AttributeSet attrs, int defStyle) {

        mActivity = (SherlockFragmentActivity) context;

        mActionBar = mActivity.getSupportActionBar();
        // enabling action bar app icon and behaving it as toggle button
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        ImageView iv = (ImageView) mActivity.findViewById(android.R.id.home);

        setHomeIconPaddingLeftRight(iv, HOME_ICON_PADDING);


        mDrawerToggle
                = createActionBarDrawerToggle(mActivity, mActionBar);
        this.setDrawerListener(mDrawerToggle);


    }

    private void setHomeIconPaddingLeftRight(ImageView iv, int padding) {
        float scale = getResources().getDisplayMetrics().density;
        int paddingDp = (int) (padding * scale + 0.5f);
        iv.setPadding(paddingDp, 0, paddingDp, 0);
    }

    private SherlockActionBarDrawerToggle createActionBarDrawerToggle(final SherlockFragmentActivity mActivity,
                                                                      final ActionBar actionBar) {


        SherlockActionBarDrawerToggle drawerToggle = new SherlockActionBarDrawerToggle(mActivity,
                this,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {


            public void onDrawerClosed(View view) {
                actionBar.setTitle(mActivity.getTitle());
                // calling onPrepareOptionsMenu() to show action bar icons
                mActivity.invalidateOptionsMenu();

            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(mActivity.getTitle());
                // calling onPrepareOptionsMenu() to hide action bar icons
                mActivity.invalidateOptionsMenu();

                mMenuDrawer.refreshLikeCount();


            }
        };

        return drawerToggle;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        /**
         *  NOTICE
         *
         *  Here, I use the convention
         *  so the child(0) must be the Frame container
         *  and the child(1) must be the drawer menu list view
         *
         */


        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // setting the nav drawer list adapter
        NavDrawerListAdapter adapter
                = new NavDrawerListAdapter(this.getContext(), getDefaultDrawerMenuList(getResources()));


        mMenuDrawer = (MenuDrawer) getChildAt(1);

        mMenuDrawer.setAdapter(adapter);
        mMenuDrawer.setOnItemClickListener(this);

        mContentFrameId = getChildAt(0).getId();

    }

    private ArrayList<NavDrawerItem> getDefaultDrawerMenuList(Resources resources) {

        ArrayList<NavDrawerItem> menuList = new ArrayList<NavDrawerItem>();
        String[] navMenuTitles = resources.getStringArray(R.array.nav_drawer_items);
        TypedArray navMenuIcons = resources.obtainTypedArray(R.array.nav_drawer_icons);
        TypedArray navMenuIconsInverse = resources.obtainTypedArray(R.array.nav_drawer_icons_inverse);


        int max = navMenuTitles.length;
        for (int i = 0; i < max; i++) {

            String title = navMenuTitles[i];
            NavDrawerItem drawerItem = new NavDrawerItem(title,
                    navMenuIcons.getResourceId(i, -1),
                    navMenuIconsInverse.getResourceId(i, -1));

            menuList.add(drawerItem);


        }

        // Recycle the typed array
        navMenuIcons.recycle();

        return menuList;

    }




    public SherlockActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public ListView getMenuDrawer() {
        return mMenuDrawer;
    }


    ////////////////////////////////////////////////////////////
    ////    OnItemClickListener
    ////
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // display view for selected nav drawer item
        processClick(position);

    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    public void processClick(int position) {

        // update the main content by replacing fragments
        switch (position) {
            case 0:
                if (!(mFragment instanceof MainFragment)) {
                    setUpSelectedFragment(position, new MainFragment());
                }
                break;
            case 1:
                if (!(mFragment instanceof OnGameNetMainFragment)) {
                    setUpSelectedFragment(position, new OnGameNetMainFragment());
                }
                break;
            case 2:
                if (!(mFragment instanceof KazaMainFragment)) {
                        setUpSelectedFragment(position, new KazaMainFragment());
                    }
                break;
            case 3:
                if (!(mFragment instanceof MrMainFragment)) {
                    setUpSelectedFragment(position, new MrMainFragment());
                }
                break;

            case 4:
                if (!(mFragment instanceof ProtatoMainFragment)) {
                    setUpSelectedFragment(position, new ProtatoMainFragment());
                }
                break;
            case 5:  // LIKE_MENU_ITEM_INDEX {@link MenuDrawer.refreshLikeCount}
                if (!(mFragment instanceof LikeVideoListFragment)) {
                    setUpSelectedFragment(position, new LikeVideoListFragment());
                }
                break;
            case 6:
                Intent emailIntent = IntentUtils.getIntentComposeEmail(
                        mActivity.getString(R.string.report_email_address),
                        mActivity.getString(R.string.sending_a_memo_using));
                mActivity.startActivity(emailIntent);

                mMenuDrawer.setSelectionWithPrevious();
                closeDrawer(mMenuDrawer);

                break;

//            case 4:
//                // show selection view
//                if (!(mFragment instanceof VideoPlayListFragment)) {
//                    setUpSelectedFragment(position, new VideoPlayListFragment());
//                }
//                break;


            default:

                break;
        }


    }

    private void setUpSelectedFragment(int position, SherlockFragment fragment) {

        if (fragment == null) {
            Log.e("SimpleDrawer", "Error in creating fragment");
            return;
        }

        mActivity.setTitle(mNavMenuTitles[position]);
        replaceFragment(fragment);
        mMenuDrawer.setAdapterSelect(position);
        closeDrawer(mMenuDrawer);
    }

    /**
     * @param newFragment : new fragment which will be injected
     */
    private void replaceFragment(SherlockFragment newFragment) {

        mFragment = newFragment;
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(mContentFrameId, newFragment).commit();

    }


    public void displayView(int position) {

        // update the main content by replacing fragments
        switch (position) {
            case 0:
                mFragment = new MainFragment();
                break;
            case 1:
                mFragment = new LikeVideoListFragment();
                break;
            default:
                break;
        }

        if (mFragment == null) {
            // error in creating fragment
            Log.e("SimpleDrawer", "Error in creating fragment");
        }

        replaceFragment(mFragment);
        mMenuDrawer.setItemCheckedAndSelect(position);
        closeDrawer(mMenuDrawer);
    }
}
