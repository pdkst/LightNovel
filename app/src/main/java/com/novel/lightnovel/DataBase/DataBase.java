package com.novel.lightnovel.DataBase;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;
import java.util.Map;

/**
 * Created by Crash on 2015/2/7.
 */
public interface DataBase {
    boolean isFavor(int v_id);

    boolean addFavor(int v_id);

    //增删改查；
    boolean insertVollist(ContentValues vollist);

    boolean insertBook(ContentValues book);

    boolean insertView(ContentValues view);

    boolean insertIllustration(ContentValues view);

    boolean insertHistory(ContentValues history);

    boolean insertFavor(ContentValues favor);

    boolean deleteVollist(int v_id);

    boolean deleteBook(int b_id);

    boolean deleteView(int view_id);

    boolean deleteIllustration(int i_id);

    boolean deleteFavor(int f_id);

    boolean deleteHistory(int h_id);

    boolean updateVollist(ContentValues cv);

    boolean updateBook(ContentValues cv);

    boolean updateView(ContentValues cv);

    boolean updateIllustration(ContentValues cv);

    boolean updateFavor(ContentValues cv);

    boolean updateHistory(ContentValues cv);

    List<ContentValues> getVollist(int v_id);

    List<ContentValues> getBook(int b_id);

    List<ContentValues> getView(int view_id);

    List<ContentValues> getIllustration(int i_id);

    List<ContentValues> getFavor(int v_id);

    List<ContentValues> getHistory(int b_id);

    List<ContentValues> getHistorys(int v_id);

    List<ContentValues> getVollist_list();

    List<ContentValues> getBook_list(int v_id);

    List<ContentValues> getView_list(int b_id);

    List<ContentValues> getIll_list(int b_id);

    List<ContentValues> getFavor_list();

    List<ContentValues> getFavor_list2();

    List<ContentValues> getHistory_list();

    List<Map<String, String>> getMap(Cursor cursor);

    List<ContentValues> getCV(Cursor cursor);
}
