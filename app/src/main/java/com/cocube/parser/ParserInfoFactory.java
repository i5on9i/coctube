package com.cocube.parser;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class ParserInfoFactory extends TagParserInfo{


    public ParserInfoFactory(int startIndex, String tag, int sortType) {
        super("loltv1004", tag, startIndex, sortType);
    }

    public static ParserInfo createParserInfo(){
//        ParserInfo pinfo;
//        int orderBy = ParserInfo.SORT_TYPE_LATEST_FIRST;
//        switch (position){
//            case 0:
//                pinfo = new LolTvLatestVideosParserInfo(1);
//                break;
//            case 1:
//                pinfo = new LolTvMostViewedParserInfo(1);
//                break;
//            case 2:
//                pinfo = new LolTvHighlightTagParserInfo(1, ParserInfo.SORT_TYPE_LATEST_FIRST);
//                break;
//            case 3:
//                pinfo = new LolTvHighRankSoloRankTagParserInfo(1, ParserInfo.SORT_TYPE_LATEST_FIRST);
//                break;
//            default:
//                pinfo = new LolTvLatestVideosParserInfo(1);
//                break;
//        }
//        return pinfo;
        return null;
    }
}
