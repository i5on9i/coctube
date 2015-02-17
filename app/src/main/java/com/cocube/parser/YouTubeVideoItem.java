package com.cocube.parser;

import android.content.ContentValues;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;

import com.cocube.like.LikeSingleton;
import com.cocube.provider.LolTvContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.StringUtils;


/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 11. 13
 * Time: 오후 9:00
 * To change this template use File | Settings | File Templates.
 */
public class YouTubeVideoItem implements View.OnClickListener {

    private final String mTitle;
    private final String mDesc;

    private final String mYoutubeId;
    private final String mUpdated;
    private String mDateSpanString = "";

    private final String THUMB_URL_BASE = "https://i1.ytimg.com/vi/";
    private final String THUMB_URL_HEAD;
    private final String THUMB_URL_TAIL_MQDEFAULT = "mqdefault.jpg";
    private final String THUMB_URL_TAIL_HQDEFAULT = "hqdefault.jpg";
    private final String THUMB_URL_TAIL_SDDEFAULT = "sddefault.jpg";
    private final String THUMB_URL_TAIL_MAXDEFAULT = "maxresdefault.jpg";

    private final String mViewCount;
    private final String mDuration;
    private final String mDurationPlain;
    private boolean mExistYouTubeId = false;


    public YouTubeVideoItem(String title, String desc,
                            String youtubeId, String updated,
                            String duration, String viewCount) {
        mTitle = title;
        this.mDesc = desc;

        mYoutubeId = youtubeId;
        mUpdated = updated;


        if(updated.length() > 0){
            try {
                // 2015-02-16T15:00:06.000Z --> Date --> 1 weeks ago
                mDateSpanString = utils.DateUtils.getRelativeTimeSpanString(mUpdated);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        mDurationPlain = duration;
        mDuration = StringUtils.convertSecToDurationString(duration);
        mViewCount = StringUtils.getNumberWithComma(viewCount);
        THUMB_URL_HEAD = THUMB_URL_BASE + mYoutubeId;


        if (!mYoutubeId.equals("")) {
            mExistYouTubeId = true;

        }

    }


    public String getTitle() {
        return mTitle;
    }

    public String getYoutubeId() {
        return mYoutubeId;
    }

    public String getDescription() {
        return mDesc;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getPlainDuration() {
        return mDurationPlain;
    }


    public String getThumbMq() {
        if (mExistYouTubeId) {
            return THUMB_URL_HEAD + "/" + THUMB_URL_TAIL_MQDEFAULT;
        } else {
            return "";
        }

    }

    public String getThumbHq() {
        if (mExistYouTubeId) {
            return THUMB_URL_HEAD + "/" + THUMB_URL_TAIL_HQDEFAULT;
        } else {
            return "";
        }
    }

    public String getThumbSd() {
        if (mExistYouTubeId) {
            return THUMB_URL_HEAD + "/" + THUMB_URL_TAIL_SDDEFAULT;
        } else {
            return "";
        }
    }

    public String getThumbMax() {
        if (mExistYouTubeId) {
            return THUMB_URL_HEAD + "/" + THUMB_URL_TAIL_MAXDEFAULT;
        } else {
            return "";
        }
    }

    /**
     * For Video item, updated date is not meaningful, so the published is used.
     *
     * @return
     */
    public String getUpdated() {
        return mUpdated;
    }


    public String getDateSpanString() {
        return mDateSpanString;
    }


    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LolTvContract.LikeItems.TITLE, mTitle);
        values.put(LolTvContract.LikeItems.DESC, mDesc);
        values.put(LolTvContract.LikeItems.YOUTUBE_ID, mYoutubeId);
        values.put(LolTvContract.LikeItems.UPDATED, mUpdated);
        values.put(LolTvContract.LikeItems.DURATION, mDurationPlain);



        return values;
    }


    ////////////////////////////////////////////////////////////////////
    ////    View.OnClickListener
    ////
    @Override
    public void onClick(View view) {

        LikeSingleton.getInstance().toggleLikeItem((ImageView)view, this);

    }



}
