package com.novel.lightnovel.DataBase;

import android.content.ContentValues;
import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Crash on 2015/2/27.
 */
public class DataBaseDao2 extends DataBaseDao {
    public DataBaseDao2(Context context) {
        super(context);
    }

    @Override
    public List<ContentValues> getVollist(int v_id) {
        return super.getVollist(v_id);
    }

    @Override
    public List<ContentValues> getBook(int b_id) {
        return super.getBook(b_id);
    }

    @Override
    public List<ContentValues> getView(int view_id) {
        return super.getView(view_id);
    }

    private List<ContentValues> getCV(String sql) {
        String url = "jdbc:mysql://a7xsk.gotoftp4.com:3306/a7xsk";
        String user = "a7xsk";
        String pass = "sdws12580";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("加载驱动异常");
        }
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            if (conn != null) {
                System.out.println("连接成功");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                List<ContentValues> list = new ArrayList<>();
                ContentValues cv = null;
                while (rs.next()){
                    cv = new ContentValues();
                }

            }
        } catch (Exception e) {
            System.out.println("数据库链接失败");
        }
        return null;
    }
}
