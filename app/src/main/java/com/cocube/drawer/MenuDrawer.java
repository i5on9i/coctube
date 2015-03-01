package com.cocube.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.cocube.R;
import com.cocube.like.LikeSingleton;

public class MenuDrawer extends ListView {


    private int prevSelection = 0;
    private static int LIKE_MENU_ITEM_INDEX = 1;
    private String[] navMenuTitles;


    public MenuDrawer(Context context) {
        super(context);
        init();
    }

    public MenuDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    void init() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        getLikeMenuIndex();

    }

    private void getLikeMenuIndex() {
        LIKE_MENU_ITEM_INDEX = navMenuTitles.length - 2;
    }


    public void refreshLikeCount() {
        NavDrawerListAdapter adapter = (NavDrawerListAdapter) getAdapter();

        if (adapter != null) {
            NavDrawerItem menuItem = (NavDrawerItem) adapter.getItem(LIKE_MENU_ITEM_INDEX);
            menuItem.setCounterVisibility(true);
            menuItem.setCount(Integer.toString(LikeSingleton.getInstance().count()));
            adapter.notifyDataSetChanged();
        }


    }

    public void setItemCheckedAndSelect(int position) {
        setItemChecked(position, true);
        setSelection(position);

        setAdapterSelect(position);


    }

    public void setAdapterSelect(int position) {
        NavDrawerListAdapter adapter = (NavDrawerListAdapter) getAdapter();
        if (adapter != null) {
            adapter.setSelected(position);
        }
        prevSelection = position;


    }


    public void setSelectionWithPrevious() {

        setItemCheckedAndSelect(prevSelection);
    }


}

