package com.novel.lightnovel.Domain;

import java.util.List;

/**
 * Created by Crash on 2015/1/16.
 */
public class SinBook {
    private int id;                 //卷id
    private String name;            //卷名


    private List<String> titles;     //章节名列表
    private List<String> viewIDs;     //章节id·集合（可转章节链接·集合

    public SinBook() {
    }

    public SinBook(int id,  String name) {
        this.id = id;
        this.name = name;
    }

    public String infoTostring(){

        return id+"##"+name;
    }
    public String viewsIdToString(){
        StringBuffer str = new StringBuffer();
        for (String view:viewIDs){
            str.append(view).append(",");
        }
        return str.toString();
    }
    public String titlesToString(){
        StringBuffer str = new StringBuffer();
        for (String string:titles){
            str.append(string).append("##");
        }
        return str.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getViewIDs() {
        return viewIDs;
    }

    public void setViewIDs(List<String> viewIDs) {
        this.viewIDs = viewIDs;
    }
}
