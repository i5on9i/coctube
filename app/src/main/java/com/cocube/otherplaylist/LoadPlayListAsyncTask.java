package com.cocube.otherplaylist;

import android.os.AsyncTask;
import android.util.Log;

import com.cocube.parser.ParserInfo;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import utils.NetworkUtils;

/**
 * Created by namh on 13. 12. 6.
 */
public class LoadPlayListAsyncTask extends AsyncTask<Void, Void, List> {

    private static final String TAG = "LoadPlayListAsyncTask";
    private final ParserInfo mParserInfo;

    private int mMaxResult = 0;


    private final VideoPlayListItemAdapter mAdapter;




    public LoadPlayListAsyncTask(VideoPlayListItemAdapter adapter) {
        mParserInfo = new LolTvOthersPlayListParserInfo();
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


        mAdapter.replaceData(entries);
//        if (mActivity != null) {
//            mActivity.setProgressBarIndeterminateVisibility(Boolean.FALSE);
//
//        }


    }



    private List loadFeed() throws IOException, JSONException {


        List entries;

        InputStream is = NetworkUtils.getInputStream("GET", mParserInfo.getFeedUrl());
        OthersPlayListParser parser = new OthersPlayListParser(mParserInfo);


        entries = parser.parse(is, OthersPlayListParser.LANGUAGE_EN);
        // TODO : use the below to select language
//
//        if (Locale.getDefault().getDisplayLanguage().equals(Locale.KOREAN)) {
//            // use korean
//            entries = parser.parse(is, OthersPlayListParser.LANGUAGE_KR);
//        } else {
//            // use english
//            entries = parser.parse(is, OthersPlayListParser.LANGUAGE_EN);
//        }


        return entries;
    }

}
