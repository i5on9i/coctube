package com.cocube.like;

import android.content.Context;
import android.view.LayoutInflater;

import com.cocube.LolTvItemAdapter;
import com.cocube.parser.YouTubeVideoItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 12
 * Time: 오전 10:18
 */

public class LikeItemAdapter extends LolTvItemAdapter {


    private Map<String, Integer> mItemSet = Collections.synchronizedMap(
            new HashMap<String, Integer>());//Last argument true for LRU ordering


    /**
     * layout inflator
     * <p/>
     * Instantiates a layout XML file into its corresponding View objects.
     * {@link : http://developer.android.com/reference/android/view/LayoutInflater.html}
     */
    private LayoutInflater mInflater;


    public LikeItemAdapter() {

    }

    //--------------------------------------------------------
    public LikeItemAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //--------------------------------------------------------
    public LikeItemAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;

    }


    public void addItem(YouTubeVideoItem item) {
        if (!mItemSet.containsKey(item.getYoutubeId())) {
            mItems.add(item);
            notifyDataSetChanged();
        }

    }

    @Override
    public void addAll(List items) {

        super.addAll(extractUniqueItems(items));

    }


    @Override
    public void replaceData(List items) {


        // item has only unique items because when the item is added to the List, list checks it.

        List<YouTubeVideoItem> list = items;
        int len = items.size();
        int i = len;
        while(--i >=0 ){
            mItemSet.put(list.get(i).getYoutubeId(), i);
        }


        super.replaceData(items);

    }

    private ArrayList<YouTubeVideoItem> extractUniqueItems(List items) {
        ArrayList<YouTubeVideoItem> paramItems = new ArrayList<YouTubeVideoItem>();
        int size = items.size();
        for (int i = 0; i < size; i++) {
            YouTubeVideoItem item = (YouTubeVideoItem) items.get(i);
            if (mItemSet.put(item.getYoutubeId(), i) == null) {
                paramItems.add(item);
            }
        }
        return paramItems;
    }

    public void removeItem(YouTubeVideoItem item) {
        String key = item.getYoutubeId();
        if (mItemSet.containsKey(key)) {
            int idx = mItemSet.remove(key);
            mItems.remove(idx);
        }

    }


}
