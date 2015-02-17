package com.cocube.parser;


/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class PlayListParserInfo implements ParserInfo {


    private int mStartIndex = 1;
    private int mMaxResult = 10;

    //
    // Unfortunately, mobile web page costs more bytes
    //  - mobile url : http://m.imbc.com/m/PodCast/default.aspx?progCode=1000671100000100000
    private String mId = "PLWQeRMoEALvrH6C_fx9m9Pca2dFIHjg9D";
    private static final String FEED_HOST = "https://gdata.youtube.com/feeds/api/playlists/";


    private static String FEED_URL = FEED_HOST;
    private static final String FEED_ENCODING = "UTF-8";

    private String nextPageToken = "";

    public PlayListParserInfo(String id, int startIndex) {
        this.mId = id;
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
        builder.append(mId);
        builder.append("?v=2&start-index=");
        builder.append(mStartIndex);
        builder.append("&max-results=");
        builder.append(mMaxResult);
        builder.append("&alt=json");


        FEED_URL = builder.toString();
        return FEED_URL;
    }


    @Override
    public String getPageEncoding() {
        return FEED_ENCODING;
    }

    @Override
    public int getSortType() {
        return ParserInfo.SORT_TYPE_NONE;
    }

    @Override
    public void setStartIndex(int startIndex) {
        this.mStartIndex = startIndex;
    }


    @Override
    public void setNextPageToken(String token) {
        this.nextPageToken = token;
    }
}
