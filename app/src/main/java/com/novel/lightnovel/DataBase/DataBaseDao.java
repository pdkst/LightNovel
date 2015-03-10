package com.novel.lightnovel.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Crash on 2015/1/26.
 */
public class DataBaseDao implements DataBase {
    private final static String TAG = "DataBaseDao";

    private Context context;

    public DataBaseDao(Context context) {
        this.context = context;
    }


    @Override
    public boolean isFavor(int v_id) {
        List<ContentValues> list = getFavor(v_id);
        return !list.isEmpty();
    }

    @Override
    public boolean addFavor(int v_id) {
        ContentValues favor = new ContentValues();
        favor.put(D.f_id, v_id);
        favor.put(D.v_id, v_id);
        favor.put(D.b_id, -1);
        favor.put(D.view_id, -1);
        List<ContentValues> books = getBook_list(v_id);
        if (!books.isEmpty()){
            int b_id = books.get(0).getAsInteger(D.b_id);
            favor.put(D.b_id,b_id);
            List<ContentValues> views = getView_list(b_id);
            if (!views.isEmpty()){
                int view_id = views.get(0).getAsInteger(D.view_id);
                favor.put(D.view_id,view_id);
            }
        }
        favor.put(D.pos, 0);
        favor.put(D.f_time, System.currentTimeMillis());
        return insertFavor(favor);
    }

