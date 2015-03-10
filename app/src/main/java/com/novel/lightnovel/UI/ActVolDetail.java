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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.novel.lightnovel.DataBase.D;
import com.novel.lightnovel.DataBase.DataBase;
import com.novel.lightnovel.DataBase.DataBaseDao;
import com.novel.lightnovel.R;
import com.novel.lightnovel.Thread.DocumentRunable;
import com.novel.lightnovel.Thread.VisRunable;
import com.novel.lightnovel.Utils.DrawableDownloader;
import com.novel.lightnovel.Utils.FileFactory;
import com.novel.lightnovel.Utils.HtmlParser;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ActVolDetail extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "ActVolDetail";

    private LinearLayout ll_inc_loading, ll_inc_error;
    private ScrollView sv_detail_view;

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_download;

    private ImageView iv_cover;
    private TextView tv_Name;
    private TextView tv_author, tv_illustration, tv_library, tv_count, tv_updatetime, tv_about;
    private RadioButton rb_detail_favor_vol,
            rb_detail_update_vol,
            rb_detail_star_vol,
            rb_detail_read_vol;
    private Button btn_inc_reload;

    private LinearLayout ll_book_list;

    private int v_id;
    private DataBase dataBaseDao;
    private ExecutorService pool;
    private DrawableDownloader downloader = new DrawableDownloader(1);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DocumentRunable.READ_OK) {
                Document document = (Document) msg.obj;

                FileFactory f = FileFactory.newInstence(ActVolDetail.this);
//                f.saveVol(v_id, document);

                Map<String, List<ContentValues>> all = HtmlParser.getAll(v_id, document);
                if (!all.isEmpty()) {
                    ContentValues vol = HtmlParser.getVollist(v_id, document);
                    setInfo(vol);
                    ll_inc_loading.setVisibility(View.GONE);
                    sv_detail_view.setVisibility(View.VISIBLE);
                    dataBaseDao.insertVollist(vol);
                    List<ContentValues> books = all.get("book");
                    for (ContentValues book : books) {
                        dataBaseDao.insertBook(book);
                    }
                    List<ContentValues> views = all.get("view");
                    for (ContentValues view : views) {
                        dataBaseDao.insertView(view);
                    }
                    addBookview(books);

                }


            } else if (msg.what == DocumentRunable.CONNECT_ERROR) {
                //连接网络失败
                Toast.makeText(ActVolDetail.this, "连接网络失败", Toast.LENGTH_SHORT).show();
            } else {
                //读取失败
                Toast.makeText(ActVolDetail.this, "获取信息失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private VisRunable vis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_vol);
        init(); //初始化对象等
        readInfo();
        addBookview(getBookData());
    }
    private void init() {
        //数据的初始化以及绑定监听事件
        Intent intent = getIntent();
        v_id = intent.getIntExtra(D.v_id, -1);
        Log.e(TAG,"-----actDtail----i->"+v_id);

        dataBaseDao = new DataBaseDao(this);

        ll_inc_loading = (LinearLayout) findViewById(R.id.ll_inc_loading);
        ll_inc_error = (LinearLayout) findViewById(R.id.ll_inc_error);
        sv_detail_view = (ScrollView) findViewById(R.id.sv_detail_view);

        iv_back = (ImageView) findViewById(R.id.iv_detail_back);
        tv_title = (TextView) findViewById(R.id.tv_detail_title);
        tv_download = (TextView) findViewById(R.id.tv_detail_download);

        tv_Name = (TextView) findViewById(R.id.tv_detail_name);
        iv_cover = (ImageView) findViewById(R.id.iv_detail_cover);
        tv_author = (TextView) findViewById(R.id.d_tv_author);
        tv_illustration = (TextView) findViewById(R.id.d_tv_illustration);
        tv_library = (TextView) findViewById(R.id.d_tv_library);
        tv_count = (TextView) findViewById(R.id.d_tv_count);
        tv_updatetime = (TextView) findViewById(R.id.d_tv_updatetime);
        tv_about = (TextView) findViewById(R.id.d_tv_about);

        rb_detail_favor_vol = (RadioButton) findViewById(R.id.rb_detail_favor_vol);
        rb_detail_update_vol = (RadioButton) findViewById(R.id.rb_detail_update_vol);
        rb_detail_star_vol = (RadioButton) findViewById(R.id.rb_detail_star_vol);
        rb_detail_read_vol = (RadioButton) findViewById(R.id.rb_detail_read_vol);

        btn_inc_reload = (Button) findViewById(R.id.btn_inc_reload);

        ll_book_list = (LinearLayout) findViewById(R.id.ll_books_list);
        vis = new VisRunable(ll_inc_loading, ll_inc_error, sv_detail_view);

        if (dataBaseDao.isFavor(v_id)) {
            rb_detail_favor_vol.setChecked(true);
            rb_detail_favor_vol.setText("已收藏");
        }

        iv_back.setOnClickListener(this);
        tv_download.setOnClickListener(this);
        rb_detail_favor_vol.setOnClickListener(this);
        rb_detail_update_vol.setOnClickListener(this);
        rb_detail_star_vol.setOnClickListener(this);
        rb_detail_read_vol.setOnClickListener(this);
        btn_inc_reload.setOnClickListener(this);
    }
    private void addFavor() {
        // 收藏按钮，如果已收藏则取消收藏
        if (dataBaseDao.isFavor(v_id)) {
            dataBaseDao.deleteFavor(v_id);
            rb_detail_favor_vol.setText("收藏");
            rb_detail_favor_vol.setChecked(false);
        } else {
            dataBaseDao.addFavor(v_id);
            rb_detail_favor_vol.setText("已收藏");
        }
    }
    private void readInfo() {
        List<ContentValues> list = dataBaseDao.getVollist(v_id);
        if (list.size() == 0) {
            //如果没有获取到vollist的信息，则检查内存卡上是否存在相关信息
            FileFactory fileFactory = FileFactory.newInstence(this);

            if (fileFactory.isVolExists(v_id)) {
                Document document = fileFactory.readVol(v_id);
                Map<String, List<ContentValues>> all = HtmlParser.getAll(v_id, document);
                ContentValues vol = all.get("vol").get(0);
                setInfo(vol);       //更改ui--->detail详细信息
                addBookview(all.get("book"));

            } else {
                //判断网络连接状态
                ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
                boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                if (wifi || internet) {
                    update(); //执行更新操作
                } else {
                    Toast.makeText(getApplicationContext(), "未连接到网络", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            //获取到sqlite里面储存的vollist信息就显示出来
            setInfo(list.get(0));
        }
    }
    private List<ContentValues> getBookData() {
        return dataBaseDao.getBook_list(v_id);
    }

    /**
     * 生成book视图
     *
     * @param books books列表
     */
    private void addBookview(List<ContentValues> books) {
        ll_book_list.removeAllViews();

        //生成book视图的button控件，并绑定监听事件
        for (int i = 0; i < books.size(); i++) {
            final ContentValues finalBook = books.get(i);
            final List<ContentValues> views = dataBaseDao.getView_list(finalBook.getAsInteger(D.b_id));

            Button btnBook = new Button(this);
            btnBook.setText(finalBook.getAsString(D.b_name));
            btnBook.setBackgroundResource(R.drawable.bg_noshape);
            btnBook.setTag(i);      //内部类数据传递

            btnBook.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //长按book按钮，监听事件
                    int viewSize = views.size();

                    String[] view_name = new String[viewSize];
                    for (int j = 0; j < viewSize; j++) {
                        view_name[j] = views.get(j).getAsString(D.view_name);
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(ActVolDetail.this);
                    alert.setTitle(finalBook.getAsString(D.b_name));
                    alert.setItems(view_name, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //选择章节
                            startRead(which, finalBook, views);
                        }
                    });
                    alert.show();
                    return false;
                }
            });
            btnBook.setOnClickListener(new View.OnClickListener() {
                //点击book按钮，监听事件
                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    startRead(-1, finalBook, views);       //views是根据b_id搜索出的view集合，因此没有选择章节用-1表示
                    Toast.makeText(ActVolDetail.this, "" + i, Toast.LENGTH_SHORT).show();
                }
            });
            ll_book_list.addView(btnBook);
        }
    }

    /**
     * 跳转到ActRead,activity,放入需要的数据
     *
     * @param which views中章节位置
     * @param book  book信息
     * @param views 选定book的views章节列表
     */
    private void startRead(int which, ContentValues book, List<ContentValues> views) {
        Intent intent = new Intent(this, ActRead.class);
        intent.putExtra("which",which);
        intent.putExtra(D.v_id, v_id);
        int b_id = book.getAsInteger(D.b_id);
        intent.putExtra(D.b_id, b_id);

        if (which == -1) {
            List<ContentValues> history = dataBaseDao.getHistory(b_id);
            if (history.size() == 0) {
                intent.putExtra(D.view_id, views.get(0).getAsInteger(D.view_id));       //直接点击，但是没有历史记录
            } else {
                intent.putExtra(D.view_id, history.get(0).getAsInteger(D.view_id));         //直接点击，有历史记录
            }
        } else {
            intent.putExtra(D.view_id, views.get(which).getAsInteger(D.view_id));           //点击具体章节
        }
        Log.w(TAG, "--intent-->" + intent.toString());
        startActivity(intent);
    }

    private void readBook() {
        Intent intent = new Intent(this, ActRead.class);
        intent.putExtra(D.v_id, v_id);

        List<ContentValues> favors = dataBaseDao.getFavor(v_id);
        List<ContentValues> his = dataBaseDao.getHistorys(v_id);

        if (!his.isEmpty()){
            ContentValues cv = his.get(0);
            intent.putExtra(D.b_id,cv.getAsInteger(D.b_id));
            intent.putExtra(D.view_id,cv.getAsInteger(D.view_id));
        }else if (!favors.isEmpty()) {
            ContentValues cv = favors.get(0);

            intent.putExtra("isFavor", true);
            intent.putExtra(D.b_id, cv.getAsInteger("b_id"));
            intent.putExtra(D.view_id, cv.getAsInteger("view_id"));
        }
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_detail_back:
                finish();
                break;
            case R.id.tv_detail_download:
                Intent intent = new Intent(this,ActDownload.class);
                intent.putExtra(D.v_id,v_id);
                startActivity(intent);
                break;
            case R.id.rb_detail_favor_vol:      //收藏
                addFavor();
                break;
            case R.id.btn_inc_reload:
            case R.id.rb_detail_update_vol:     //刷新
                update();
                rb_detail_update_vol.setChecked(false);
                break;
            case R.id.rb_detail_star_vol:       //评分
                rb_detail_star_vol.setChecked(false);
                Toast.makeText(this, "不可用", Toast.LENGTH_LONG).show();
                break;
            case R.id.rb_detail_read_vol:       //阅读
                readBook();
                break;

        }
    }

    //主线程无法连接网络，只能放进子线程里面执行
    private void update() {
        if (pool != null) pool.shutdownNow();
        pool = Executors.newSingleThreadExecutor();
        pool.submit(new DocumentRunable(handler, HtmlParser.getVolPath(v_id)));
        Log.e(TAG, "------detail------->" + HtmlParser.getVolPath(v_id));
        pool.shutdown();
        handler.postDelayed(new VisRunable(ll_inc_loading, ll_inc_error, sv_detail_view), 6000);

        List<ContentValues> list = dataBaseDao.getVollist(v_id);
        if (!list.isEmpty()) {
            String coverUrl = list.get(0).getAsString(D.v_cover);
//            if (coverUrl.length() > 30) downloader.loadDrawable(coverUrl, iv_cover);   //加载图片
        }
    }

    private void setInfo(ContentValues cv) {
        tv_title.setText(cv.getAsString(D.v_id));                       //小说id（直接放入int类型会导致程序崩溃
        tv_Name.setText(cv.getAsString(D.v_name));                      //小说名
        tv_author.setText(getString(R.string.d_tv_author) + cv.getAsString(D.author));          //作者
        tv_illustration.setText(getString(R.string.d_tv_illustration) + cv.getAsString(D.illustration));//插画
        tv_library.setText(getString(R.string.d_tv_library) + cv.getAsString(D.library));       //文库
        tv_count.setText(getString(R.string.d_tv_count) + cv.getAsString(D.counts));//浏览数量
        tv_updatetime.setText("@"+cv.getAsString(D.updatetime));        //更新时间
        Log.e(TAG,"--------detail---->update----->"+cv.getAsString(D.updatetime));
        tv_about.setText(getString(R.string.d_tv_about) + cv.getAsString(D.about));             //简介
        vis.vis();
    }
}
