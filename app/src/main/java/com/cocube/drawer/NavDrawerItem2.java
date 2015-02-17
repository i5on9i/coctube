package com.cocube.drawer;

public class NavDrawerItem2 extends NavDrawerItem{



    private String url;

	public NavDrawerItem2(String title, String url, int icon, int iconSelected){
        super(title, icon, iconSelected);
        this.url = url;
	}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}

