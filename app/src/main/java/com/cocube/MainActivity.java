package com.cocube;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cocube.drawer.SimpleDrawer;
import com.cocube.imageloader.ImageLoader;
import com.cocube.like.LikeLoaderCallback;
import com.cocube.like.LikeSingleton;
import com.cocube.parser.ParserInfo;
import com.cocube.parser.YouTubeVideoItem;
import com.cocube.provider.LolTvContract;
import com.cocube.videostats.VideoStatsLoader;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener {

    private final Handler handler = new Handler();
    private CocubePagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF2d3586;
    private String mCurrentYoutubeId;
    private int mYoutubeState;


    // Drawer Menu
    private SimpleDrawer mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mDrawerLayout = (SimpleDrawer) findViewById(R.id.drawer_layout);
        mDrawerToggle = mDrawerLayout.getDrawerToggle();

        if (savedInstanceState == null) {
            mDrawerLayout.displayView(0);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        determineYouTubeState();
        ImageLoader.getInstance().init(this);
        LikeSingleton.getInstance().init(this);
        VideoStatsLoader.getInstance().init(this);

//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//
//        }
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbr_main);
//        setSupportActionBar(toolbar);
//        // toolbar.setTitle() requires API-21
//
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
//        ListView drawerListView = (ListView)mDrawerLayout.findViewById(R.id.lv_left_drawer);
//        drawerListView.setAdapter();
//        mDrawerLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        mDrawerToggle = new ActionBarDrawerToggle(this,
//                mDrawerLayout,
//                toolbar,
//                R.string.app_name,
//                R.string.app_name);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//
//        determineYouTubeState();
//        ImageLoader.getInstance().init(this);
//        LikeSingleton.getInstance().init(this);
//        VideoStatsLoader.getInstance().init(this);
    }


    private void determineYouTubeState() {
        if (YouTubeIntents.isYouTubeInstalled(this)) {

            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this) == YouTubeInitializationResult.SUCCESS) {
                // start the YouTube player
//                youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
                mYoutubeState = YouTubeState.YOUTUBE_STATE_SERVICE_AVAILABLE;
            } else if (YouTubeIntents.canResolvePlayVideoIntent(this)) {
                mYoutubeState = YouTubeState.YOUTUBE_STATE_CANRESOLVE;
            }
        }
    }


    @Override
    protected void onStart() {
        initLoader();
        super.onStart();

    }

    @Override
    protected void onResume() {

        super.onResume();


    }


    @Override
    protected void onPause() {

        saveLikeItems();
        super.onPause();


    }


    private void saveLikeItems() {

        if (LikeSingleton.getInstance().existLike()) {

            ArrayList<ContentProviderOperation> ops = getContentProviderOperations();

            try {
                getContentResolver().applyBatch(LolTvContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

        }
    }


    private static ArrayList<ContentProviderOperation> getContentProviderOperations() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // delete all
        ops.add(ContentProviderOperation.newDelete(LolTvContract.LikeItems.CONTENT_URI)
                .withYieldAllowed(true).build());

        // Add new
        List<YouTubeVideoItem> likeItems = LikeSingleton.getInstance().getValues();
        for (YouTubeVideoItem item : likeItems) {
            ops.add(ContentProviderOperation.newInsert(LolTvContract.LikeItems.CONTENT_URI)
                    .withValues(item.getContentValues())
                    .withYieldAllowed(true).build());
        }


        return ops;
    }


    private void initLoader() {

        LoaderManager loaderManager = getSupportLoaderManager();
        final Loader<Cursor> loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null || loader.isReset()) {
            loaderManager.initLoader(LOADER_ID, null, new LikeLoaderCallback(this));
        }


        // @note : showLastNotes() is run in the onLoadFinished()
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // commented not to use option menu, 2015-02-17
//        if (mMenuDrawer.getCheckedItemPosition() == 0) {
//            getSupportMenuInflater().inflate(R.menu.main, menu);
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (LolTvPreference.getOrderBy(this) == ParserInfo.SORT_TYPE_LATEST_FIRST) {
            menu.findItem(R.id.action_sort_by_time)
                    .setChecked(true);
        } else if (LolTvPreference.getOrderBy(this) == ParserInfo.SORT_TYPE_HIGH_VIEW_COUNT_FIRST) {
            menu.findItem(R.id.action_sort_by_hit)
                    .setChecked(true);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        MainFragment f;
        switch (item.getItemId()) {

            // TODO : option menu item report
//            case R.id.action_report:
//
//                Intent emailIntent = IntentUtils.getIntentComposeEmail(getString(R.string.report_email_address),
//                        getString(R.string.sending_a_memo_using));
//                startActivity(emailIntent);
//                return true;
            case R.id.action_sort_by_time:
                LolTvPreference.setKeyOrderBy(this, ParserInfo.SORT_TYPE_LATEST_FIRST);

                f = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                f.notifyDataSetChanged();

                item.setChecked(true);
                supportInvalidateOptionsMenu();
                return true;
            case R.id.action_sort_by_hit:
                LolTvPreference.setKeyOrderBy(this, ParserInfo.SORT_TYPE_HIGH_VIEW_COUNT_FIRST);

                f = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                f.notifyDataSetChanged();


                item.setChecked(true);
                supportInvalidateOptionsMenu();
                return true;

//            case R.id.action_contact:
//                QuickContactFragment dialog = new QuickContactFragment();
//                dialog.show(getSupportFragmentManager(), "QuickContactFragment");
//                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");

    }


    ////////////////////////////////////////////////////
    //
    //  AdapterView.OnItemClickListener
    //
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        YouTubeVideoItem item = (YouTubeVideoItem) parent.getItemAtPosition(position);
        String youtubeId = item.getYoutubeId();
        if (youtubeId.equals("")) {   // boundary items
            return;
        }

        mCurrentYoutubeId = youtubeId;


        switch (mYoutubeState) {
            case YouTubeState.YOUTUBE_STATE_NOT_INSTALLED:
                Toast.makeText(this,
                        "Youtube does not exist", Toast.LENGTH_LONG).show();

                break;

            case YouTubeState.YOUTUBE_STATE_SERVICE_AVAILABLE:
            case YouTubeState.YOUTUBE_STATE_CANRESOLVE:
                this.startActivity(
                        YouTubeIntents.createPlayVideoIntent(this, mCurrentYoutubeId));

                break;
        }


    }


}
