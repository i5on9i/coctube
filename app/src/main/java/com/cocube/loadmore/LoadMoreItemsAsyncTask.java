package com.cocube.loadmore;

import android.os.AsyncTask;
import android.util.Log;

import com.cocube.LolTvItemAdapter;
import com.cocube.parser.ParserInfo;
import com.cocube.parser.YouTubePlayListParser;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import utils.NetworkUtils;

/**
 * Created by namh on 13. 12. 6.
 */
public class LoadMoreItemsAsyncTask extends AsyncTask<Void, Void, List> {

    private static final String TAG = "LoadMoreItemsAsyncTask";
    private final ParserInfo mParserInfo;

    private int mMaxResult = 0;


    private final LolTvItemAdapter mAdapter;




    public LoadMoreItemsAsyncTask(LolTvItemAdapter adapter, ParserInfo parserInfo) {
        mParserInfo = parserInfo;
        mAdapter = adapter;


    }


    //--------------------------------------------------------
    @Override
    protected List doInBackground(Void... params) {


        try {

            return loadFeed();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //--------------------------------------------------------

    /**
     * @param entries its type must be same as the return type of
     *                doInBackground()
     */
    @Override
    protected void onPostExecute(List entries) {

        if (entries == null) {
            Log.e(TAG, "feed data is null");
            return;
        }


        mAdapter.addAll(entries);
//        if (mActivity != null) {
//            mActivity.setProgressBarIndeterminateVisibility(Boolean.FALSE);
//
//        }


    }



    private List loadFeed() throws IOException, JSONException {


        List entries;

        InputStream is = NetworkUtils.getInputStream("GET", mParserInfo.getFeedUrl());
        YouTubePlayListParser parser = new YouTubePlayListParser(mParserInfo);
        entries = parser.parse(is);



        return entries;
    }

}
