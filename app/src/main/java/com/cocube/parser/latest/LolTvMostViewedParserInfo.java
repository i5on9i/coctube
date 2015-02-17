package com.cocube.parser.latest;

import com.cocube.parser.ParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class LolTvMostViewedParserInfo implements ParserInfo {

    private int mStartIndex = 1;
    private int mMaxResult = 10;

    private static final String FEED_HOST = "http://gdata.youtube.com/feeds/api/users/loltv1004/uploads";
    private static String FEED_URL = FEED_HOST;
    private static final String FEED_ENCODING = "UTF-8";

    private String nextPageToken = "";

    public LolTvMostViewedParserInfo(int startIndex) {
        this.mStartIndex = startIndex;
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
        builder.append("&orderby=viewCount");

        builder.append("&pageToken=");
        builder.append(nextPageToken);

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
    public int getSortType() {
        return ParserInfo.SORT_TYPE_NONE;
    }

    @Override
    public void setNextPageToken(String token) {
        this.nextPageToken = token;
    }
}
