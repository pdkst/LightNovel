package com.novel.lightnovel.Thread;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;

import com.novel.lightnovel.Utils.HtmlParser;

import org.apache.http.HttpConnection;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.ResponseDate;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Crash on 2015/1/29.
 */
public class DocumentRunable implements Runnable {
    private static final String TAG = "DocumentRunable";

    public static final int CONNECT_ERROR = 0;
    public static final int READ_ERROR = 1;
    public static final int READ_OK = 2;

    private Handler handler;
    private String url;

    public DocumentRunable(Handler handler) {
        this.handler = handler;
        this.url = "http://lknovel.lightnovel.cn/";
    }

    public DocumentRunable(Handler handler, String url) {
        this.handler = handler;
        this.url = url;
    }

    public static boolean isConnected(Context context) {
        //判断网络连接状态
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return wifi || internet;
    }

    @Override
    public void run() {
        //执行更新任务的子线程
        Message msg = handler.obtainMessage(READ_ERROR);
        try {
            Document document = HtmlParser.doc(url);
            if (HtmlParser.volTest(document)) {
                Log.e(TAG, "-----documentRunable----vTest-----> = true;");
                Log.e(TAG, "-----documentRunable---->"+(document==null));
                msg = handler.obtainMessage(READ_OK,document);
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.what = CONNECT_ERROR;
            Log.e(TAG,"-----documentRunable----->connect_error--------->url="+url);
        }
        boolean b = handler.sendMessage(msg);
        Log.e(TAG, "-----documentRunable----send-------> = " + b);
        Log.e(TAG, "-----documentRunable----msg.what---> = " + msg.what);
    }
}
