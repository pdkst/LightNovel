package com.novel.lightnovel.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.novel.lightnovel.DataBase.D;
import com.novel.lightnovel.DataBase.DataBase;
import com.novel.lightnovel.DataBase.DataBaseDao;
import com.novel.lightnovel.R;
import com.novel.lightnovel.Thread.DocumentRunable;
import com.novel.lightnovel.Thread.VisRunable;
import com.novel.lightnovel.Utils.FileFactory;
import com.novel.lightnovel.Utils.HtmlParser;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActRead extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "ActRead";
    public static final int CONNECT_ERROR = 0;
    public static final int READ_ERROR = 1;
    public static final int READ_OK = 2;

    private int v_id;
    private int b_id;
    private int view_id;
    private int which;

    private LinearLayout ll_read_loading, ll_read_error;
    private LinearLayout ll_inc_menu;
    private ScrollView sv_read_view;

    private TextView tv_title;
    private TextView tv_view;
    private Button btn_inc_reload;

    private DataBase dataBase;
    private FileFactory fileFactory;
    private ExecutorService pool = null;
    private List<ContentValues> views;

    private String path;
    private VisRunable vis;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            obtainMessage();
            if (msg.what == READ_OK) {
                Document document = (Document) msg.obj;
                if (document != null) {
                    setText(document);
                    List<ContentValues> ills = HtmlParser.getIllustration(v_id,b_id,document);
                    for (ContentValues ill:ills){
                        dataBase.insertIllustration(ill);       //获取插画并更新到数据库
                    }
                }
            }
        }
    };
    private ContentValues history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_read);

        tv_title = (TextView) findViewById(R.id.tv_read_title);
        tv_view = (TextView) findViewById(R.id.tv_read_view);

        btn_inc_reload = (Button) findViewById(R.id.btn_inc_reload);
        ll_inc_menu = (LinearLayout) findViewById(R.id.ll_inc_menu);

        ll_read_loading = (LinearLayout) findViewById(R.id.ll_inc_loading);
        ll_read_error = (LinearLayout) findViewById(R.id.ll_inc_error);
        sv_read_view = (ScrollView) findViewById(R.id.sv_read_view);
        vis = new VisRunable(ll_read_loading, ll_read_error, sv_read_view);

        sv_read_view.setOnTouchListener(new TouchListen());
        btn_inc_reload.setOnClickListener(this);
        tv_view.setOnClickListener(this);
        dataBase = new DataBaseDao(this);
        fileFactory = FileFactory.newInstence(this);

        Intent intent = getIntent();
        v_id = intent.getIntExtra("v_id", -1);
        b_id = intent.getIntExtra("b_id", -1);
        view_id = intent.getIntExtra("view_id", -1);
        which = intent.getIntExtra("which", -2);
        if (b_id != -1) views = dataBase.getView_list(b_id);
        switch (which) {
            case -2:
                List<ContentValues> favors = dataBase.getFavor(v_id);
                List<ContentValues> his = dataBase.getHistorys(v_id);

                if (!his.isEmpty()){
                    ContentValues history = his.get(0);
                    b_id = history.getAsInteger(D.b_id);
                    view_id = history.getAsInteger(D.view_id);
                }else if (!favors.isEmpty()) {
                    ContentValues favor = favors.get(0);
                    if (b_id == favor.getAsInteger("b_id")) {
                        view_id = favor.getAsInteger("view_id");
                    }else {
                        List<ContentValues> books = dataBase.getBook_list(v_id);
                        ContentValues book = books.get(0);
                        b_id = book.getAsInteger("b_id");
                        views = dataBase.getView_list(b_id);
                        view_id = views.get(0).getAsInteger("view_id");
                    }
                    favor.put(D.f_time,System.currentTimeMillis()); //更新favor时间
                    dataBase.insertFavor(favor);                    //更新数据库
                }else break;
                //不需要break，因为需要执行下面的步骤
            case -1:
                for (int i = 0; i < views.size(); i++) {
                    ContentValues cv = views.get(i);
                    if (view_id == cv.getAsInteger("view_id")) {
                        which = i;
                        break;
                    }
                }
            default:
                break;
        }

        getContent();

    }

    private void getContent() {
        Document document = fileFactory.readView(v_id, view_id);
        if (document != null) {
            setText(document);
        } else {
            //判断网络连接状态
            ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
            if (wifi || internet) {
                downView();  //执行从网络上下载view的操作
            } else {
                Toast.makeText(this, "未连接到网络", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 将获得的内容（document）设置到界面中并显示
     *
     * @param document 网页的document对象
     */
    private void setText(Document document) {
        Log.e(TAG, "---------->handle msg....");
        vis.vis();
        Log.e(TAG, "---------->set visable");
        tv_title.setText(document.title());
        tv_view.setText(HtmlParser.getText(document));
        List<ContentValues> his = dataBase.getHistory(b_id);
        if (!his.isEmpty()) {
            final int y = his.get(0).getAsInteger(D.pos);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sv_read_view.setScrollY(y);
                }
            }, 200);
            Toast.makeText(ActRead.this, "Scroll = " + y, Toast.LENGTH_SHORT).show();
        }else {
            addHistory(0);
        }
        Log.d(TAG, "---------->set text end");
    }
    /**
     * 下载view对应的网页，并显示加载页面，获取到的内容将在handleMessage里面处理
     *
     * @param which views的序号
     */
    private void downView(int which) {
        vis.load();
        if (ll_inc_menu.getVisibility() == View.VISIBLE) ll_inc_menu.setVisibility(View.GONE);
        view_id = views.get(which).getAsInteger(D.view_id);
        downView();
    }

    private void downView() {
        Log.e(TAG, "-----view id------->" + view_id);
        path = HtmlParser.getViewPath(view_id);
        if (pool != null) pool.shutdownNow();
        pool = Executors.newSingleThreadExecutor();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("setting_net_read",false)){
            pool.submit(new MdocRun(path,handler));
        }else pool.submit(new DocRun(path,handler));
        pool.shutdown();
        handler.postDelayed(new VisRunable(ll_read_loading, ll_read_error, sv_read_view), 7000);
    }


    private void viewlist() {
        int viewSize = views.size();
        if (ll_inc_menu.getVisibility() == View.VISIBLE) ll_inc_menu.setVisibility(View.GONE);

        String[] view_name = new String[viewSize];
        for (int j = 0; j < viewSize; j++) {
            view_name[j] = views.get(j).getAsString(D.view_name);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setItems(view_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //选择章节
                downView(which);
            }
        });
        alert.show();
    }

    private void pre() {
        if (which!=0){
        which--;
        downView(which);
        }else {
            Toast.makeText(this,"已经是第一话了，前面没有内容了哦。",Toast.LENGTH_SHORT).show();
        }
    }

    private void next() {
        if (which != views.size()-1) {
            which++;
            downView(which);
        }else {
            Toast.makeText(this,"章节末尾，已看完本卷",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_inc_reload:
                downView();
                break;
            //绑定菜单显示
            case R.id.ll_inc_menu:
            case R.id.tv_read_view:
                if (ll_inc_menu.getVisibility() == View.GONE || ll_inc_menu.getVisibility() == View.INVISIBLE) {
                    ll_inc_menu.setVisibility(View.VISIBLE);
                } else {
                    ll_inc_menu.setVisibility(View.INVISIBLE);
                }
                break;
            //上一章节
            case R.id.btn_menu_pre:
            case R.id.btn_footer_pre:
                pre();
                break;
            //viewlist
            case R.id.btn_menu_list:
            case R.id.btn_footer_list:
                viewlist();
                break;
            //下一章
            case R.id.btn_menu_next:
            case R.id.btn_footer_next:
                next();
                break;
        }
    }

    private class TouchListen implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                int y = sv_read_view.getScrollY();
                List<ContentValues> his = dataBase.getHistory(b_id);
                if (!his.isEmpty()) {
                    history = his.get(0);
                    history.put(D.pos, y);
                    history.put(D.h_time, System.currentTimeMillis());
                    history.put(D.view_id, view_id);
                    dataBase.updateHistory(history);
                } else {
                    addHistory(y);
                }

            }
            return false;
        }
    }

    private void addHistory(int y) {
        history = new ContentValues();
        history.put(D.h_id, b_id);
        history.put(D.h_time, System.currentTimeMillis());
        history.put(D.v_id, v_id);
        history.put(D.b_id, b_id);
        history.put(D.view_id, view_id);
        history.put(D.pos, y);
        dataBase.insertHistory(history);
    }

    private class MdocRun implements Runnable{
        private String url;
        private Handler handler;

        private MdocRun(String url, Handler handler) {
            this.url = url;
            this.handler = handler;
        }

        @Override
        public void run() {
            //执行更新任务的子线程
            Message msg = handler.obtainMessage(READ_ERROR);
            try {
                Document document = HtmlParser.mDoc(url);
                if (HtmlParser.volTest(document)) {
                    Log.e(TAG, "-----MdocRun----vTest-----> = true;");
                    msg = handler.obtainMessage(READ_OK,document);
                    handler.sendMessage(msg);
                    fileFactory.saveView(v_id,view_id,document);
                }
            } catch (IOException e) {
                e.printStackTrace();
                msg.what = CONNECT_ERROR;
                Log.e(TAG,"-----MdocRun----->connect_error--------->url="+url);
            }
            boolean b = handler.sendMessage(msg);
            Log.e(TAG, "-----MdocRun----->send="+ b + ",msg.what=" + msg.what);
        }
    }
    private class DocRun implements Runnable{
        private String url;
        private Handler handler;

        private DocRun(String url, Handler handler) {
            this.url = url;
            this.handler = handler;
        }

        @Override
        public void run() {
            //执行更新任务的子线程
            Message msg = handler.obtainMessage(READ_ERROR);
            try {
                Document document = HtmlParser.doc(url);
                if (HtmlParser.volTest(document)) {
                    Log.e(TAG, "-----docRun---->vTest=true;");
                    msg = handler.obtainMessage(READ_OK,document);
                    handler.sendMessage(msg);
                    fileFactory.saveView(v_id,view_id,document);
                }
            } catch (IOException e) {
                e.printStackTrace();
                msg.what = CONNECT_ERROR;
                Log.e(TAG,"-----docRun----->connect_error-->url="+url);
            }
            boolean b = handler.sendMessage(msg);
            Log.e(TAG, "-----docRun----->send="+ b + ",msg.what=" + msg.what);
        }
    }
}