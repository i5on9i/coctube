package com.cocube.loadmore;

import android.widget.AbsListView;

import com.cocube.LolTvItemAdapter;
import com.cocube.parser.ParserInfo;

/**
 * Created by namh on 13. 12. 6.
 */

public class LoadMoreScrollListener implements AbsListView.OnScrollListener {


    private final LolTvItemAdapter mAdapter;
    private final ParserInfo mParserInfo;
    private int mOffsetBoundary = 2;
    private int visibleThreshold = 3;
    private int mCurrentPage = 0;
    private int previousTotal = 0;
    private boolean mDoneLoading = false;


    public LoadMoreScrollListener(LolTvItemAdapter adapter, ParserInfo parserInfo) {
        mParserInfo = parserInfo;
        mAdapter = adapter;
        visibleThreshold += adapter.getCountOfDummy();
    }

//    public LoadMoreScrollListener(LolTvItemAdapter adapter, int visibleThreshold) {
//        mAdapter = adapter;
//        this.visibleThreshold = visibleThreshold + adapter.getCountOfDummy();
//    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (!mDoneLoading) {
            if (totalItemCount > previousTotal) {
                mDoneLoading = true;
                previousTotal = totalItemCount;
                mCurrentPage++;
            }
        }


        if (mDoneLoading
                && almostScrolled(firstVisibleItem, visibleItemCount, totalItemCount)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.

            final int startIndex = (mCurrentPage * 10) + 1;

            mParserInfo.setStartIndex(startIndex);
            new LoadMoreItemsAsyncTask(mAdapter, mParserInfo).execute();

            // if the loading is failed, mDoneLoading is always false
            // and totalItemCount and previousTotal are same.
            // thus invokation of OnScroll() would do nothing.


            mDoneLoading = false;
        }


    }

    private boolean almostScrolled(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int total = totalItemCount - mOffsetBoundary;
        int firstVisible = firstVisibleItem - (mOffsetBoundary/2);

        return (total - (firstVisible + visibleItemCount)) <= visibleThreshold;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


}
