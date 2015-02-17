package com.cocube.parser;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public interface ParserInfo {
    public static final int SORT_TYPE_INIT = -1;
    public static final int SORT_TYPE_NONE = 0;
    public static final int SORT_TYPE_LATEST_FIRST = 1;
    public static final int SORT_TYPE_LATEST_LAST = 2;
    public static final int SORT_TYPE_HIGH_VIEW_COUNT_FIRST = 3;
    //
    // Unfortunately, mobile web page costs more bytes
    //  - mobile url : http://m.imbc.com/m/PodCast/default.aspx?progCode=1000671100000100000
    String FEED_HOST = "https://www.googleapis.com/youtube/v3/";
    String FEED_TYPE_SEARCH = "search";
    String FEED_TYPE_VIDEOS = "videos";
    String API_KEY2 = "AIzaSyC4HsfTWNcT8qlTxK3DIh50cV4e5I9YT10";
    String API_KEY = "AIzaSyAF81XCZukw8ZEbf36dPKLIR5l3LiaZrBw";


    public String getFeedHost();
    public String getFeedUrl();
    public String getPageEncoding();
    public int getSortType();


    void setStartIndex(int startIndex);
    void setNextPageToken(String token);
}
