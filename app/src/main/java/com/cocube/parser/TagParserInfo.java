package com.cocube.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class TagParserInfo implements ParserInfo {

    private int mSortType = SORT_TYPE_LATEST_FIRST;
    private int mStartIndex = 1;
    private int mMaxResult = 10;

    private static String FEED_HOST;
    private static String FEED_URL = "";

    private static final String FEED_ENCODING = "UTF-8";
    private String mEcodedTag = null;

    private boolean mSortByViewCount = false;
    private String nextPageToken = "";

    public TagParserInfo(String userid, String tag, int startIndex, int sortType) {
        FEED_HOST = "http://gdata.youtube.com/feeds/api/users/" + userid + "/uploads";

        mSortType = sortType;

        this.mStartIndex = startIndex;

        mEcodedTag = tag;
        try {
            mEcodedTag = URLEncoder.encode(tag, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getFeedHost() {
        return FEED_HOST;
    }

    @Override
    public String getFeedUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(FEED_HOST);
        builder.append("?v=2&start-index=");
        builder.append(mStartIndex);
        builder.append("&max-results=");
        builder.append(mMaxResult);
        builder.append("&alt=json");

        if(mEcodedTag != null){
            builder.append("&category=");
            builder.append(mEcodedTag);    // 천상계
        }

        // default orderby is published.
        switch (mSortType){
            case SORT_TYPE_HIGH_VIEW_COUNT_FIRST:
                builder.append("&orderby=viewCount");
                break;
            default:
                break;
        }




        FEED_URL = builder.toString();
        return FEED_URL;
    }


    @Override
    public String getPageEncoding() {
        return FEED_ENCODING;
    }


    @Override
    public void setStartIndex(int startIndex) {
        this.mStartIndex = startIndex;
    }

    @Override
    public void setNextPageToken(String token) {
        this.nextPageToken = token;
    }

    @Override
    public int getSortType(){
        return mSortType;
    }


}
