package com.cocube.like;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.cocube.R;
import com.cocube.parser.YouTubeVideoItem;
import com.cocube.provider.LolTvContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LikeSingleton {


    private volatile static LikeSingleton instance;
    private Map<String, YouTubeVideoItem> mLikeSet = new LinkedHashMap<String, YouTubeVideoItem>();


    private Bitmap mNormalImage;
    private Bitmap mLikeImage;
    private Object lockSet = new Object();


    /**
     * Returns singleton class instance
     */
    public static LikeSingleton getInstance() {
        if (instance == null) {
            synchronized (LikeSingleton.class) {
                if (instance == null) {
                    instance = new LikeSingleton();
                }
            }
        }
        return instance;
    }

    protected LikeSingleton() {
    }

    public synchronized void init(Context context) {
        mNormalImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_normal);
        mLikeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_like);

    }

    public void displayLike(ImageView likeView, String youTubeId) {

        // TODO : like function
        if (youTubeId.equals("")) {
            likeView.setImageBitmap(mNormalImage);
            return;
        }

        if (mLikeSet.containsKey(youTubeId)) {
            likeView.setImageBitmap(mLikeImage);
        } else {
            likeView.setImageBitmap(mNormalImage);
        }


    }

    public void toggleLikeItem(ImageView view, YouTubeVideoItem item) {

        String youTubeId = item.getYoutubeId();

        if (mLikeSet.containsKey(youTubeId)) {
            view.setImageBitmap(mNormalImage);
            synchronized (lockSet){
                mLikeSet.remove(youTubeId);
            }
        } else {
            view.setImageBitmap(mLikeImage);
            synchronized (lockSet){ // it's because other thread can be used for the initialization of this Set
                mLikeSet.put(youTubeId, item);
            }
        }


    }

    public boolean existLike(){
        return (mLikeSet.size() > 0);
    }

    public int count() {
        return mLikeSet.size();

    }

    public List getValues() {

        return new ArrayList<YouTubeVideoItem>(mLikeSet.values());

    }

    public void setData(Cursor data) {
        mLikeSet = generateListWithEntries(data);
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
}
