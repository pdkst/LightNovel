package com.novel.lightnovel.DataBase;

/**
 * Created by Crash on 2015/3/1.
 */
public class D {

    public static String DB_NAME = "novel.db";
    public static String TB_VOLLIST = "tb_vollist";
    public static String TB_BOOK = "tb_book";
    public static String TB_VIEW = "tb_view";
    public static String TB_ILLUSTRATION = "tb_illustration";
    public static String TB_FAVOR = "tb_favor";
    public static String TB_HISTORY = "tb_history";
//    CREATE TABLE tb_vollist (
//            v_id  INTEGER PRIMARY KEY NOT NULL,
//            v_name  TEXT,
//            v_cover TEXT,
//            author TEXT,
//            illustration TEXT,
//            library TEXT,
//            counts  INTEGER,
//            updatetime TEXT,
//            last_title TEXT,
//            last_id  INTEGER,
//            about TEXT
//    );

    public static String v_id = "v_id";
    public static String v_name = "v_name";
    public static String v_cover = "v_cover";
    public static String author = "author";
    public static String illustration = "illustration";
    public static String library = "library";
    public static String counts = "counts";
    public static String updatetime = "updatetime";
    public static String last_title = "last_title";
    public static String last_id = "last_id";
    public static String about = "about";
//    CREATE TABLE tb_book (
//            b_id  INTEGER PRIMARY KEY NOT NULL,
//            b_name  TEXT,
//            b_cover TEXT,
//            v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE
//    );
    public static String b_id = "b_id";
    public static String b_name = "b_name";
    public static String b_cover = "b_cover";
//    CREATE TABLE tb_view (
//            view_id INTEGER PRIMARY KEY NOT NULL,
//            view_name TEXT,
//            v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE,
//    b_id INTEGER REFERENCES tb_book(b_id) ON DELETE CASCADE ON UPDATE CASCADE
//    );
    public static String view_id = "view_id";
    public static String view_name = "view_name";
//    CREATE TABLE tb_illustration (
//            i_id INTEGER PRIMARY KEY NOT NULL,
//            i_link TEXT,
//            v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE,
//    b_id INTEGER REFERENCES tb_book(b_id) ON DELETE CASCADE ON UPDATE CASCADE
//    );
    public static String i_id = "i_id";
    public static String i_link = "i_link";
//    CREATE TABLE tb_favor (
//            f_id INTEGER PRIMARY KEY NOT NULL,
//            f_time TEXT,
//            v_id INTEGER UNIQUE REFERENCES tb_vollist(v_id),
//    b_id INTEGER REFERENCES tb_book(b_id),
//    view_id INTEGER REFERENCES tb_view(view_id),
//    pos INTEGER
//    );
    public static String f_id = "f_id";
    public static String f_time = "f_time";
    public static String pos = "pos";
//    CREATE TABLE tb_history(
//            h_id INTEGER PRIMARY KEY NOT NULL,
//            h_time TEXT,
//            v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE,
//    b_id INTEGER REFERENCES tb_book(b_id)ON DELETE CASCADE ON UPDATE CASCADE,
//    view_id INTEGER UNIQUE REFERENCES tb_view(view_id)ON DELETE CASCADE ON UPDATE CASCADE,
//    pos INTEGER
//    );
    public static String h_id = "h_id";
    public static String h_time = "h_time";
}
