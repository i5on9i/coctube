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
         * {
             "kind": "youtube#searchListResponse",
             "etag": "\"9Y5jTkxN1JET3y-M4wKMA5aK7Mk/WwcGgU_smaWbB8xz259IP-Koa74\"",
             "nextPageToken": "CAUQAA",
             "pageInfo": {
              "totalResults": 1428,
              "resultsPerPage": 5
             },
             "items": [
              {
               "kind": "youtube#searchResult",
               "etag": "\"9Y5jTkxN1JET3y-M4wKMA5aK7Mk/ZK6AIbfdTb29-QPl3U_SlbEEkn4\"",
               "id": {
                "kind": "youtube#video",
                "videoId": "c-lZPAttcU4"
               },
               "snippet": {
                "publishedAt": "2015-02-25T12:00:00.000Z",
                "channelId": "UCqgRbodod44OjLp9tqpgkPw",
                "title": "[라이너TV] 식물 VS 좀비 26화 - 광합성의 힘으로 좀비를 물리치자!",
                "description": "라이너가 식좀에 손을 댔다! 컬트적인 인기를 구가하며 디펜스 게임 계에 새 바람을 불러온 바로 그 게임! 그저 그런 좀비 디펜스가 아니다! 귀여운...",
                "thumbnails": {
                 "default": {
                  "url": "https://i.ytimg.com/vi/c-lZPAttcU4/default.jpg"
                 },
                 "medium": {
                  "url": "https://i.ytimg.com/vi/c-lZPAttcU4/mqdefault.jpg"
                 },
                 "high": {
                  "url": "https://i.ytimg.com/vi/c-lZPAttcU4/hqdefault.jpg"
                 }
                },
                "channelTitle": "TheRainerTV",
                "liveBroadcastContent": "none"
               }
              },
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


            JSONObject snippet = item.getJSONObject("snippet");
            String videoId = getVideoId(item, snippet);

            String title = snippet.getString("title");
            String publishedAt = snippet.getString("publishedAt");
            String desc = snippet.getString("description");

            // the below are needed
            // duration
            // view count
            entries.add(new YouTubeVideoItem(title, desc,
                    videoId, publishedAt, "None", "None"));


        }



        return entries;
    }


    /**
     *
     * Because the data from playlistItem has different format for video ID, so
     * this method is needed.
     *
     *
     * json data from playlistItem
     *
     * {
         ...
         "items": [
          {
            ...
           "id": "PLDFFwwxeSQ_pjIiEG4L-q5u1x2w58WkUj-v2pu7t0Kls",
           "snippet": {
            ...
            "resourceId": {
             "kind": "youtube#video",
             "videoId": "wtTRZuOKpHs"
            }
           }
          },
     * @param item
     * @param snippet
     * @return
     * @throws JSONException
     */
    private String getVideoId(JSONObject item, JSONObject snippet) throws JSONException {
        String videoId = "";
        String kind = item.getString("kind");
        if(kind.equals(ParserInfo.DataKind.PLAYLIST_ITEM)){
            JSONObject resourceId = snippet.getJSONObject("resourceId");
            videoId = resourceId.getString("videoId");
        }else{
            JSONObject id = item.getJSONObject("id");
            videoId = id.getString("videoId");
        }
        return videoId;
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
