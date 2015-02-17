package com.cocube.otherplaylist;

import android.content.ContentValues;
import android.view.View;

import com.cocube.provider.LolTvContract;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 12
 * Time: 오전 10:18
 */

public class VideoPlayListItem implements View.OnClickListener {


    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SECTION = 1;

    private final int type;
    private final String section;
    private final String title;
    private final String url;
    private final boolean selected;
    public String text;




    public int position;    // just for test



    public VideoPlayListItem(int type, String section, String title, String url, boolean selected) {
        this.type = type;
        this.section = section;
        this.title = title;
        this.url = url;
        this.selected = selected;   // this is ok unless the list is many.
    }



    public int getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getSection() {
        return section;
    }
    public boolean isSelected() {
        return selected;
    }




    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LolTvContract.PlayLists.LIST_CATEGORY, section);
        values.put(LolTvContract.PlayLists.LIST_NAME, title);
        values.put(LolTvContract.PlayLists.LIST_SELECTED, selected);
        values.put(LolTvContract.PlayLists.LIST_URL, url);



        return values;
    }


    ////////////////////////////////////////////////////////////////////
    ////    View.OnClickListener
    ////
    @Override
    public void onClick(View view) {

//        LikeSingleton.getInstance().toggleLikeItem((ImageView)view, this);

    }



}
