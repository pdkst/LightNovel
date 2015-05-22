package com.novel.lightnovel.UI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.novel.lightnovel.R;


public class ActHomeMain extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "ActHomeMain";
    private RadioGroup rg_view_tab;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_view);
        fragmentManager = getFragmentManager();
        String home = PreferenceManager.getDefaultSharedPreferences(this).getString("setting_home_tab", "TAB_RECOM");
        rg_view_tab = (RadioGroup) findViewById(R.id.rg_view_tab);
        rg_view_tab.setOnCheckedChangeListener(this);
        if ("TAB_RECOM".equals(home)){
            fragmentManager.beginTransaction().replace(R.id.home_view_content, new ActRecom()).commit();
            rg_view_tab.check(R.id.rb_act_recom);
        }
        else
            fragmentManager.beginTransaction().replace(R.id.home_view_content, new ActFavor()).commit();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (checkedId) {
            case R.id.rb_act_favor:
                fragment = new ActFavor();
                fragmentTransaction.replace(R.id.home_view_content, fragment, "Favor").commit();
                break;
            case R.id.rb_act_recom:
                fragment = new ActRecom();
                fragmentTransaction.replace(R.id.home_view_content, fragment, "Recom").commit();
                break;
            case R.id.rb_act_search:
                fragment = new ActSearch();
                fragmentTransaction.replace(R.id.home_view_content, fragment, "Search").commit();
                break;
            case R.id.rb_act_more:
                fragment = new ActMore();
                fragmentTransaction.replace(R.id.home_view_content, fragment, "More").commit();
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e(TAG, "------home----->" + PreferenceManager.getDefaultSharedPreferences(this).getBoolean("setting_exit", false));
            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("setting_exit", false)) {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    Toast.makeText(this, "再按一下退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("提示");
                alertDialog.setMessage("确认退出程序？");
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });
                alertDialog.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
