package com.cocube.parser;

import android.os.AsyncTask;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cocube.LolTvItemAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import utils.NetworkUtils;


public class LoadListAndParseAsyncTask extends AsyncTask<Void, Void, List> {


    private static final String TAG = "LoadListAndParseAsyncTask";
    private ParserInfo mParserInfo;
    private int mMaxResult = 0;


    private SherlockFragmentActivity mActivity;
    private final LolTvItemAdapter mAdapter;


    private static final int MAX_ITEM = 7 * 2;    // 2 item a day
    private CountDownLatch mLatch;


    public LoadListAndParseAsyncTask(SherlockFragmentActivity activity, LolTvItemAdapter adapter,
                                     ParserInfo parserInfo) {
        mActivity = activity;
        mAdapter = adapter;
        mParserInfo = parserInfo;

    }

//    public LoadListAndParseAsyncTask(SherlockFragmentActivity activity, LolTvItemAdapter adapter) {
//        mActivity = activity;
//        mAdapter = adapter;
//    }

    public LoadListAndParseAsyncTask(LolTvItemAdapter adapter, CountDownLatch latch) {
        mAdapter = adapter;
        mLatch = latch;
    }

    public void setParserInfo(ParserInfo parserInfo) {
        this.mParserInfo = parserInfo;
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

        mAdapter.replaceData(entries);
        if (mActivity != null) {
            mActivity.setProgressBarIndeterminateVisibility(Boolean.FALSE);

        }


        // this is only for junit test, please refer to {@ref LoadAndParseAnsyncTaskTest}
        if (mLatch != null)
            mLatch.countDown();


    }


    //--------------------------------------------------------
    @Override
    protected void onPreExecute() {
        /**
         * to show UI first, before the data is loaded
         */
        mActivity.setProgressBarIndeterminateVisibility(Boolean.TRUE);    // progress bar on ActionBar

    }

    @Override
    protected void onProgressUpdate(Void... progress) {

    }


    private List loadFeed() throws IOException, JSONException {


        List entries;

        InputStream is = NetworkUtils.getInputStream("GET", mParserInfo.getFeedUrl());

        YouTubePlayListParser parser = new YouTubePlayListParser(mParserInfo);
        entries = parser.parse(is);


        return entries;
    }


}

	
	


