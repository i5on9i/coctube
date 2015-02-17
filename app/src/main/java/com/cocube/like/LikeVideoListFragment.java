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

package com.cocube.like;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.cocube.R;
import com.cocube.parser.*;
import com.cocube.parser.latest.HateKerrLatestVideosParserInfo;
import com.cocube.parser.latest.LolTvHighRankSoloRankTagParserInfo;
import com.cocube.parser.latest.LolTvHighlightTagParserInfo;
import com.cocube.parser.latest.LolTvMostViewedParserInfo;
import com.cocube.provider.LolTvContract;

public class LikeVideoListFragment extends SherlockFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "LikeVideoListFragment";
    private static final String ARG_POSITION = "position";

    private int mPosition;
    private LikeItemAdapter mLikeItemAdapter;
    private LoadListAndParseAsyncTask mAsyncTask;
    private ParserInfo mParserInfo;
    private int mOrderBy;


    // Just leave this in case.
    public static LikeVideoListFragment newInstance(int position) {
        LikeVideoListFragment f = new LikeVideoListFragment();
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
        mLikeItemAdapter = new LikeItemAdapter();


//        mOrderBy = LolTvPreference.getOrderBy(getSherlockActivity());
//        mParserInfo = getParserInfo(mPosition, mOrderBy);
//
//        // TODO : do the below only when the initLoader is not loaded
//        mAsyncTask = new LoadListAndParseAsyncTask(getSherlockActivity(), mLikeItemAdapter, mParserInfo);
//        mAsyncTask.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View fragment = inflater.inflate(R.layout.list_fragment, container, false);
        mLikeItemAdapter.setInflater(inflater);

        ListView lv = (ListView) fragment.findViewById(R.id.listview_of_reports);
        lv.setAdapter(mLikeItemAdapter);
        lv.setOnItemClickListener((AdapterView.OnItemClickListener) getSherlockActivity());

        mLikeItemAdapter.replaceData(LikeSingleton.getInstance().getValues());


        return fragment;
    }

    private ParserInfo getParserInfo(int position, int orderBy) {


        ParserInfo pinfo;

        switch (position) {
            case 0:
                pinfo = new HateKerrLatestVideosParserInfo(1);
                mOrderBy = ParserInfo.SORT_TYPE_NONE;
                break;
            case 1:
                pinfo = new LolTvMostViewedParserInfo(1);
                mOrderBy = ParserInfo.SORT_TYPE_NONE;
                break;
            case 2:
                pinfo = new LolTvHighlightTagParserInfo(1, orderBy);
                break;
            case 3:
                pinfo = new LolTvHighRankSoloRankTagParserInfo(1, orderBy);
                break;
            default:
                pinfo = new HateKerrLatestVideosParserInfo(1);
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
        super.onStart();

//        initLoader();

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


    ////////////////////////////////////////////
    // LoaderManager.LoaderCallbacks<Cursor>
    //
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.d(TAG, "onCreateLoader()");
        return new CursorLoader(getSherlockActivity(), LolTvContract.LikeItems.CONTENT_URI,
                LolTvContract.LikeItems.PROJECTION_ALL, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished()");
        showLastNotes(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d(TAG, "onLoaderReset()");

    }

    private void showLastNotes(Cursor cursor) {
        // comment this for test
        //mLikeItemAdapter.setData(cursor);
    }


}