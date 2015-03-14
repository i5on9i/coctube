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

package com.cocube.otherplaylist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cocube.R;
import com.cocube.parser.LoadListAndParseAsyncTask;
import com.cocube.parser.ParserInfo;
import com.cocube.parser.latest.CocAttacksParserInfo;
import com.cocube.parser.latest.LolTvHighRankSoloRankChannelParserInfo;
import com.cocube.parser.latest.LolTvHighlightChannelParserInfo;
import com.cocube.parser.latest.LolTvMostViewedParserInfo;
import com.cocube.provider.LolTvContract;

public class VideoPlayListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VideoPlayListFragment";
    private static final String ARG_POSITION = "position";

    private int mPosition;
    private VideoPlayListItemAdapter mVideoPlayListItemAdapter;
    private LoadListAndParseAsyncTask mAsyncTask;
    private ParserInfo mParserInfo;
    private int mOrderBy;
    private int LOADER_ID = 11;


    // Just leave this in case.
    public static VideoPlayListFragment newInstance(int position) {
        VideoPlayListFragment f = new VideoPlayListFragment();
        // passing the arguments
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		mPosition = getArguments().getInt(ARG_POSITION);


        setRetainInstance(true);
        mVideoPlayListItemAdapter = new VideoPlayListItemAdapter();
//        mOrderBy = LolTvPreference.getOrderBy(getActivity());
//        mParserInfo = getParserInfo(mPosition, mOrderBy);
//
//        // TODO : do the below only when the initLoader is not loaded
//        mAsyncTask = new LoadListAndParseAsyncTask(getActivity(), mVideoPlayListItemAdapter, mParserInfo);
//        mAsyncTask.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.video_list_fragment, container, false);
        mVideoPlayListItemAdapter.setInflater(inflater);

        ListView lv = (ListView) fragment.findViewById(R.id.listview_of_videolist);
        lv.setAdapter(mVideoPlayListItemAdapter);
        //lv.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());


        return fragment;
    }

    private ParserInfo getParserInfo(int position, int orderBy) {


        ParserInfo pinfo;

        switch (position) {
            case 0:
                pinfo = new CocAttacksParserInfo();
                mOrderBy = ParserInfo.SORT_TYPE_NONE;
                break;
            case 1:
                pinfo = new LolTvMostViewedParserInfo(1);
                mOrderBy = ParserInfo.SORT_TYPE_NONE;
                break;
            case 2:
                pinfo = new LolTvHighlightChannelParserInfo(1, orderBy);
                break;
            case 3:
                pinfo = new LolTvHighRankSoloRankChannelParserInfo(1, orderBy);
                break;
            default:
                pinfo = new CocAttacksParserInfo();
                break;
        }
        return pinfo;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onStart() {
        // TODO : initLoader() is going to be used
        initLoader();     // should do before super.onStart()
        super.onStart();


    }


    @Override
    public void onResume() {
        // restore the first items
        super.onResume();
    }

    @Override
    public void onPause() {

//        saveLetterData();

        super.onPause();

    }

    public int getOrderBy() {
        return mOrderBy;
    }


    private void initLoader() {


        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        final Loader<Cursor> loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null || loader.isReset()) {
            loaderManager.initLoader(LOADER_ID, null, this);
        }


        // @note : showLastConfiguration() is run in the onLoadFinished()
    }


    ////////////////////////////////////////////
    // LoaderManager.LoaderCallbacks<Cursor>
    //
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.d(TAG, "onCreateLoader()");
        return new CursorLoader(getActivity(), LolTvContract.PlayLists.CONTENT_URI,
                LolTvContract.PlayLists.PROJECTION_ALL, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished()");
        showLastConfiguration(cursor);
        loadPlayListItems(mVideoPlayListItemAdapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d(TAG, "onLoaderReset()");

    }

    private void showLastConfiguration(Cursor cursor) {
        // comment this for test
        //mVideoPlayListItemAdapter.setData(cursor);
    }

    private void loadPlayListItems(VideoPlayListItemAdapter adapter) {


        LoadPlayListAsyncTask load = new LoadPlayListAsyncTask(adapter);
        load.execute();
    }

}