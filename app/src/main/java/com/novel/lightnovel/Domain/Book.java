package com.novel.lightnovel.Domain;

import java.util.List;

/**
 * Created by Crash on 2015/1/6.
 */
public class Book {
    private int id;                 //卷id
    private int vollistId;          //系列小说ID(父类
    private String name;            //卷名
    private String author;          //作者
    private String illustration;    //插画师
    private String library;         //文库
    private String updatetime;      //更新时间
    private int number;             //浏览数量
    private int favor;              //收藏数量

    private String cover;           //封面

    private List<String> titles;     //章节名列表
    private List<String> viewIDs;     //章节id·集合（可转章节链接·集合

    public Book() {
    }

    public Book(int id, int vollistId,String name, String author, String illustration, String library, String updatetime, int number, int favor) {
        this.id = id;
        this.vollistId = vollistId;
        this.name = name;
        this.author = author;
        this.illustration = illustration;
        this.library = library;
        this.updatetime = updatetime;
        this.number = number;
        this.favor = favor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVollistId() {
        return vollistId;
    }

    public void setVollistId(int vollistId) {
        this.vollistId = vollistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFavor() {
        return favor;
    }

    public void setFavor(int favor) {
        this.favor = favor;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(id).append("##").append(vollistId).append("##").append(name).append("##").append(author).append("##").append(illustration).append("##").append(library).append("##").append(updatetime).append("##").append(number).append("##").append(favor);
        return stringBuffer.toString();
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

}
