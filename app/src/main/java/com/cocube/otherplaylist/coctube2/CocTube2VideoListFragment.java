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

package com.cocube.otherplaylist.coctube2;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cocube.LolTvItemAdapter;
import com.cocube.LolTvPreference;
import com.cocube.R;
import com.cocube.like.LikeSingleton;
import com.cocube.loadmore.LoadMoreScrollListener;
import com.cocube.parser.LoadListAndParseAsyncTask;
import com.cocube.parser.ParserInfo;
import com.cocube.parser.YouTubeVideoItem;
import com.cocube.parser.coctube2.CocCamParserInfo;
import com.cocube.parser.coctube2.DaddyCocParserInfo;
import com.cocube.parser.coctube2.HaVocGamingParserInfo;
import com.cocube.parser.coctube2.MasterOvParserInfo;
import com.cocube.provider.LolTvContract;

import java.util.ArrayList;
import java.util.List;

public class CocTube2VideoListFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int mPosition;
    private LolTvItemAdapter mLolTvItemAdapter;
    private LoadListAndParseAsyncTask mAsyncTask;
    private ParserInfo mParserInfo;
    private int mCurrentOrderBy = ParserInfo.SORT_TYPE_INIT;

    public static CocTube2VideoListFragment newInstance(int position) {
        CocTube2VideoListFragment f = new CocTube2VideoListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPosition = getArguments().getInt(ARG_POSITION);

        mLolTvItemAdapter = new LolTvItemAdapter();


        mParserInfo = getParserInfo(mPosition, LolTvPreference.getOrderBy(getActivity()));
//        TODO : do the below only when the initLoader is not loaded
//        mAsyncTask = new LoadListAndParseAsyncTask(getSherlockActivity(), mLolTvItemAdapter);
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.list_fragment, container, false);

        int newOrderBy = LolTvPreference.getOrderBy(getActivity());
        if (isOrderChanged(mPosition, newOrderBy)) {
            mParserInfo = getParserInfo(mPosition, newOrderBy);
            initList(mParserInfo, newOrderBy);
        }

        mLolTvItemAdapter.setInflater(inflater);


        ListView lv = (ListView) fragment.findViewById(R.id.listview_of_reports);
        lv.setAdapter(mLolTvItemAdapter);
        lv.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        LoadMoreScrollListener scrollListener = new LoadMoreScrollListener(mLolTvItemAdapter, mParserInfo);
        lv.setOnScrollListener(scrollListener);


        return fragment;
    }


    private void initList(ParserInfo parserInfo, int newOrderBy) {

        LoadListAndParseAsyncTask task =
                new LoadListAndParseAsyncTask(getActivity(), mLolTvItemAdapter, parserInfo);

        task.execute();

        mCurrentOrderBy = parserInfo.getSortType();
    }


    private boolean isOrderChanged(int pos, int newOrderBy) {
        if (mCurrentOrderBy == ParserInfo.SORT_TYPE_NONE)
            return false;
        return mCurrentOrderBy != newOrderBy;
    }

    private ParserInfo getParserInfo(int position, int orderBy) {


        ParserInfo pinfo;


        // FIXME : ParserInfos are better to be passed as an argument
        switch (position) {
            // the count of position is determined by {@link KazaPagerAdapter.getCount()}

            case 0:
                pinfo = new MasterOvParserInfo();
                break;
            case 1:
                pinfo = new DaddyCocParserInfo();
                break;
            case 2:
                pinfo = new CocCamParserInfo();
                break;
            case 3:
                pinfo = new HaVocGamingParserInfo();
                break;

            default:
                pinfo = new MasterOvParserInfo();
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


//        saveLikeItems();

        super.onPause();

    }

    private void saveLikeItems() {

        if (LikeSingleton.getInstance().existLike()) {

            ArrayList<ContentProviderOperation> ops = getContentProviderOperations();

            try {
                getActivity().getContentResolver().applyBatch(LolTvContract.AUTHORITY, ops);
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

    public int getOrderBy() {
        return mCurrentOrderBy;
    }


}