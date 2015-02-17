package com.cocube.parser.latest;

import com.cocube.parser.ParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class GodSonLatestVideosParserInfo implements ParserInfo {

    private int mStartIndex = 1;
    private int mMaxResult = 10;

    private static final String CHANNEL_ID = "UCsr2PfJA9b-vM9qeDS4a_Qw";    // https://www.youtube.com/channel/UC4Vxtl8TqjlsPcHzlO7UoIg

    private static final int MAX_RESULT = 10;





    private static String FEED_URL = FEED_HOST;

    private static final String FEED_ENCODING = "UTF-8";

    private String nextPageToken = "";


    public GodSonLatestVideosParserInfo(int startIndex) {
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
        builder.append(FEED_TYPE_SEARCH);
        builder.append("/?part=snippet&order=date");
        builder.append("&channelId=");
        builder.append(CHANNEL_ID);
        builder.append("&key=");
        builder.append(API_KEY2);
        builder.append("&maxResults=");
        builder.append(MAX_RESULT);
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
