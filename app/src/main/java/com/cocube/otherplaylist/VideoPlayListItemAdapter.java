package com.cocube.otherplaylist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocube.R;
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

public class VideoPlayListItemAdapter extends BaseAdapter
        implements PinnedSectionListView.PinnedSectionListAdapter {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;

    private Map<String, Integer> mItemSet = Collections.synchronizedMap(
            new HashMap<String, Integer>());//Last argument true for LRU ordering

    private List mItems = new ArrayList<VideoPlayListItem>();
    private static final int[] COLORS = new int[] {
            R.color.section_color_1, R.color.section_color_2,
            R.color.section_color_3, R.color.section_color_4 };

    /**
     * layout inflator
     * <p/>
     * Instantiates a layout XML file into its corresponding View objects.
     * {@link : http://developer.android.com/reference/android/view/LayoutInflater.html}
     */
    private LayoutInflater mInflater;
    private HashMap<String, Integer> colorMap = new HashMap<String, Integer>();


    public VideoPlayListItemAdapter() {

        // for test
        String[] secs = {"SEC0", "SEC1", "SEC2", "SEC3", "SEC4"};
        int sectionPosition = 0, listPosition = 0;
        for (char i=0; i<4; i++) {
            VideoPlayListItem sectionItem
                = new VideoPlayListItem(VideoPlayListItem.TYPE_SECTION,
                                secs[sectionPosition],
                                "title",
                                "http://daum.net", false);

            sectionItem.position = listPosition++;
            mItems.add(sectionItem);

            final int itemsNumber = 10;
            for (int j=0;j<itemsNumber;j++) {
                VideoPlayListItem item
                        = new VideoPlayListItem(VideoPlayListItem.TYPE_ITEM,
                                    secs[sectionPosition],
                                    "title2", "http://dddum.net", true);

                item.position = listPosition++;
                mItems.add(item);
            }

            sectionPosition++;
        }


    }

    //--------------------------------------------------------
    public VideoPlayListItemAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //--------------------------------------------------------
    public VideoPlayListItemAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;

    }




    public void addItem(VideoPlayListItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List items) {



    }


    public void replaceData(List items) {
        mItems = items;
        notifyDataSetChanged();

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


    public void setInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }


    @Override
    public int getItemViewType(int position) {
        return ((VideoPlayListItem)mItems.get(position)).getType();

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            return convertView;
        }

        ViewHolder holder;
        VideoPlayListItem item = (VideoPlayListItem) mItems.get(pos);
        int type = getItemViewType(pos);

        if (convertView == null) {

            holder = new ViewHolder();

            switch (type) {
                case VideoPlayListItem.TYPE_ITEM:    // normal
                    convertView = mInflater.inflate(R.layout.item_video_list, null);

                    holder.no = ((TextView) convertView.findViewById(R.id.item_no));
                    holder.name = ((TextView) convertView.findViewById(R.id.item_name));
                    holder.dur = ((TextView) convertView.findViewById(R.id.item_dur));
                    holder.viewCount = ((TextView) convertView.findViewById(R.id.item_view_count));

                    // TODO : like function
                    holder.like = ((ImageView) convertView.findViewById(R.id.item_like));
                    break;
                case VideoPlayListItem.TYPE_SECTION:    // top or bottom
                    convertView = mInflater.inflate(R.layout.item_video_list, null);

                    holder.no = ((TextView) convertView.findViewById(R.id.item_no));
                    holder.name = ((TextView) convertView.findViewById(R.id.item_name));
                    holder.dur = ((TextView) convertView.findViewById(R.id.item_dur));
                    holder.viewCount = ((TextView) convertView.findViewById(R.id.item_view_count));

                    break;
            }

            holder.type = type;

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(item.getTitle());
        holder.viewCount.setText(item.getUrl());


        switch (type){
            case VideoPlayListItem.TYPE_ITEM:
                convertView.setBackgroundColor(Color.WHITE);

                break;
            case VideoPlayListItem.TYPE_SECTION:
                convertView.setBackgroundColor(
                        parent.getResources().getColor(getColorId(item)));
                break;

        }



        return convertView;

    }

    private int getColorId(VideoPlayListItem item) {
        String sId = item.getSection();
        Integer cId = colorMap.get(sId);
        if(cId == null){
            cId = colorMap.size();
            colorMap.put(sId, cId);
        }


        return COLORS[cId % COLORS.length];
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == VideoPlayListItem.TYPE_SECTION;
    }


    ////////////////////////////////////////////////////////////////////
    ////    inner class
    ////
    protected static class ViewHolder {
        int type;
        ImageView thumb;
        TextView no;
        TextView name;
        TextView dur;
        TextView viewCount;
        ImageView like;
    }
}
