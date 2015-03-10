package com.novel.lightnovel.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Crash on 2015/1/18.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";
    public static int VERSION = 1;

    public static String DB_NAME = "novel.db";
    public static String TB_VOLLIST = "tb_vollist";
    public static String TB_BOOK = "tb_book";
    public static String TB_VIEW = "tb_view";
    public static String TB_ILLUSTRATION = "tb_illustration";
    public static String TB_FAVOR = "tb_favor";
    public static String TB_HISTORY = "tb_history";

    private static final String CREATE_TB_VOLLIST = "CREATE TABLE tb_vollist (\n" +
            "v_id  INTEGER PRIMARY KEY NOT NULL,\n" +
            "v_name  TEXT,\n" +
            "v_cover TEXT,\n" +
            "author TEXT,\n" +
            "illustration TEXT,\n" +
            "library TEXT,\n" +
            "counts  INTEGER,\n" +
            "updatetime TEXT,\n" +
            "last_title TEXT,\n" +
            "last_id  INTEGER,\n" +
            "about TEXT\n" +
            ");";
    private static final String CREATE_TB_BOOK = "CREATE TABLE tb_book (\n" +
            "b_id  INTEGER PRIMARY KEY NOT NULL,\n" +
            "b_name  TEXT,\n" +
            "b_cover TEXT,\n" +
            "v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";
    private static final String CREATE_TB_VIEW = "CREATE TABLE tb_view (\n" +
            "view_id INTEGER PRIMARY KEY NOT NULL,\n" +
            "view_name TEXT,\n" +
            "v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
            "b_id INTEGER REFERENCES tb_book(b_id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";
    private static final String CREATE_TB_ILLUSTRATION = "CREATE TABLE tb_illustration (\n" +
            "i_id INTEGER PRIMARY KEY NOT NULL,\n" +
            "i_link TEXT,\n" +
            "v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
            "b_id INTEGER REFERENCES tb_book(b_id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";
    private static final String CREATE_TB_FAVOR = "CREATE TABLE tb_favor (\n" +
            "f_id INTEGER PRIMARY KEY NOT NULL,\n" +
            "f_time TEXT,\n" +
            "v_id INTEGER UNIQUE REFERENCES tb_vollist(v_id),\n" +
            "b_id INTEGER REFERENCES tb_book(b_id),\n" +
            "view_id INTEGER REFERENCES tb_view(view_id),\n" +
            "pos INTEGER\n" +
            ");";
    private static final String CREATE_TB_HISTORY = "CREATE TABLE tb_history(\n" +
            "h_id INTEGER PRIMARY KEY NOT NULL,\n" +
            "h_time TEXT,\n" +
            "v_id INTEGER REFERENCES tb_vollist(v_id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
            "b_id INTEGER REFERENCES tb_book(b_id)ON DELETE CASCADE ON UPDATE CASCADE,\n" +
            "view_id INTEGER UNIQUE REFERENCES tb_view(view_id)ON DELETE CASCADE ON UPDATE CASCADE,\n" +
            "pos INTEGER\n" +
            ");";

    public static String insert_test1 = "INSERT INTO tb_vollist VALUES(1,'name','http://','author','illustration','library',0,'updatetime','最新章节',0,'about');";
    public static String insert_test2 = "INSERT INTO tb_book VALUES(1,'name','http://',1);";
    public static String insert_test3 = "INSERT INTO tb_view VALUES(1,'name',1,1);";
    public static String insert_test4 = "INSERT INTO tb_favor VALUES(1,'2015-02-12',1,1,1,0);";
    public static String insert_test5 = "INSERT INTO tb_illustration VALUES(1,'http://',1,1);";
    public static String insert_test6 = "INSERT INTO tb_history VALUES(1,'2015-02-23',1,1,1,0);";


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    //数据库第一次打开时候创建表（只调用一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB_VOLLIST);
        db.execSQL(CREATE_TB_BOOK);
        db.execSQL(CREATE_TB_VIEW);
        db.execSQL(CREATE_TB_ILLUSTRATION);
        db.execSQL(CREATE_TB_FAVOR);
        db.execSQL(CREATE_TB_HISTORY);
        Log.d(TAG, CREATE_TB_VOLLIST);

        insert_test(db);
    }

    private void insert_test(SQLiteDatabase db) {
        db.execSQL(insert_test1);//插入测试数据
        db.execSQL(insert_test2);//插入测试数据
        db.execSQL(insert_test3);//插入测试数据
        db.execSQL(insert_test4);//插入测试数据
        db.execSQL(insert_test5);//插入测试数据
//        db.execSQL(insert_test6);//插入测试数据
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级时候调用，版本不一致时候
    }


}
