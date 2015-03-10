package com.novel.lightnovel.UI;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.novel.lightnovel.DataBase.D;
import com.novel.lightnovel.R;
import com.novel.lightnovel.Utils.DrawableDownloader;

import java.util.List;

/**
 * Created by Crash on 2015/1/2.
 */
public class VollistAdapter extends BaseAdapter{
    private static final String TAG = "VollistAdapter";

    private Context context = null;
    private LayoutInflater inflater = null;
    private List<ContentValues> list = null;
    private DrawableDownloader downloader = null;

    public VollistAdapter(Context context, List<ContentValues> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_search, null);
            holder = new Holder();
            holder.ivCover = (ImageView) convertView.findViewById(R.id.iv_lv_cover);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvNewBook = (TextView) convertView.findViewById(R.id.tvNewBook);
            holder.tvNewChapter = (TextView) convertView.findViewById(R.id.tvNewChapter);
            holder.rl_vol_list = (RelativeLayout) convertView.findViewById(R.id.ll_vol_list);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ContentValues sea = list.get(position);
        String url = sea.getAsString(D.v_cover);
//        if (!TextUtils.isEmpty(url))downloader.loadDrawable(url, holder.ivCover); //加载图片
        String name = sea.getAsString(D.v_name);
        if (!TextUtils.isEmpty(name))holder.tvName.setText(name);
        String newbook = sea.getAsString("last_title");
        if (!TextUtils.isEmpty(newbook))holder.tvNewBook.setText(newbook);
//        holder.tvNewChapter.setText(sea.getAsString("last_title"));
        holder.rl_vol_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetail(position);
            }
        });
        return convertView;
    }

    private void toDetail(int position) {
        //这里可以放入部分数据
        ContentValues contentValues = list.get(position);
        Log.e(TAG,"---------VollistAdapter--cv-------->"+contentValues.toString());
        int v_id = contentValues.getAsInteger(D.v_id);
        if (v_id==-1)return;
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

    public final class Holder {
        public ImageView ivCover;
        public TextView tvName;
        public TextView tvNewBook;
        public TextView tvNewChapter;
        public RelativeLayout rl_vol_list;
    }
}
