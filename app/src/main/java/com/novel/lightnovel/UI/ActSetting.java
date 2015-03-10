package com.novel.lightnovel.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.novel.lightnovel.R;


public class ActSetting extends ActionBarActivity{
    private final static String TAG = "ActSetting";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);

        Log.e(TAG, "------actShareP------> sharePreference");

    }
}

