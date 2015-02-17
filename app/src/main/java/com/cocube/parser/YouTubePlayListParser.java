package com.cocube.parser;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 13
 * Time: 오후 10:32
 * To change this template use File | Settings | File Templates.
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;


public class YouTubePlayListParser {


    private ParserInfo mInfo;

    public YouTubePlayListParser(ParserInfo info) {
        mInfo = info;
    }


    /**
     * JSON parser
     * the source data json must be the gdata version 2
     *
     * @param stream
     * @return
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public List<YouTubeVideoItem> parse(InputStream stream) throws IOException, JSONException {

        /**
         * feed -+
         *       +--- entry -- array --+
         *                             +---- dict +
         *                             |          +- published
         *                             |          +- title
         *                             |          +- content +
         *                             |          |          +- src
         *                             |          +- media$group -- ditct -- media$description -- dict -- $t
         *                             |
         *                             +---- dict +
         *                                        +- published
         *                                        +- title
         *                                        +- content +
         *                                        |          +- src
         *                                        +- media$group -- ditct -- media$description -- dict -- $t
         *
         *
         *
         * entry
         media$group
         media$description
         *
         *
         * {
         "version": "1.0",
         "encoding": "UTF-8",
         "feed": {

         "entry": [
         {
         "title": {
         "$t": "[6\ud68c] 1\ub4f1 \uc2e0\ubd93\uac10 \uce90\ub9ac\ub294 \uc65c \uacb0\ud63c\uc744 \ubabb\ud558\ub098...'\uc778\uc13c\ud2f0\ube0c'\uc758 \ub9c8\ubc95"
         },
         "content": {
         "type": "application\/x-shockwave-flash",
         "src": "http:\/\/www.youtube.com\/v\/YWZ4S8fcsoM?version=3&f=playlists&app=youtube_gdata"
         },
         ...
         },
         {
         ...
         }
         ]
         }
         }
         *
         */
        ArrayList<YouTubeVideoItem> entries = new ArrayList<YouTubeVideoItem>();

        String jsonString = StringUtils.convertStreamToString(stream, mInfo.getPageEncoding());
        JSONObject jsonObj = new JSONObject(jsonString);


        // set next page token
        mInfo.setNextPageToken(jsonObj.getString("nextPageToken"));

        JSONArray items = jsonObj.getJSONArray("items");
        for(int i = 0, maxlen =items.length(); i< maxlen; i++){
            JSONObject item = items.getJSONObject(i);

            JSONObject id = item.getJSONObject("id");
            String videoId = id.getString("videoId");

            JSONObject snippet = item.getJSONObject("snippet");

            String title = snippet.getString("title");
            String publishedAt = snippet.getString("publishedAt");
            String desc = snippet.getString("description");

            // the below are needed
            // duration
            // view count
            entries.add(new YouTubeVideoItem(title, desc,
                    videoId, publishedAt, "10", "10"));


        }



        return entries;
    }


    public static YouTubeVideoItem parseStats(InputStream stream) throws IOException, JSONException {

        /**
         *
             {
                "kind": "youtube#videoListResponse",
                "etag": "\"k1sYjErg4tK7WaQQxvJkW5fVrfg/xCIntIEc4Q-wUQ7GmXjs1czWhUA\"",
                "pageInfo": {
                    "totalResults": 2,
                    "resultsPerPage": 2
                },
                "items": [
                    {
                        "kind": "youtube#video",
                        "etag": "\"k1sYjErg4tK7WaQQxvJkW5fVrfg/jjzuDmrogwFNo8I1G7zgMxOiFw8\"",
                        "id": "o2q2xNVZPbc",
                        "contentDetails": {
                            "duration": "PT14M45S",
                            "dimension": "2d",
                            "definition": "hd",
                            "caption": "false",
                            "licensedContent": true
                        },
                        "statistics": {
                            "viewCount": "16608",
                            "likeCount": "395",
                            "dislikeCount": "48",
                            "favoriteCount": "0",
                            "commentCount": "36"
                        }
                    }
                ]
            }
         *
         *
         */


        YouTubeVideoItem ret = null;
        String jsonString = StringUtils.convertStreamToString(stream, "UTF-8");
        JSONObject jsonObj = new JSONObject(jsonString);
        JSONArray items = jsonObj.getJSONArray("items");
        for(int i = 0, maxlen =items.length(); i< maxlen; i++){
            JSONObject item = items.getJSONObject(i);

            String dur = item.getJSONObject("contentDetails").getString("duration");
            String viewCount = item.getJSONObject("statistics").getString("viewCount");


            // the below are needed
            // duration
            // view count
            ret = new YouTubeVideoItem("", "", "", "", dur, viewCount);


        }



        return ret;
    }



}
