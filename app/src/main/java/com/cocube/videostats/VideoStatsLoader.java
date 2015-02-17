package com.cocube.videostats;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.cocube.parser.ParserInfo;
import com.cocube.parser.YouTubePlayListParser;
import com.cocube.parser.YouTubeVideoItem;
import com.cocube.provider.LolTvContract;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by namh on 2015-02-15.
 */
public class VideoStatsLoader {

    private static String VIDEO_STATS_API_URL = ParserInfo.FEED_HOST + ParserInfo.FEED_TYPE_VIDEOS +
            "/?part=contentDetails,statistics" + "&key=" + ParserInfo.API_KEY2;
    private static String VIDEO_STATS_API_URL_WITH_ID = VIDEO_STATS_API_URL + "&id=";


    private volatile static VideoStatsLoader instance;


    ExecutorService mExecutorService;
    Handler mHandler = new Handler();//mHandler to display images in UI thread
    private Map<String, YouTubeVideoItem> mStatsSet = new LinkedHashMap<String, YouTubeVideoItem>();


    private Map<TextView, String> mTextViews
            = Collections.synchronizedMap(new WeakHashMap<TextView, String>());


    /**
     * Returns singleton class instance
     */
    public static VideoStatsLoader getInstance() {
        if (instance == null) {
            synchronized (VideoStatsLoader.class) {
                if (instance == null) {
                    instance = new VideoStatsLoader();
                }
            }
        }
        return instance;
    }

    protected VideoStatsLoader() {
    }


    public synchronized void init(Context context) {

        mExecutorService = Executors.newFixedThreadPool(5);
    }

    public void setStatistics() {

    }

    public void displayDurationViewCount(TextView durTextView, TextView countTextView, String youtubeId) {


        if (youtubeId.equals("")) {
            durTextView.setText("No Duration.");
            countTextView.setText("None");
        } else {

            mTextViews.put(durTextView, youtubeId); // for recycling


            YouTubeVideoItem videoItem = mStatsSet.get(youtubeId);
            if (videoItem != null) {
                durTextView.setText(videoItem.getPlainDuration());
                countTextView.setText(videoItem.getViewCount());
            } else {
                queueStats(durTextView, countTextView, youtubeId);
                durTextView.setText("");
                countTextView.setText("");
            }
        }

    }

    private void queueStats(TextView durTextView, TextView countTextView, String youtubeId) {
        String url = VIDEO_STATS_API_URL_WITH_ID + youtubeId;

        StatsToLoad p = new StatsToLoad(durTextView, countTextView, youtubeId);
        mExecutorService.submit(new StatsLoader(p));    // thread.execute()
    }

    public void setData(Cursor data) {
        mStatsSet = generateListWithEntries(data);
    }

