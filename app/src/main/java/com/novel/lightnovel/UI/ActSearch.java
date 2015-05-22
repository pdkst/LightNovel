package com.novel.lightnovel.UI;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.novel.lightnovel.R;
import com.novel.lightnovel.Thread.DocumentRunable;
import com.novel.lightnovel.Thread.VisRunable;
import com.novel.lightnovel.Utils.FileFactory;
import com.novel.lightnovel.Utils.HtmlParser;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActSearch extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String TAG = "ActSearch";
    private FileFactory f;

    private LinearLayout ll_inc_loading, ll_inc_error;
    private GridView gv_search_recom;
    private Button btn_inc_reload;

    private Context context;
    private EditText et_search_bar;
    private ExecutorService pool;
    private VisRunable vis;
    private String docStr;

    private String[] title = new String[]{"最新新番", "即将动画化", "热门阅读", "最新更新", "排行榜", "推荐", "收藏"};
    private int[] cover = new int[]{R.drawable.cover1185s, R.drawable.cover1185s, R.drawable.cover1185s, R.drawable.cover1185s, R.drawable.cover1185s, R.drawable.cover1185s, R.drawable.cover1185s,};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DocumentRunable.READ_OK) {
                Document document = (Document) msg.obj;
                f.saveIndex(document);
                Log.d(TAG,"ActSearch-->");
                docStr = document.toString();
                vis.vis();
            }
        }
    };

    public ActSearch() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_tab_search, container, false);

        context = view.getContext();
        ll_inc_loading = (LinearLayout) view.findViewById(R.id.ll_inc_loading);
        ll_inc_error = (LinearLayout) view.findViewById(R.id.ll_inc_error);
        btn_inc_reload = (Button) view.findViewById(R.id.btn_inc_reload);


        gv_search_recom = (GridView) view.findViewById(R.id.gv_search_recom);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), getGirdDatas(), R.layout.item_grid_search, new String[]{"iv_item_image", "tv_item_title"}, new int[]{R.id.iv_item_image, R.id.tv_item_title});
        gv_search_recom.setAdapter(simpleAdapter);
        gv_search_recom.setOnItemClickListener(this);
        f = FileFactory.newInstence(context);
        if (!f.isIndexExists()) updata();

        ImageButton iv_search_bar = (ImageButton) view.findViewById(R.id.ib_search_bar);
        et_search_bar = (EditText) view.findViewById(R.id.et_search_bar);

        vis = new VisRunable(ll_inc_loading, ll_inc_error, gv_search_recom);

        iv_search_bar.setOnClickListener(this);
        btn_inc_reload.setOnClickListener(this);

        return view;
    }

    private void updata() {
        if (pool != null) pool.shutdownNow();
        pool = Executors.newSingleThreadExecutor();
        pool.submit(new DocumentRunable(handler));
        handler.postDelayed(new VisRunable(ll_inc_loading, ll_inc_error, gv_search_recom), 5000);
    }

    private List<Map<String, Object>> getGirdDatas() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("iv_item_image", cover[i]);
            map.put("tv_item_title", title[i]);
            list.add(map);
        }
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(view.getContext(), "点击了" + position + "", Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                Intent i0 = getIn();
                i0.putExtra("title", ActVolList.ANIME);
                startActivity(i0);
                break;
            case 1:
                Intent i1 = getIn();
                i1.putExtra("title", ActVolList.ANIME_NEXT);
                startActivity(i1);
                break;
            case 2:
                Intent i2 = getIn();
                i2.putExtra("title", ActVolList.HOT);
                startActivity(i2);
                break;
            case 3:
                Intent i3 = getIn();
                i3.putExtra("title", ActVolList.LAST);
                startActivity(i3);
                break;
            case 4:
                Intent i4 = getIn();
                i4.putExtra("title", ActVolList.LV_LIST);
                startActivity(i4);
                break;
            case 5:
                Intent i5 = getIn();
                i5.putExtra("title", ActVolList.RECOM);
                startActivity(i5);
                break;
            case 6:
                Intent i6 = getIn();
                i6.putExtra("title", ActVolList.FAVOR);
                startActivity(i6);
                break;
        }
    }

    private Intent getIn() {
        Intent intent = new Intent(context, ActVolList.class);
        intent.putExtra("document", docStr);
        return intent;
    }

    public void onSearch(String key) {
        try {
            String url = HtmlParser.getSearchPath(key);
            if (!TextUtils.isEmpty(url)) {
                Intent i7 = getIn();
                i7.putExtra("url", url);
                i7.putExtra("title", ActVolList.SEARCH);
                startActivity(i7);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(context, "字符串转换失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search_bar:
                String str = et_search_bar.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(context, "输入的字符不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else onSearch(str);
                break;
            case R.id.btn_inc_reload:
                updata();
                break;
        }
    }
}
