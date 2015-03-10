package com.novel.lightnovel.test;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.novel.lightnovel.DataBase.D;
import com.novel.lightnovel.DataBase.DataBase;
import com.novel.lightnovel.DataBase.DataBaseDao;
import com.novel.lightnovel.DataBase.DataBaseHelper;
import com.novel.lightnovel.R;
import com.novel.lightnovel.UI.ActVolDetail;
import com.novel.lightnovel.Utils.ShareStore;

import java.util.List;

public class TestActivity extends ActionBarActivity implements View.OnClickListener{
    private TextView tv_test;
    private EditText et_test_id;

    private ShareStore shareStore;
    private DataBase dataBaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv_test = (TextView)findViewById(R.id.tv_test);
        et_test_id = (EditText) findViewById(R.id.et_test_id);

        shareStore = new ShareStore(this);
        dataBaseDao = new DataBaseDao(this);
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final List<ContentValues> list = dataBaseDao.getFavor_list2();

    }

    private void store() {
        Intent intent = new Intent(this, ActVolDetail.class);
        String v_id = et_test_id.getText().toString();
        intent.putExtra(D.v_id,Integer.parseInt(v_id));
        startActivity(intent);
    }

    private void read() {
        List<ContentValues> list = dataBaseDao.getVollist(162);
        String str = list.get(0).getAsString(D.updatetime);
        if (!TextUtils.isEmpty(str))tv_test.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_read:
                read();
                break;
            case R.id.btn_test_store:
                store();
                break;
        }
    }
}
