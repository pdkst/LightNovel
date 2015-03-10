package com.novel.lightnovel.UI;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.novel.lightnovel.R;
import com.novel.lightnovel.Thread.DocumentRunable;
import com.novel.lightnovel.Thread.VisRunable;
import com.novel.lightnovel.Utils.FileFactory;
import com.novel.lightnovel.Utils.HtmlParser;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActVolList extends ActionBarActivity implements View.OnClickListener{
    public static final String SEARCH = "搜索";   //
    public static final String ANIME = "最新新番";
    public static final String ANIME_NEXT = "即将动画";
    public static final String HOT = "热门阅读";
    public static final String LAST = "最近更新";
    public static final String LV_LIST = "排行榜";
    public static final String RECOM = "随机推荐";
    public static final String FAVOR = "收藏榜";

    private static final String TAG = "ActVollistList";

    private ImageView iv_vollsit_back;
    private TextView tv_vollist_title;

    private ListView lv_vollist;
    private LinearLayout ll_inc_loading, ll_inc_error;

    private Button btn_inc_reload;

    private String title = "";
    private Document document = null;
    private VollistAdapter adapter;
    private ExecutorService pool = null;
    private VisRunable vis;
    private FileFactory f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vol_list);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        if (title == null) title = "-1";

        iv_vollsit_back = (ImageView) findViewById(R.id.iv_vollsit_back);
        tv_vollist_title = (TextView) findViewById(R.id.tv_vollist_title);

        lv_vollist = (ListView) findViewById(R.id.lv_vol_list);
        ll_inc_loading = (LinearLayout) findViewById(R.id.ll_inc_loading);
        ll_inc_error = (LinearLayout) findViewById(R.id.ll_inc_error);

        btn_inc_reload = (Button) findViewById(R.id.btn_inc_reload);

        tv_vollist_title.setText(title);
        iv_vollsit_back.setOnClickListener(this);
        btn_inc_reload.setOnClickListener(this);

        f = FileFactory.newInstence(this);
        vis = new VisRunable(ll_inc_loading, ll_inc_error, lv_vollist);

        setListview();

    }

    private void setListview() {
        if (SEARCH.equals(title)) search();
        else {
            List<ContentValues> list = getData();
            if (list!=null)adapter = new VollistAdapter(this, getData());
            lv_vollist.setAdapter(adapter);
            vis.vis();
        }
    }

    private List<ContentValues> getData() {
        List<ContentValues> list = new ArrayList<>();
        document = HtmlParser.parse(getIntent().getStringExtra("document"));

        ContentValues cv = new ContentValues();
        cv.put("v_id", "-1");
        cv.put("v_name", "加载失败……");
        cv.put("last_title", "可能哪里出了问题，鱼正在寻找解决的办法……");
        list.add(cv);
        if (document == null)document = f.readIndex();
        if (document == null)return list;
        else{
            switch (title) {
                case ANIME:
                    return HtmlParser.getIndex_last_anime(document);
                case ANIME_NEXT:
                    return HtmlParser.getIndex_furture_anime(document);
                case HOT:
                    return HtmlParser.getIndex_hot(document);
                case LAST:
                    return HtmlParser.getIndex_last_vol(document);
                case LV_LIST:
                    return HtmlParser.getIndex_list(document);
                case RECOM:
                    return HtmlParser.getIndex_radom(document);
                case FAVOR:
                    return HtmlParser.getIndex_favoList(document);
                default:return list;
            }
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DocumentRunable.READ_OK) {
                Document search = (Document) msg.obj;
                if (HtmlParser.volTest(search)) {
                    List<ContentValues> list = HtmlParser.getListFromSearch(search);
                    if (list.isEmpty()){
                        ContentValues cv = new ContentValues();
                        cv.put("v_id", "-1");
                        cv.put("v_name", "没有搜索到结果……");
                        cv.put("last_title", "可能没有收录该书,换一个关键字试试……");
                        list.add(cv);
                    }
                    adapter = new VollistAdapter(ActVolList.this, list);
                    lv_vollist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    vis.vis();
                }
            }
        }
    };

    /**
     *执行搜索(待完成
     */
    public void search() {
        vis.load();
        String url = getIntent().getStringExtra("url");
        if (pool != null) pool.shutdownNow();
        pool = Executors.newSingleThreadExecutor();
        pool.submit(new DocumentRunable(handler, url));
        handler.postDelayed(new VisRunable(ll_inc_loading, ll_inc_error, lv_vollist), 7000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_vollsit_back:
                finish();
                break;
            case R.id.btn_inc_reload:
                search();
                break;
        }
    }
}
