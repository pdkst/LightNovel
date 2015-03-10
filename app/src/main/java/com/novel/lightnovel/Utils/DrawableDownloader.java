package com.novel.lightnovel.Utils;

/**
 * Created by Crash on 2015/1/8.
 */

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrawableDownloader {
    private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    private final LinkedList<Drawable> mChacheController = new LinkedList<Drawable>();
    private ExecutorService mThreadPool;
    private final Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    public static int MAX_CACHE_SIZE = 80;
    public int THREAD_POOL_SIZE = 3;

    /**
     * Constructor
     */
    public DrawableDownloader() {
        mThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public DrawableDownloader(int THREAD_POOL_SIZE) {
        this.THREAD_POOL_SIZE = THREAD_POOL_SIZE;
    }

    /**
     * Clears all instance data and stops running threads
     */
    public void Reset() {
        ExecutorService oldThreadPool = mThreadPool;
        mThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        oldThreadPool.shutdownNow();
        mChacheController.clear();
        mCache.clear();
        mImageViews.clear();
    }

    /**
     * 多线程加载图片
     *
     * @param url       图片的地址
     * @param imageView 图片显示控件
     */
    public void loadDrawable(String url, ImageView imageView) {
        mImageViews.put(imageView, url);
        Drawable drawable = getDrawableFromCache(url);

        // check in UI thread, so no concurrency issues
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            queueJob(url, imageView);
        }
    }

    private Drawable getDrawableFromCache(String url) {
        if (mCache.containsKey(url)) {
            return mCache.get(url).get();
        }
        return null;
    }

    private synchronized void putDrawableInCache(String url, Drawable drawable) {
        int chacheControllerSize = mChacheController.size();
        if (chacheControllerSize > MAX_CACHE_SIZE)
            mChacheController.subList(0, MAX_CACHE_SIZE / 2).clear();
        mChacheController.addLast(drawable);
        mCache.put(url, new SoftReference<Drawable>(drawable));
    }

    private void queueJob(final String url, final ImageView imageView) {
    /* Create handler in UI thread. */
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj != null) {
                    imageView.setImageDrawable((Drawable) msg.obj);
                }
            }
        };
        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                final Drawable bmp = downloadDrawable(url);
                // if the view is not visible anymore, the image will be ready for next time in cache
                if (imageView.isShown()) {
                    Message message = Message.obtain();
                    message.obj = bmp;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private Drawable downloadDrawable(String url) {
        try {
            InputStream is = getInputStream(url);
            Drawable drawable = Drawable.createFromStream(is, url);

            putDrawableInCache(url, drawable);
            return drawable;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setUseCaches(true);
        connection.connect();
        return connection.getInputStream();
    }
}