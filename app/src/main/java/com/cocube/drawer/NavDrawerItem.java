package com.cocube.drawer;

public class NavDrawerItem {


    private String title;
    private int icon;
    private int iconSelected;

    private String count = "0";
	// boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
    private boolean selected = false;


    public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon, int iconSelected){
		this.title = title;
		this.icon = icon;
        this.iconSelected = iconSelected;
	}
	
	public NavDrawerItem(String title, int icon, int iconSelected, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
        this.iconSelected = iconSelected;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}

    public int getIconSelected(){
        return this.iconSelected;
    }

	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected(){
        return selected;
    }
}

