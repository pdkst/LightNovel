package com.novel.lightnovel.UI;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.novel.lightnovel.DataBase.D;
import com.novel.lightnovel.R;
import com.novel.lightnovel.Thread.DocumentRunable;
import com.novel.lightnovel.Thread.VisRunable;
import com.novel.lightnovel.Utils.FileFactory;
import com.novel.lightnovel.Utils.HtmlParser;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActRecom extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = "ActRecom";
    private Context context;

    private LinearLayout ll_inc_loading, ll_inc_error;
    private Button btn_inc_reload;
    private ListView lv_recom;


    private RadioGroup rg_bar_recom;

    private VisRunable vis;
    private Document document;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DocumentRunable.READ_OK) {
                document = (Document) msg.obj;
                List<ContentValues> list = HtmlParser.getIndex_radom(document);
                adapter = new ListRecomAdapter(context, list);
                lv_recom.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                vis.vis();
            }
        }
    };
    private ExecutorService pool;
    private ListRecomAdapter adapter;

    public ActRecom() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_tab_recom, container, false);
        context = view.getContext();

        ll_inc_loading = (LinearLayout) view.findViewById(R.id.ll_inc_loading);
        ll_inc_error = (LinearLayout) view.findViewById(R.id.ll_inc_error);
        lv_recom = (ListView) view.findViewById(R.id.lv_recom);
        btn_inc_reload = (Button) view.findViewById(R.id.btn_inc_reload);
        btn_inc_reload.setOnClickListener(this);

        rg_bar_recom = (RadioGroup) view.findViewById(R.id.rg_bar_recom);
        rg_bar_recom.setOnCheckedChangeListener(this);

        vis = new VisRunable(ll_inc_loading, ll_inc_error, lv_recom);
        FileFactory factory = FileFactory.newInstence(context);
        if (factory.isIndexExists())document = factory.readIndex();
        update();

        return view;
    }

    private void update() {
        vis.load();
        if (pool != null)pool.shutdownNow();
        pool = Executors.newSingleThreadExecutor();
        pool.submit(new DocumentRunable(handler));
        handler.postDelayed(new VisRunable(ll_inc_loading, ll_inc_error, lv_recom), 7000);
    }


    @Override
    public void onClick(View v) {
        update();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        List<ContentValues> list = null;
        switch (checkedId){
            case R.id.rb_recom_daily:
                list = HtmlParser.getIndex_radom(document);
                Log.d(TAG,"------RadioButton-------->每日推荐");
                break;
            case R.id.rb_recom_hot:
                list = HtmlParser.getIndex_hot(document);
                Log.d(TAG,"------RadioButton-------->热门阅读");
                break;
            case R.id.rb_recom_anime:
                list = HtmlParser.getIndex_furture_anime(document);
                Log.d(TAG,"------RadioButton-------->即将动画");
                break;
            case R.id.rb_recom_favor:
                list = HtmlParser.getIndex_favoList(document);
                Log.d(TAG,"------RadioButton-------->收藏榜");
                break;
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    private class ListRecomAdapter extends BaseAdapter {
        Context context;
        List<ContentValues> list;
        LayoutInflater inflater;
        private Hold hold;

        public void setList(List<ContentValues> list) {
            this.list = list;
        }

        private ListRecomAdapter(Context context, List<ContentValues> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_list_recom_simple, null);
                hold = new Hold();
                hold.tvPosition = (TextView) convertView.findViewById(R.id.tv_item_position);
                hold.tvTittle = (TextView) convertView.findViewById(R.id.tv_item_title);
                hold.ll_item_recom = (LinearLayout) convertView.findViewById(R.id.ll_item_recom);
                convertView.setTag(hold);
            } else {
                hold = (Hold) convertView.getTag();
            }
            final ContentValues c = list.get(position);
            hold.tvPosition.setText(position+1+":");
            hold.tvTittle.setText(c.getAsString(D.v_name));
            hold.ll_item_recom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ActVolDetail.class);
                    intent.putExtra(D.v_id,c.getAsInteger(D.v_id));
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    public class Hold {
        public TextView tvPosition;
        public TextView tvTittle;
        public LinearLayout ll_item_recom;
    }
}
