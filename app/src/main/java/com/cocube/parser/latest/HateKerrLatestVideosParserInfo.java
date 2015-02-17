package com.cocube.parser.latest;

import com.cocube.parser.ParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class HateKerrLatestVideosParserInfo implements ParserInfo {

    private int mStartIndex = 1;
    private int mMaxResult = 10;

//    private static final String CHANNEL_ID = "UC4Vxtl8TqjlsPcHzlO7UoIg";    // https://www.youtube.com/channel/UC4Vxtl8TqjlsPcHzlO7UoIg
    private static final String CHANNEL_ID = "UCZAGk8POTWog0LBKMDRW99A";    // https://www.youtube.com/channel/UC4Vxtl8TqjlsPcHzlO7UoIg

    private static final int MAX_RESULT = 10;



    private static final String LIST_LATEST_ACTIVITIES = "https://www.googleapis.com/youtube/v3/activities?part=snippet&channelId=UC4Vxtl8TqjlsPcHzlO7UoIg&publishedAfter=2015-02-08T17%3A58%3A10.000Z&fields=items%2CnextPageToken%2CpageInfo%2CprevPageToken%2CtokenPagination&key=AIzaSyC4HsfTWNcT8qlTxK3DIh50cV4e5I9YT10";
    private static final String SEARCH_LATEST = "https://www.googleapis.com/youtube/v3/search?channelId=UC4Vxtl8TqjlsPcHzlO7UoIg&key=YOUR_KEY&part=snippet&order=date";




    private static String FEED_URL = FEED_HOST;

    private static final String FEED_ENCODING = "UTF-8";

    private String nextPageToken = "";


    public HateKerrLatestVideosParserInfo(int startIndex) {
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
