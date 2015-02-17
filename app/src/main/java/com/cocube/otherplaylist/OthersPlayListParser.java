package com.cocube.otherplaylist;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 13
 * Time: 오후 10:32
 * To change this template use File | Settings | File Templates.
 */


import com.cocube.parser.ParserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.StringUtils;


public class OthersPlayListParser {


    public static final String LANGUAGE_KR = "kr";
    public static final String LANGUAGE_EN = "en";


    private ParserInfo mInfo;
    private static final String START_POINT = "\\{category-list\\}";
    private static final String END_POINT = "\\{/category-list\\}";

    public OthersPlayListParser(ParserInfo info) {
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
    public List<VideoPlayListItem> parse(InputStream stream, String lang) throws IOException, JSONException {


        ArrayList<VideoPlayListItem> entries = new ArrayList<VideoPlayListItem>();

        String jsonString = getJsonData(stream);

        JSONObject jsonObj = new JSONObject(jsonString);


        JSONArray entry = jsonObj.getJSONArray(lang);

        int sectionId = 0;
        int maxlen = entry.length();
        for (int i = 0; i < maxlen; ++i) {
            JSONObject item = entry.getJSONObject(i);
            String category = item.getString("id");
            String cUrl = item.getString("url");
            JSONArray playlists = item.getJSONArray("playlists");

            entries.add(
                    new VideoPlayListItem(VideoPlayListItem.TYPE_SECTION,
                            category, category, cUrl, false));

            int listLen = playlists.length();
            for (int j = 0; j < listLen; ++j) {
                JSONObject list = playlists.getJSONObject(j);
                String name = list.getString("name");
                String url = list.getString("url");

                // TODO
                // make playlist info
                boolean selected = false; // for test
                entries.add(
                        new VideoPlayListItem(VideoPlayListItem.TYPE_ITEM,
                                category, name, url, false));
            }
            sectionId++;

        }


        return entries;
    }

    private String getJsonData(InputStream stream) throws IOException {
        String feedPage = StringUtils.convertStreamToString(stream, mInfo.getPageEncoding());

        final String regx = START_POINT + "(.*)"+ END_POINT;
        Pattern p = Pattern.compile(regx, Pattern.DOTALL);
        Matcher m = p.matcher(feedPage);

        if(m.find())
            return m.group(1);
        else
            return "";
    }


}
