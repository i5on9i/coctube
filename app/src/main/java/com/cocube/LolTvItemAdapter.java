package com.cocube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocube.imageloader.ImageLoader;
import com.cocube.like.LikeSingleton;
import com.cocube.parser.YouTubeVideoItem;
import com.cocube.videostats.VideoStatsLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 12
 * Time: 오전 10:18
 */
public class LolTvItemAdapter extends BaseAdapter implements View.OnClickListener{

    public static final int TYPE_ITEM1 = 0;    // normal item
    public static final int TYPE_ITEM_BOUNDARY = 1;    // top or bottom item
    public static final int TYPE_MAX_COUNT = 2;

    private final int mCountOfDummy = 2;    // this is the count of the TYPE_ITEM_BOUNDARY

    protected List mItems = new ArrayList<YouTubeVideoItem>();


    /**
     * layout inflator
     * <p/>
     * Instantiates a layout XML file into its corresponding View objects.
     * {@link : http://developer.android.com/reference/android/view/LayoutInflater.html}
     */
    private LayoutInflater mInflater;


    public LolTvItemAdapter() {

    }

    //--------------------------------------------------------
    public LolTvItemAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //--------------------------------------------------------
    public LolTvItemAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;

    }

    public void setInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;


    }


    //--------------------------------------------------------
//    public void addItem(Report item) {
//        mItems.add(item);
//        notifyDataSetChanged();
//    }

    //--------------------------------------------------------
    public void addAll(List items) {



        if (!existDummy()) {
            addDummyItems(mItems);
        }

        mItems.addAll(mItems.size() - 1, items);
        notifyDataSetChanged();
    }


    protected boolean existDummy() {
        return mItems.size() >= getCountOfDummy();
    }


    //--------------------------------------------------------
    @Override
    public int getCount() {
        return mItems.size();
    }


    //--------------------------------------------------------
    @Override
    public Object getItem(int position) {

        return mItems.get(position);
    }


    //--------------------------------------------------------
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0 || position == mItems.size() - 1) {
            return TYPE_ITEM_BOUNDARY;
        }

        return TYPE_ITEM1;
    }


    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    //--------------------------------------------------------
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        if (mInflater == null) {
            return convertView;
        }

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            int type = getItemViewType(pos);

            switch (type) {
                case TYPE_ITEM1:    // normal
                    convertView = mInflater.inflate(R.layout.item_video, null);

                    holder.thumb = ((ImageView) convertView.findViewById(R.id.item_thumb));
                    holder.no = ((TextView) convertView.findViewById(R.id.item_no));
                    holder.name = ((TextView) convertView.findViewById(R.id.item_name));
                    holder.updated = ((TextView) convertView.findViewById(R.id.item_updated));
                    holder.dur = ((TextView) convertView.findViewById(R.id.item_dur));
                    holder.viewCount = ((TextView) convertView.findViewById(R.id.item_view_count));

                    // TODO : like function
                    holder.like = ((ImageView) convertView.findViewById(R.id.item_like));
                    break;
                case TYPE_ITEM_BOUNDARY:    // top or bottom
                    convertView = mInflater.inflate(R.layout.item_video2, null);
                    break;
            }

            holder.type = type;


            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        YouTubeVideoItem item = (YouTubeVideoItem) mItems.get(pos);

        if (holder.type == TYPE_ITEM1) {

            String imageUrl = item.getThumbHq();
            String imageKey = item.getUpdated() + imageUrl;
            ImageLoader.getInstance().displayImage(holder.thumb, imageUrl, imageKey);

            // TODO : like function
            holder.like.setOnClickListener(item);
            LikeSingleton.getInstance().displayLike(holder.like, item.getYoutubeId());

            holder.no.setText(Integer.toString(pos));
            holder.name.setText(item.getTitle());
            holder.updated.setText(item.getDateSpanString());

            VideoStatsLoader.getInstance()
                    .displayDurationViewCount(holder.dur, holder.viewCount, item.getYoutubeId());
        } else {
            // do nothing
        }


        return convertView;


    }


    /**
     *
     * @param items notice, two boundary items are added to {@param: items} after run this method
     */
    public void replaceData(List items) {

        mItems = items;
        addDummyItems(mItems);
        notifyDataSetChanged();

    }

    private void addDummyItems(List list) {
        // boundary(dummy) nodes
        list.add(0, new YouTubeVideoItem("", "", "", "", "", ""));
        list.add(new YouTubeVideoItem("", "", "", "", "", ""));
    }


    public int getCountOfDummy() {
        return mCountOfDummy;
    }




//    public void setData(Cursor cursor) {
//        mItems = generateListWithEntries(cursor);
//    }
//
//    private List generateListWithEntries(Cursor cursor) {
//        List mListItems = new ArrayList<YouTubeVideoItem>();
//
//        if (cursor != null) {
//
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//
//                String title = cursor.getString(cursor.getColumnIndex(LolTvItemContract.Item.COLUMN_NAME_TITLE));
//                String desc = cursor.getString(cursor.getColumnIndex(LolTvItemContract.Item.COLUMN_NAME_DESC));
//                String vId = cursor.getString(cursor.getColumnIndex(LolTvItemContract.Item.COLUMN_NAME_YOUTUBEID));
//                String published = cursor.getString(cursor.getColumnIndex(LolTvItemContract.Item.COLUMN_NAME_PUBLISHED));
//
//                mListItems.add(new YouTubeVideoItem(title, desc, vId, published));
//                cursor.moveToNext();
//            }
//
//        }
//        return mListItems;
//    }


    ////////////////////////////////////////////////////////
    //// View.OnClickListener
    ////
    @Override
    public void onClick(View v) {








    }



    ////////////////////////////////////////////////////////////////////
    ////    inner class
    ////
    protected static class ViewHolder {
        int type;
        ImageView thumb;
        TextView no;
        TextView name;
        TextView updated;
        TextView dur;
        TextView viewCount;
        ImageView like;
    }


}
