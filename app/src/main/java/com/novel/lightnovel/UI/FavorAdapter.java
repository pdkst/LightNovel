package com.novel.lightnovel.UI;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.novel.lightnovel.DataBase.D;
import com.novel.lightnovel.DataBase.DataBase;
import com.novel.lightnovel.DataBase.DataBaseDao;
import com.novel.lightnovel.R;
import com.novel.lightnovel.Utils.DrawableDownloader;

import java.util.List;

/**
 * Created by Crash on 2015/1/2.
 */
public class FavorAdapter extends BaseAdapter {
    final static String TAG = "FavorAdapter";


    private Context context = null;
    private LayoutInflater inflater = null;
    private List<ContentValues> list = null;
    private DrawableDownloader downloader = null;
    private DataBase dataBaseDao;

    public FavorAdapter(Context context, List<ContentValues> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        downloader = new DrawableDownloader();
        dataBaseDao = new DataBaseDao(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_favor, null);
            holder = new Holder();
            holder.rl_lv_favor = (RelativeLayout) convertView.findViewById(R.id.rl_lv_favor);
            holder.ivCover = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvHistory = (TextView) convertView.findViewById(R.id.tvNewBook);
            holder.tvNewChapter = (TextView) convertView.findViewById(R.id.tvNewChapter);
            holder.ibtnBookManage = (ImageButton) convertView.findViewById(R.id.ibtnBookManage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ContentValues vol = list.get(position);
        String cover = vol.getAsString(D.v_cover);
//        if (cover.length()>30)downloader.loadDrawable(cover,holder.ivCover);   //加载图片
        holder.tvName.setText(vol.getAsString(D.v_name));
        List<ContentValues> his = dataBaseDao.getHistorys(vol.getAsInteger(D.v_id));
        if (!his.isEmpty()) {
            ContentValues history = his.get(0);
            int b_id = history.getAsInteger(D.b_id);
            int view_id = history.getAsInteger(D.view_id);
            ContentValues book = dataBaseDao.getBook(b_id).get(0);
            ContentValues view = dataBaseDao.getView(view_id).get(0);

            holder.tvHistory.setText("看到:"+book.getAsString(D.b_name)+"  "+view.getAsString(D.view_name));
        }
        holder.tvNewChapter.setText("更新:"+vol.getAsString(D.last_title));

        holder.rl_lv_favor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                menuDialog(vol, position);
                return false;
            }
        });
        holder.rl_lv_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetail(position);
            }
        });
        holder.ibtnBookManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog(vol, position);
            }
        });
        return convertView;
    }

    private void menuDialog(ContentValues favor, final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(favor.getAsString(D.v_name));
        alert.setItems(new String[]{"详情目录", "下载管理", "删除"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        toDetail(position);
                        break;
                    case 1:
                        Toast.makeText(context, "不可用", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        ContentValues vol = list.get(position);
                        int f_id = vol.getAsInteger(D.f_id);
                        dataBaseDao.deleteFavor(f_id);
                        list.remove(position);
                        notifyDataSetChanged();
                        break;
                }
            }
        });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alert.show();
        Toast.makeText(context, "长按了第" + (position + 1) + "个", Toast.LENGTH_SHORT).show();
    }

    private void toDetail(int position) {
        //这里可以放入部分数据
        ContentValues contentValues = list.get(position);
        int v_id = contentValues.getAsInteger(D.v_id);
        Intent intent = new Intent(context, ActVolDetail.class);
        intent.putExtra(D.v_id, v_id);

        context.startActivity(intent);
    }

    public List<ContentValues> getList() {
        return list;
    }

    public void setList(List<ContentValues> list) {
        this.list = list;
    }

    public static final class Holder {
        public RelativeLayout rl_lv_favor;
        public ImageView ivCover;
        public TextView tvName;
        public TextView tvHistory;
        public TextView tvNewChapter;
        public ImageButton ibtnBookManage;
    }
}
