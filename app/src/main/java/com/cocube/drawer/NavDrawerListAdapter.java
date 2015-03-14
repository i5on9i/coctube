package com.cocube.drawer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocube.R;

import java.util.ArrayList;
import java.util.List;



public class NavDrawerListAdapter extends RecyclerView.Adapter<NavDrawerListAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final int TYPE_SELECTED = 0;
    private final int TYPE_NOTSELECTED = 1;
    private final int TYPE_MAX_COUNT = 2;


    private final LayoutInflater mInflater;
    private final View.OnClickListener mOnClickHandler;

    private Context mContext;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private int mCurrentSelected = 0;
    private final int mTitleColor;
    private final int mTitleInversedColor;


    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, View.OnClickListener handler) {

        mOnClickHandler = handler;


        mTitleColor = context.getResources().getColor(R.color.drawer_menu_list_item_title);
        mTitleInversedColor = context.getResources().getColor(R.color.drawer_menu_list_item_title_inversed);
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.navDrawerItems = navDrawerItems;
    }

//    @Override
//    public int getCount() {
//        return navDrawerItems.size();
//    }


    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu_list_item,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType, mOnClickHandler); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu_header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType, mOnClickHandler); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }


    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.holderId ==1) {                              // as the list view is going to be called after the header view so we decrement the
            int drawerItemIndex = position - 1;
            NavDrawerItem item = navDrawerItems.get(drawerItemIndex);

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

        }
        else{

//            holder.profile.setImageResource(profileId);           // Similarly we set the resources for header view
//            holder.name.setText(name);
//            holder.email.setText(email);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size() + 1;   // +1 for header
    }


    /**
     * getItemViewType()
     * getViewTypeCount()
     * are needed for the different types of listitem.
     */

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//
//        ViewHolder holder = null;
//
//        if (convertView == null) {
//
//            holder = new ViewHolder();
//
//            convertView = mInflater.inflate(R.layout.drawer_menu_list_item, null);
//
//            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
//            holder.title = (TextView) convertView.findViewById(R.id.title);
//            holder.count = (TextView) convertView.findViewById(R.id.counter);
//            convertView.setTag(holder);
//
//
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//
//        NavDrawerItem item = navDrawerItems.get(position);
//
//        int icon = item.getIcon();
//        holder.title.setTextColor(mTitleColor);
//        if (item.isSelected()) {
//            icon = item.getIconSelected();
//            holder.title.setTextColor(mTitleInversedColor);
//        }
//
//        holder.icon.setImageResource(icon);
//        holder.title.setText(item.getTitle());
//
//        // displaying count
//        // check whether it set visible or not
//        if (item.getCounterVisibility()) {
//            holder.count.setVisibility(View.VISIBLE);
//            holder.count.setText(item.getCount());
//        } else {
//            // hide the counter view
//            holder.count.setVisibility(View.GONE);
//        }
//
//        return convertView;
//    }


    public void setSelected(int pos) {

        navDrawerItems.get(mCurrentSelected).setSelected(false);
        navDrawerItems.get(pos).setSelected(true);
        mCurrentSelected = pos;

        notifyDataSetChanged();

    }

    public void addAll(List<NavDrawerItem> items) {
        navDrawerItems.addAll(items);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView name;
        TextView email;
        ImageView profile;


        ImageView icon;
        TextView title;
        TextView count;


        public ViewHolder(View itemView, int ViewType, View.OnClickListener handler) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                icon = (ImageView) itemView.findViewById(R.id.icon);
                title = (TextView) itemView.findViewById(R.id.title);
                count = (TextView) itemView.findViewById(R.id.counter);
                itemView.setOnClickListener(handler);
                holderId = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            } else {
                name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                holderId = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }
}