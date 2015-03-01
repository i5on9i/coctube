package com.cocube.parser;


/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class PlaylistItemsParserInfo implements ParserInfo {

    protected String PLAYLIST_ID;
    protected static final int MAX_RESULT = 10;


    protected static String FEED_URL = FEED_HOST;

    protected static final String FEED_ENCODING = "UTF-8";

    protected String nextPageToken = "";


    public PlaylistItemsParserInfo(String playlistId) {
        this.PLAYLIST_ID = playlistId;

    }


    @Override
    public String getFeedHost() {
        return FEED_HOST;
    }

    @Override
    public String getFeedUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(FEED_HOST);
        builder.append(FEED_TYPE_PLAYLIST);
        builder.append("/?part=snippet&order=date");
        builder.append("&playlistId=");
        builder.append(PLAYLIST_ID);
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

    @Deprecated
    @Override
    public void setStartIndex(int startIndex) {

    }

    @Override
    public void setNextPageToken(String token) {
        this.nextPageToken = token;
    }

}
