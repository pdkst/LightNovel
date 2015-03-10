package com.novel.lightnovel.UI;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.novel.lightnovel.DataBase.DataBase;
import com.novel.lightnovel.DataBase.DataBaseDao;
import com.novel.lightnovel.R;
import com.novel.lightnovel.test.TestActivity;

import java.util.List;


public class ActFavor extends Fragment{
	private static final String TAG = "ActFavor";

    private ListView lv_my_favor;
    private TextView tv_my_name;

    private Context context;
    private FavorAdapter favorAdapter;

    private List<ContentValues> list = null;
    private DataBase dataBaseDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.act_tab_favor, container, false);
        context = view.getContext();

        lv_my_favor = (ListView)view.findViewById(R.id.lv_my_favor);
        tv_my_name = (TextView)view.findViewById(R.id.tv_my_name);

        dataBaseDao = new DataBaseDao(context);

        favorAdapter = new FavorAdapter(context,getData());
        lv_my_favor.setAdapter(favorAdapter);

        tv_my_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),TestActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private List<ContentValues> getData(){
	    Log.w(TAG,"getData()---->"+(dataBaseDao.getFavor_list()==null));
        return dataBaseDao.getFavor_list();
    }

    @Override
    public void onResume() {
        dataBaseDao = new DataBaseDao(context);
        list = dataBaseDao.getFavor_list();
        favorAdapter.setList(list);
        favorAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
