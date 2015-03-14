package com.cocube.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cocube.MainFragment;
import com.cocube.R;
import com.cocube.like.LikeVideoListFragment;
import com.cocube.otherplaylist.coctube2.CocTube2MainFragment;
import com.cocube.otherplaylist.frenchcoc.FrCocChannelMainFragment;
import com.cocube.otherplaylist.germancoc.GermanCocChannelMainFragment;
import com.cocube.otherplaylist.krcoc.KrCocChannelMainFragment;
import com.cocube.otherplaylist.protato.ProtatoMainFragment;


import java.util.ArrayList;

import utils.IntentUtils;


public class SimpleDrawer extends DrawerLayout implements
        View.OnClickListener{


    private NavDrawerItem mLikeMenuItem;

    private final String TRADITIONAL_ARAB_FONT = "tradobdo.ttf";
    private final int HOME_ICON_PADDING = 5;

    private MenuDrawer mMenuDrawer;
    private ActionBarActivity mActivity;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;
    private int mContentFrameId = R.id.content_frame;
    private String[] mNavMenuTitles;
    private Fragment mFragment;
    private Toolbar mToolbar;


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

        mActivity = (ActionBarActivity) context;


    }

    private void setHomeIconPaddingLeftRight(ImageView iv, int padding) {
        float scale = getResources().getDisplayMetrics().density;
        int paddingDp = (int) (padding * scale + 0.5f);
        iv.setPadding(paddingDp, 0, paddingDp, 0);
    }

    private ActionBarDrawerToggle createActionBarDrawerToggle(final ActionBarActivity mActivity,
                                                              final ActionBar actionBar,
                                                              final Toolbar toolbar) {


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                mActivity,
                this,
                toolbar,
                R.string.app_name,  // nav drawer open - description for accessibility
                R.string.app_name
        ){


            public void onDrawerClosed(View view) {
                toolbar.setTitle(mActivity.getTitle());
                // calling onPrepareOptionsMenu() to show action bar icons
                mActivity.invalidateOptionsMenu();

            }

            public void onDrawerOpened(View drawerView) {
                toolbar.setTitle(mActivity.getTitle());
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
         */

        // Toolbar
        mToolbar = (Toolbar) this.findViewById(R.id.tlbr_main);
        mActivity.setSupportActionBar(mToolbar);
        // mToolbar.setTitle() requires API-21

        mDrawerToggle
                = createActionBarDrawerToggle(mActivity, mActionBar, mToolbar);
        this.setDrawerListener(mDrawerToggle);


        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // setting the nav drawer list adapter
        NavDrawerListAdapter adapter
                = new NavDrawerListAdapter(this.getContext(),
                                            getDefaultDrawerMenuList(getResources()),
                                            this);


        mMenuDrawer = (MenuDrawer) this.findViewById(R.id.list_slidermenu);


        mMenuDrawer.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size


        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mMenuDrawer.setAdapter(adapter);              // Setting the adapter to RecyclerView

        mMenuDrawer.setLayoutManager(new LinearLayoutManager(getContext()));   // Creating and Setting the layout Manager


        mMenuDrawer.setAdapter(adapter);
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


    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public RecyclerView getMenuDrawer() {
        return mMenuDrawer;
    }


    ////////////////////////////////////////////////////////////
    ////    OnClickListener
    ////
    @Override
    public void onClick(View view) {
        // display view for selected nav drawer item

        CharSequence text = ((TextView)view.findViewById(R.id.title)).getText();
        for(int i = 0, len = mNavMenuTitles.length ; i < len ; i++){
            if(text.equals(mNavMenuTitles[i])){
                processClick(i);
                return;
            }
        }


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
                if (!(mFragment instanceof CocTube2MainFragment)) {
                    setUpSelectedFragment(position, new CocTube2MainFragment());
                }
                break;

            case 2:
                if (!(mFragment instanceof FrCocChannelMainFragment)) {
                    setUpSelectedFragment(position, new FrCocChannelMainFragment());
                }
                break;

            case 3:
                if (!(mFragment instanceof GermanCocChannelMainFragment)) {
                    setUpSelectedFragment(position, new GermanCocChannelMainFragment());
                }
                break;

            case 4:
                if (!(mFragment instanceof KrCocChannelMainFragment)) {
                    setUpSelectedFragment(position, new KrCocChannelMainFragment());
                }
                break;


            case 5:
                if (!(mFragment instanceof ProtatoMainFragment)) {
                    setUpSelectedFragment(position, new ProtatoMainFragment());
                }
                break;

//            case 5:
//                if (!(mFragment instanceof MyListPrefFragment)) {
//                    setUpSelectedFragment(position, new MyListPrefFragment());
//                }
//                break;

            case 6:  // LIKE_MENU_ITEM_INDEX {@link MenuDrawer.refreshLikeCount}
                if (!(mFragment instanceof LikeVideoListFragment)) {
                    setUpSelectedFragment(position, new LikeVideoListFragment());
                }
                break;
            case 7:
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

    private void setUpSelectedFragment(int position, Fragment fragment) {

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
    private void replaceFragment(Fragment newFragment) {

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