    //增删改查；
    @Override
    public boolean insertVollist(ContentValues vollist) {
        SQLiteDatabase db = null;
        long row = -1;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.insert(DataBaseHelper.TB_VOLLIST, null, vollist);
        } catch (Exception e) {
            e.printStackTrace();
            updateVollist(vollist);
        } finally {
            if (db != null) db.close();
        }
        return row != -1;
    }

    @Override
    public boolean insertBook(ContentValues book) {
        SQLiteDatabase db = null;
        long row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.insert(DataBaseHelper.TB_BOOK, null, book);
        } catch (Exception e) {
            e.printStackTrace();
            updateBook(book);
        } finally {
            if (db != null) db.close();
        }
        return row != -1;
    }

    @Override
    public boolean insertView(ContentValues view) {
        SQLiteDatabase db = null;
        long row = -1;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.insert(DataBaseHelper.TB_VIEW, null, view);
        } catch (Exception e) {
            e.printStackTrace();
            updateView(view);
        } finally {
            if (db != null) db.close();
        }
        return row != -1;
    }

    @Override
    public boolean insertIllustration(ContentValues ill) {
        SQLiteDatabase db = null;
        long row = -1;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.insert(DataBaseHelper.TB_ILLUSTRATION, null, ill);
        } catch (Exception e) {
            e.printStackTrace();
            updateIllustration(ill);
        } finally {
            if (db != null) db.close();
        }
        return row != -1;
    }

    @Override
    public boolean insertHistory(ContentValues history) {
        SQLiteDatabase db = null;
        long row = -1;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.insert(DataBaseHelper.TB_HISTORY, null, history);
        } catch (Exception e) {
            e.printStackTrace();
            updateHistory(history);
        } finally {
            if (db != null) db.close();
        }
        return row != -1;
    }

    @Override
    public boolean insertFavor(ContentValues favor) {
        SQLiteDatabase db = null;
        long row = -1;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.insert(DataBaseHelper.TB_FAVOR, null, favor);
        } catch (Exception e) {
            e.printStackTrace();
            updateFavor(favor);
        } finally {
            if (db != null) db.close();
        }
        return row != -1;
    }

    @Override
    public boolean deleteVollist(int v_id) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.delete(DataBaseHelper.TB_VOLLIST, "v_id=?", new String[]{String.valueOf(v_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean deleteBook(int b_id) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.delete(DataBaseHelper.TB_BOOK, "b_id=?", new String[]{String.valueOf(b_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean deleteView(int view_id) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.delete(DataBaseHelper.TB_VIEW, "view_id=?", new String[]{String.valueOf(view_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean deleteIllustration(int i_id) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.delete(DataBaseHelper.TB_VIEW, "i_id=?", new String[]{String.valueOf(i_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean deleteFavor(int f_id) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.delete(DataBaseHelper.TB_FAVOR, "f_id=?", new String[]{String.valueOf(f_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean deleteHistory(int h_id) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.delete(DataBaseHelper.TB_HISTORY, "h_id=?", new String[]{String.valueOf(h_id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean updateVollist(ContentValues cv) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.update(DataBaseHelper.TB_VOLLIST, cv, "v_id=?", new String[]{cv.getAsString("v_id")});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean updateBook(ContentValues cv) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.update(DataBaseHelper.TB_BOOK, cv, "b_id=?", new String[]{cv.getAsString("b_id")});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean updateView(ContentValues cv) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.update(DataBaseHelper.TB_VIEW, cv, "view_id=?", new String[]{cv.getAsString("view_id")});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean updateIllustration(ContentValues cv) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.update(DataBaseHelper.TB_VIEW, cv, "i_id=?", new String[]{cv.getAsString("i_id")});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean updateFavor(ContentValues cv) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.update(DataBaseHelper.TB_FAVOR, cv, "v_id=?", new String[]{cv.getAsString("v_id")});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public boolean updateHistory(ContentValues cv) {
        SQLiteDatabase db = null;
        int row = 0;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getWritableDatabase();
            row = db.update(DataBaseHelper.TB_HISTORY, cv, "v_id=? and b_id=?", new String[]{cv.getAsString("v_id"), cv.getAsString("b_id")});
        } catch (Exception e) {
            Log.e(TAG,"-----更新失败----->"+cv.toString());
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return row != 0;
    }

    @Override
    public List<ContentValues> getVollist(int v_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_vollist where v_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(v_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getBook(int b_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_book where b_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(b_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getView(int view_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_view where view_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(view_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getIllustration(int i_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_illustration where i_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(i_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getFavor(int v_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_favor where v_id = ? order by f_time desc;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(v_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getHistory(int b_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_history where b_id = ? order by h_time desc;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(b_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getHistorys(int v_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_history where v_id = ? order by h_time desc;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(v_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getVollist_list() {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_vollist;";
            Cursor cursor = db.rawQuery(sql, null);
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getBook_list(int v_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_book where v_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(v_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getView_list(int b_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_view where b_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(b_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getIll_list(int b_id) {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_illustration where b_id = ?;";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(b_id)});
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getFavor_list() {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM tb_favor NATURAL JOIN tb_vollist ORDER BY f_time DESC;";
            Cursor cursor = db.rawQuery(sql, null);
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getFavor_list2() {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(" tb_vollist,tb_favor ", null, null, null, null, null, null);
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<ContentValues> getHistory_list() {
        SQLiteDatabase db = null;
        List<ContentValues> list = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String sql = "select * from tb_history";
            Cursor cursor = db.rawQuery(sql, null);
            list = getCV(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    @Override
    public List<Map<String, String>> getMap(Cursor cursor) {
        List<Map<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int lenth = cursor.getColumnCount();
            Map<String, String> map = null;
            for (int i = 0; i < lenth; i++) {
                map = new HashMap<String, String>();
                String key = cursor.getColumnName(i);
                String value = cursor.getString(cursor.getColumnIndex(key));
                if (value == null) {
                    value = "";
                }
                map.put(key, value);
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public List<ContentValues> getCV(Cursor cursor) {
        List<ContentValues> list = new ArrayList<>();
        int columnCount = cursor.getColumnCount();
        ContentValues cv = null;
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            for (int i = 0; i < columnCount; i++) {
                String key = cursor.getColumnName(i);
                String value = cursor.getString(cursor.getColumnIndex(key));
                if (value == null) {
                    value = "";
                }
                cv.put(key, value);
            }
            list.add(cv);
        }
        return list;
    }
}
