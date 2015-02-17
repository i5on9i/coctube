package com.cocube.otherplaylist;

import com.cocube.parser.ParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class LolTvOthersPlayListParserInfo implements ParserInfo {


    private static final String FEED_HOST = "http://loltv2.tumblr.com/post/79022766136/category-list-ogn-lol-lcs-highlights";
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
