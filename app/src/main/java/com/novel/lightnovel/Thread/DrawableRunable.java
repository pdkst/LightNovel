package com.novel.lightnovel.Thread;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Crash on 2015/1/8.
 */


public class DrawableRunable implements Runnable{
    private static final int CONNECT_ERROR = 0;
    private static final int READ_ERROR = 1;
    private static final int READ_OK = 4;
    private Handler handler;
    private String url;

    public DrawableRunable(Handler handler, String url) {
        this.handler = handler;
        this.url = url;
    }


    @Override
    public void run() {
        //执行更新任务的子线程
        Message msg = handler.obtainMessage(READ_ERROR);
        try {
            Drawable drawable = Drawable.createFromStream(getInputStream(url), url);
            msg = handler.obtainMessage(READ_OK, drawable);
        } catch (IOException e) {
            e.printStackTrace();
            msg.what = CONNECT_ERROR;
        }
        handler.sendMessage(msg);
    }
    private InputStream getInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setUseCaches(true);
        connection.connect();
        return connection.getInputStream();
    }
}