package com.ssx86.Intreset;

/**
 * Created by new2 on 2016/1/5.
 */
public class HotsearchModel {
    public int id;

    public String title;
    public String url;


    public HotsearchModel(String title, String url, int id) {
        this.title = title;
        this.url = url;
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }

}
