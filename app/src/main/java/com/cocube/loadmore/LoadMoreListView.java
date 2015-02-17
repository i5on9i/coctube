package com.cocube.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private OnLastItemVisibleListener onLastItemVisibleListener;
    private OnScrollListener onScrollListener;
    private int lastSavedFirstVisibleItem = -1;
    private OnAutoLoadPreviousDataListener mOnAutoLoadPreviousDataListener;
    private boolean mLoading;
    private int mLastTotalItemCount = -1;


    public LoadMoreListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    ////////////////////////////////////////////////////////////////////////////
    ////    Implement OnScrollListener
    ////
    @Override
    public final void onScroll(final AbsListView view, final int firstVisibleItem,
                               final int visibleItemCount, final int totalItemCount) {

        if (onLastItemVisibleListener != null) {
            // detect if last item is visible
            if (visibleItemCount > 0 && visibleItemCount < totalItemCount
                    && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                // only process first event
                if (firstVisibleItem != lastSavedFirstVisibleItem) {
                    lastSavedFirstVisibleItem = firstVisibleItem;
                    onLastItemVisibleListener.onLastItemVisible();
                }
            }
        }



        if(mOnAutoLoadPreviousDataListener != null){

            if(isListViewAlmostScrolled(firstVisibleItem, visibleItemCount, totalItemCount))
            {
                if(!mLoading && mLastTotalItemCount != totalItemCount)   // not to invoke several time at the same position
                {
                    mLastTotalItemCount = totalItemCount;
                    mOnAutoLoadPreviousDataListener.onAutoLoadPreviousData(totalItemCount);
                }
            }
        }




    }

    private boolean isListViewAlmostScrolled(int firstIndex, int visible, int total) {

        // When the 1 page including currently showed page remains.
        final int offset = 2;
        if(visible > 0 && visible < total
                && (firstIndex + (2 * visible) + offset > total) )
        {
            return true;
        }
        return false;
    }


    protected void setLoading(boolean val)
    {
        mLoading = val;
    }

    public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        onLastItemVisibleListener = listener;
    }

    public static interface OnLastItemVisibleListener {

        public void onLastItemVisible();

    }

    public static interface OnAutoLoadPreviousDataListener {

        public void onAutoLoadPreviousData(int pageIndex);

    }


}