    private Map<String, YouTubeVideoItem> generateListWithEntries(Cursor cursor) {

        // like items is saved with only unique items.
        Map<String, YouTubeVideoItem> likes = new LinkedHashMap<String, YouTubeVideoItem>();

        if (cursor != null) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex(LolTvContract.LikeItems.TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(LolTvContract.LikeItems.DESC));
                String youtubeId = cursor.getString(cursor.getColumnIndex(LolTvContract.LikeItems.YOUTUBE_ID));
                String updated = cursor.getString(cursor.getColumnIndex(LolTvContract.LikeItems.UPDATED));
                String duration = cursor.getString(cursor.getColumnIndex(LolTvContract.LikeItems.DURATION));
                String viewCount = cursor.getString(cursor.getColumnIndex(LolTvContract.LikeItems.VIEW_COUNT));


                likes.put(youtubeId,
                        new YouTubeVideoItem(title, desc, youtubeId, updated, duration, viewCount));
                cursor.moveToNext();
            }

        }

        return Collections.synchronizedMap(likes);
    }


    /**
     * @param statsToLoad
     * @return
     * @see {@link com.cocube.imageloader.ImageLoader#imageViewRecycled}
     */
    boolean viewRecycled(StatsToLoad statsToLoad) {
        String tag = mTextViews.get(statsToLoad.durTextView);

        if (tag == null || !tag.equals(statsToLoad.key)) {
            return true;
        }
        return false;
    }


    ////////////////////////////////////////////////////
    ////
    //// inner class StatsToLoad
    ////

    //Task for the queue
    private class StatsToLoad {
        public String url;
        public TextView durTextView;
        public TextView countTextView;
        public String key;

        public StatsToLoad(TextView d, TextView c, String videoId) {
            url = VIDEO_STATS_API_URL_WITH_ID + videoId;
            durTextView = d;
            countTextView = c;
            this.key = videoId;
        }
    }


    ////////////////////////////////////////////////////
    ////
    //// inner class PhotosLoader
    ////
    private class StatsLoader implements Runnable {
        StatsToLoad statsToLoad;

        StatsLoader(StatsToLoad statsToLoad) {
            this.statsToLoad = statsToLoad;
        }

        @Override
        public void run() {
            try {

                if (viewRecycled(statsToLoad))
                    return;

                YouTubeVideoItem videoItem = null;
                try {
                    String url = statsToLoad.url;
                    String videoId = statsToLoad.key;


                    //namh
                    //from SD cache
//                    thumbnail = mDiskCache.getBitmap(imageKey);

                    if (videoItem == null) {

                        videoItem = getStatsFromTheWeb(url);
                    }

                    // the reason not to check the thumnail is null that
                    // to determine faster because mMemoryCache is used
                    // before execute this thread.
                    mStatsSet.put(videoId, videoItem);


                } catch (Exception e) {
                    Log.e("DownloadImageTask", e.getMessage());
                    e.printStackTrace();

                } catch (OutOfMemoryError oom) {
                    Log.e("PhotosLoader", "Out of memory maybe in getBitmap()");
                    mStatsSet.clear();
                } catch (Throwable ex) {
                    Log.e("PhotosLoader", "run()");
                    ex.printStackTrace();
                }


                if (viewRecycled(statsToLoad))
                    return;

                StatsDisplayer bd = new StatsDisplayer(videoItem, statsToLoad);
                mHandler.post(bd);


            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        private YouTubeVideoItem getStatsFromTheWeb(String url) {
            if (url.equals("")) {
                // When dealing with the rss item which does NOT have image url.
                return null;
            }

            YouTubeVideoItem videoItem = null;
            // Missing Cache, Get the image From web


            URL imageUrl;
            try {

                imageUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);

                InputStream is = null;
                try {

                    is = conn.getInputStream();
                    videoItem = getVideoItem(is);

                } catch (FileNotFoundException e) {
                    // FileNotFoundException
                    //
                    // When the conn.getInputStream() ecounters the 404 error,
                    // FileNotFoundException occurs.
                    // some rss items have unperfect URL information of thumb-nail
                    Log.e("getStatsFromTheWeb", "FileNotFoundException, Http response code 404");


                } catch (JSONException e) {
                    Log.e("getStatsFromTheWeb", "JSONException, Error on doing parse json");
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

            } catch (MalformedURLException e1) {
                Log.e("new URL(url)", "MalformedURLException");
                e1.printStackTrace();
            } catch (IOException e) {
                Log.e("convertToByteArrayOutputStream", "io exception");
                e.printStackTrace();
            }

            return videoItem;
        }

        private YouTubeVideoItem getVideoItem(InputStream is) throws IOException, JSONException {
            return YouTubePlayListParser.parseStats(is);

        }
    }


    ////////////////////////////////////////////////////
    ////
    //// inner class BitmapDisplayer
    ////

    //Used to display bitmap in the UI thread
    class StatsDisplayer implements Runnable {
        YouTubeVideoItem mVideoItem;
        StatsToLoad mPhotoToLoad;

        public StatsDisplayer(YouTubeVideoItem v, StatsToLoad p) {
            mVideoItem = v;
            mPhotoToLoad = p;
        }

        public void run() {
            if (viewRecycled(mPhotoToLoad))
                return;

            if (mVideoItem != null) {
                mPhotoToLoad.durTextView.setText(mVideoItem.getPlainDuration());
                mPhotoToLoad.countTextView.setText(mVideoItem.getViewCount());
            } else {
                // {@link #ImageLoader} shows stub_id, before the below code,
                // on {@link #displayImage()} which is invoked from
                // {@link #ChartAdapter.getView()}
                //
                // Thus, if the empty image and no_image has the
                // same image, I think the below code is redundant.

                // mPhotoToLoad.imageView.setImageResource(stub_id);
            }

        }

    }
}
