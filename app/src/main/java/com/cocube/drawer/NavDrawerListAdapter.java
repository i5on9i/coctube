package com.cocube.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocube.R;

import java.util.ArrayList;
import java.util.List;

public class NavDrawerListAdapter extends BaseAdapter {

    private final int TYPE_SELECTED = 0;
    private final int TYPE_NOTSELECTED = 1;
    private final int TYPE_MAX_COUNT = 2;


    private final LayoutInflater mInflater;

    private Context mContext;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private int mCurrentSelected = 0;
    private final int mTitleColor;
    private final int mTitleInversedColor;


    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {

        mTitleColor = context.getResources().getColor(R.color.drawer_menu_list_item_title);
        mTitleInversedColor = context.getResources().getColor(R.color.drawer_menu_list_item_title_inversed);
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * getItemViewType()
     * getViewTypeCount()
     * are needed for the different types of listitem.
     */
//    //--------------------------------------------------------
//    @Override
//    public int getItemViewType(int position) {
//
//        return (mCurrentSelected == position) ? TYPE_SELECTED : TYPE_NOTSELECTED;
//    }
//
//    //--------------------------------------------------------
//    @Override
//    public int getViewTypeCount() {
//        return TYPE_MAX_COUNT;
//    }
//
//
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.drawer_menu_list_item, null);

            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.count = (TextView) convertView.findViewById(R.id.counter);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        NavDrawerItem item = navDrawerItems.get(position);

        int icon = item.getIcon();
        holder.title.setTextColor(mTitleColor);
        if (item.isSelected()) {
            icon = item.getIconSelected();
            holder.title.setTextColor(mTitleInversedColor);
        }

        holder.icon.setImageResource(icon);
        holder.title.setText(item.getTitle());

        // displaying count
        // check whether it set visible or not
        if (item.getCounterVisibility()) {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText(item.getCount());
        } else {
            // hide the counter view
            holder.count.setVisibility(View.GONE);
        }

        return convertView;
    }


    public void setSelected(int pos) {

        navDrawerItems.get(mCurrentSelected).setSelected(false);
        navDrawerItems.get(pos).setSelected(true);
        mCurrentSelected = pos;

        notifyDataSetChanged();

    }

    public void addAll(List<NavDrawerItem> items){
        navDrawerItems.addAll(items);
    }


    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView count;

    }

}
