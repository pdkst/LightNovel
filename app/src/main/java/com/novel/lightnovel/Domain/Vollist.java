package com.novel.lightnovel.Domain;

import java.util.List;

/**
 * Created by Crash on 2015/1/6.
 */
public class Vollist {
    private int id;                 //小说id
    private String name;            //小说名
    private String author;          //作者
    private String illustration;    //插画师
    private String library;         //文库
    private int count;              //浏览数量
    private String updatetime;      //最后更新时间
    private String lastBook;        //最新卷名
    private String lastBookLink;    //最新卷链接
    private String about;           //简介

    private List<SinBook> bookList;
    private List<String> coverList; //封面链接

    public Vollist() {
        this.id = -1;
        this.name = "null";
        this.author = "null";
        this.illustration = "null";
        this.library = "null";
        this.count = -1;
        this.updatetime = "null";
        this.lastBook = "null";
        this.lastBookLink = "null";
        this.about = "null";
    }

    public Vollist(int id, String name, String author, String illustration, String library, int count, String updatetime, String lastBook, String lastBookLink, String about) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.illustration = illustration;
        this.library = library;
        this.count = count;
        this.updatetime = updatetime;
        this.lastBook = lastBook;
        this.lastBookLink = lastBookLink;
        this.about = about;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getLastBook() {
        return lastBook;
    }

    public void setLastBook(String lastBook) {
        this.lastBook = lastBook;
    }

    public String getLastBookLink() {
        return lastBookLink;
    }

    public void setLastBookLink(String lastBookLink) {
        this.lastBookLink = lastBookLink;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<SinBook> getBookList() {
        return bookList;
    }

    public void setBookList(List<SinBook> bookList) {
        this.bookList = bookList;
    }

    public List<String> getCoverList() {
        return coverList;
    }

    public void setCoverList(List<String> coverList) {
        this.coverList = coverList;
    }

    @Override
    public String toString() {
        //(id,name,author,illustration,library,count,updatetime,lastBook,lastBookLink,about)
        return  name+"##"+author+"##"+illustration+"##"+library+"##"+count+"##"+updatetime+"##"+lastBook+"##"+lastBookLink+"##"+about;
    }
    public String coverToString(){
        StringBuffer stringBuffer = new StringBuffer();
        for (String str:coverList){
            stringBuffer.append(str).append("@@");
        }
        return stringBuffer.toString();
    }
    public String bookIdToString(){
        StringBuffer str = new StringBuffer();
        for (SinBook book:bookList){
            str.append(book.getId()).append(",");
        }
        return str.toString();
    }
}
