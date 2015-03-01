package com.cocube.parser;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class ChannelParserInfo implements ParserInfo {


    protected String CHANNEL_ID;    // https://www.youtube.com/channel/UC4Vxtl8TqjlsPcHzlO7UoIg
    protected String query;
    protected static final int MAX_RESULT = 10;


    protected static String FEED_URL = FEED_HOST;

    protected static final String FEED_ENCODING = "UTF-8";

    protected String nextPageToken = "";


    public ChannelParserInfo(String channelId) {
        this.CHANNEL_ID = channelId;
        this.query = "";
    }

    public ChannelParserInfo(String channelId, String q) {
        this.CHANNEL_ID = channelId;
        this.query = q;
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
        builder.append("&type=");
        builder.append(FEED_SEARCH_TYPE_VIDEO);
        builder.append("&key=");
        builder.append(API_KEY2);
        builder.append("&maxResults=");
        builder.append(MAX_RESULT);
        builder.append("&pageToken=");
        builder.append(nextPageToken);
        builder.append("&q=");
        builder.append(Uri.encode(query));


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

    @Deprecated
    @Override
    public void setStartIndex(int startIndex) {

    }

    @Override
    public void setNextPageToken(String token) {
        this.nextPageToken = token;
    }


}
