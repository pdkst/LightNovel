package com.novel.lightnovel.Service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.novel.lightnovel.Utils.FileFactory;
import com.novel.lightnovel.Utils.HtmlParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {
	public static int STOP = 0;
	public static int RUN = 11;
	public static int PAUSE = 12;
	public int STATS = 0;

	private ExecutorService pool;

	public DownBinder binder = new DownBinder();

	public DownloadService() {
		pool = Executors.newSingleThreadExecutor();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: 绑定下载服务
		return binder;
	}

	private boolean addDownload(ContentValues cv) {
		String path = cv.getAsString("path");
		int v_id = cv.getAsInteger("v_id");
		pool.submit(new downloadRunable(path, v_id));
		return true;
	}
	private boolean pauseAll(){
		List<Runnable> list = pool.shutdownNow();


		return false;
	}
	private boolean pause(ContentValues cv){
		return false;
	}

	/**
	 * 下载单个view
	 *
	 * @param path view路径
	 * @param v_id vollist id
	 * @return 成功返回true
	 */
	private boolean download(String path, int v_id) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "IE11");
			int length = conn.getContentLength();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();

				FileFactory fileFactory = FileFactory.newInstence(getApplicationContext());
				int view_id = HtmlParser.getId(path);
				File file = fileFactory.getViewfile(v_id, view_id);
				FileOutputStream fos = new FileOutputStream(file);

				int len = 0;
				byte[] bytes = new byte[1024];
				while ((len = inputStream.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
				}

				fos.close();
				inputStream.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private InputStream getInputStream(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "IE11");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) return conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public class DownBinder extends Binder {


		public boolean addDown(ContentValues cv) {

			return addDownload(cv);
		}

	}

	private class downloadRunable implements Runnable {
		private String path;
		private int v_id;

		public downloadRunable(String path, int v_id) {
			this.path = path;
			this.v_id = v_id;
		}

		@Override
		public void run() {
			download(path, v_id);
		}
	}
}
