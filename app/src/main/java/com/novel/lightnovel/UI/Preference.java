package com.novel.lightnovel.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.novel.lightnovel.R;
import com.novel.lightnovel.Utils.SDCardScanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/**
 * Created by Crash on 2015/3/2.
 */

public class Preference extends PreferenceFragment implements android.preference.Preference.OnPreferenceChangeListener {
    private final static String TAG = "Preference";

    private CheckBoxPreference setting_net_read;
    private ListPreference setting_home_tab;
    private CheckBoxPreference setting_exit;
    private ListPreference setting_sd_path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        Log.e(TAG, "------------> sharePreference");
        setting_home_tab = (ListPreference) findPreference("setting_home_tab");
        setting_exit = (CheckBoxPreference) findPreference("setting_exit");
        setting_sd_path = (ListPreference) findPreference("setting_sd_path");
        setting_net_read = (CheckBoxPreference) findPreference("setting_net_read");
        setting_home_tab.setOnPreferenceChangeListener(this);
        setting_exit.setOnPreferenceChangeListener(this);
        setting_sd_path.setOnPreferenceChangeListener(this);
        setting_net_read.setOnPreferenceChangeListener(this);

        String home = setting_home_tab.getValue();
        Log.e(TAG, "------sharePreference------>home = " + home);
        if ("TAB_RECOM".equals(home))setting_home_tab.setSummary("小说推荐");
        else setting_home_tab.setSummary("我的收藏");

        String type = setting_sd_path.getValue();
        Log.e(TAG, "------sharePreference------>type = " + type);
        if ("EXTERNAL_STORAGE".equals(type))
            setting_sd_path.setSummary(System.getenv("EXTERNAL_STORAGE") + "/imaho/down");
        else setting_sd_path.setSummary(System.getenv("SECONDARY_STORAGE") + "/imaho/down");

    }

    /**
     * 绑定Preference监听事件，当设置的值变化时，执行下面代码，不绑定默认也是生效的，绑定只是为了执行额外操作
     *
     * @param preference 被更改的设置对象
     * @param newValue   设置的新的值
     * @return 返回ture则保存新的值，false则不保存
     */
    @Override
    public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
        if (preference == setting_exit) {
            Log.d(TAG, "------设置--exit---->" + String.valueOf(newValue));
            return true;
        }else if (preference == setting_net_read) {
            Log.d(TAG, "------设置--net_read---->" + String.valueOf(newValue));
            return true;
        } else if (preference == setting_home_tab) {
            Log.d(TAG, "-----newvalue.tostring--------->" + newValue.toString());
            if ("TAB_RECOM".equals(newValue.toString())) {
                preference.setSummary("小说推荐");
            } else if ("TAB_FAVOR".equals(newValue.toString())) {
                preference.setSummary("我的收藏");
            }
            return true;
        } else if (preference == setting_sd_path) {
            Log.d(TAG, "-----newvalue.tostring--------->" + newValue.toString());
            if ("EXTERNAL_STORAGE".equals(newValue.toString())) {
                preference.setSummary(System.getenv("EXTERNAL_STORAGE") + "/imaho/down");
            } else if ("SECONDARY_STORAGE".equals(newValue.toString())) {
                preference.setSummary(System.getenv("SECONDARY_STORAGE") + "/imaho/down");
            }
            return true;
        }
        return false;
    }
}

