package com.cocube.parser.latest;

import com.cocube.parser.ParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class LolTvListOfPlayListParserInfo implements ParserInfo {

    //
    // Unfortunately, mobile web page costs more bytes
    //  - mobile url : http://m.imbc.com/m/PodCast/default.aspx?progCode=1000671100000100000
    private static final String FEED_HOST = "https://gdata.youtube.com/feeds/api/users/loltv1004/playlists?v=2&alt=json";
    private static final String FEED_URL = FEED_HOST;

    private static final String FEED_ENCODING = "UTF-8";



    @Override
    public String getFeedHost() {
        return FEED_HOST;
    }

    @Override
    public String getFeedUrl() {
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

    }

    @Override
    public void setNextPageToken(String token) {

    }



}
