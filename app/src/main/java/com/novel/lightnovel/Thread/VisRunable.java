package com.novel.lightnovel.Thread;

import android.view.View;

/**
 * Created by Crash on 2015/2/24.
 * 用来管理framelayout布局界面的更新显示（主要是加载中，错误，内容页的切换
 */
public class VisRunable implements Runnable{
	private View loading;
	private View error;
	private View v;

	/**
	 * 构造函数：实例化一个新的对象
	 * 如果error界面处于显示状态，那么隐藏error界面，显示加载界面
	 * @param loading 加载界面
	 * @param error 错误界面
	 * @param v 完成界面（用来获取任务是否完成）
	 */
	public VisRunable(View loading, View error, View v) {
		this.loading = loading;
		this.error = error;
		this.v = v;
		if (error.getVisibility()==View.VISIBLE){
            load();
		}
	}

    /**
     * 直接显示加载页面
     */
    public void load() {
        loading.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        v.setVisibility(View.GONE);
    }
    /**
     * 直接显示错误页面
     */
    public void error() {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        v.setVisibility(View.GONE);
    }
    public void vis() {
        loading.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    @Override
	public void run() {
		if (v.getVisibility()==View.GONE||v.getVisibility()==View.INVISIBLE){
            error();
		}
	}


}
