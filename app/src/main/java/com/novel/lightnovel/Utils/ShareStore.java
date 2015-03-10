package com.novel.lightnovel.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.novel.lightnovel.Domain.SinBook;
import com.novel.lightnovel.Domain.Vollist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Crash on 2015/1/13.
 */
public class ShareStore {

    private Context context;

    private SharedPreferences volPreferences;
    private SharedPreferences bookPreferences;
    private SharedPreferences coverPreferences;


    public ShareStore(Context context) {
        this.context = context;
        volPreferences = context.getSharedPreferences("vollist", Context.MODE_PRIVATE);
        bookPreferences = context.getSharedPreferences("books", Context.MODE_PRIVATE);
        coverPreferences = context.getSharedPreferences("cover", Context.MODE_PRIVATE);
    }

    public boolean saveVolllsit(Vollist vol) {
        List<SinBook> bookList = vol.getBookList();

        volPreferences.edit().putString(String.valueOf(vol.getId()), vol.toString());

        for (int i = 0; i < bookList.size(); i++) {

            int bookId = bookList.get(i).getId();
            bookPreferences.edit().putString(String.valueOf(bookId) + "info", bookList.get(i).infoTostring());
            //单章信息：（标题，链接）保存到book下面的对应id------------------->链接以@@分割，名字以##分割，id以"，"分割
            bookPreferences.edit().putString(String.valueOf(bookId) + "title", bookList.get(i).titlesToString());
            bookPreferences.edit().putString(String.valueOf(bookId) + "viewId", bookList.get(i).viewsIdToString());
        }
        //vol信息：（标题，id，封面）保存在vollsit下面对应id
        volPreferences.edit().putString(vol.getId() + "bookIds", vol.bookIdToString());            //卷id集合：用于取数据
        coverPreferences.edit().putString(String.valueOf(vol.getId()), vol.coverToString());    //卷封面

        return volPreferences.edit().commit() && bookPreferences.edit().commit() && coverPreferences.edit().commit();
    }

    public Vollist readVollist(int id) {
        Vollist vollist = new Vollist();

        //读取info
        vollist.setId(id);
        String info = volPreferences.getString(String.valueOf(id), "");
        if (!"".equals(info)) {
            String[] i = info.split("##");
            //name+"##"+author+"##"+illustration+"##"+library+"##"+count+"##"+updatetime+"##"+lastBook+"##"+lastBookLink+"##"+about;
            vollist.setName(i[0]);
            vollist.setAuthor(i[1]);
            vollist.setIllustration(i[2]);
            vollist.setLibrary(i[3]);
            vollist.setCount(Integer.parseInt(i[4]));
            vollist.setUpdatetime(i[5]);
            vollist.setLastBook(i[6]);
            vollist.setLastBookLink(i[7]);
            vollist.setAbout(i[8]);
        } else {
            return null;
        }
        //读取books
        String[] bookIds = volPreferences.getString("bookIds", "").split(",");
        List<SinBook> bookList = new ArrayList<>();
        SinBook book = null;
        for (String bookId : bookIds) {
            String[] i = bookPreferences.getString(bookId + "info", "").split("##");
            book = new SinBook(Integer.parseInt(i[0]),i[1]);
            String[] titles = bookPreferences.getString(bookId + "title", "").split("##");
            book.setTitles(Arrays.asList(titles));
            String[] viewIds = bookPreferences.getString(bookId + "viewId", "").split(",");
            book.setViewIDs(Arrays.asList(viewIds));
            bookList.add(book);
        }
        vollist.setBookList(bookList);
        //读取封面
        String[] covers = coverPreferences.getString(String.valueOf(id), "").split("@@");
        vollist.setCoverList(Arrays.asList(covers));
        return vollist;
    }

    public boolean isFavor(int id) {
        boolean flag = false;
        SharedPreferences infoPreferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
        String[] favor = infoPreferences.getString("favor", "162").split(",");
        for (String str : favor) {
            if (String.valueOf(id).equals(str)) flag = true;
        }
        return flag;
    }

    public boolean addFavor(int id) {
        StringBuffer favor = new StringBuffer(id);
        SharedPreferences infoPreferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
        String old = infoPreferences.getString("favor", "162");
        favor.append(old);
        return infoPreferences.edit().putString("favor", favor.toString()).commit();
    }

    public boolean removeFavor(int id) {
        StringBuffer favor = new StringBuffer();
        SharedPreferences infoPreferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
        String[] old = infoPreferences.getString("favor", "162").split(",");
        for (String str : old) {
            if (!String.valueOf(id).equals(str)) favor.append(str).append(",");
        }
        return infoPreferences.edit().putString("favor", favor.toString()).commit();
    }

}
