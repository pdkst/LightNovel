package com.novel.lightnovel.UI;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import com.novel.lightnovel.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActMore extends Fragment implements View.OnClickListener{
    private final static String TAG = "ActMore";

    private TableRow tr_setting_feedback,tr_setting,tr_manage_user,tr_manage_down,tr_partner,tr_partner_sql,tr_update,tr_info;
    private Context context;

    public ActMore() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.act_tab_more, container, false);
        context = view.getContext();
        tr_setting_feedback = (TableRow) view.findViewById(R.id.tr_setting_feedback);
        tr_setting = (TableRow) view.findViewById(R.id.tr_setting);
        tr_manage_user = (TableRow) view.findViewById(R.id.tr_manage_user);
        tr_manage_down = (TableRow) view.findViewById(R.id.tr_manage_down);
        tr_partner = (TableRow) view.findViewById(R.id.tr_partner);
        tr_partner_sql = (TableRow) view.findViewById(R.id.tr_partner_sql);
        tr_update = (TableRow) view.findViewById(R.id.tr_update);
        tr_info = (TableRow) view.findViewById(R.id.tr_info);
        tr_setting_feedback.setOnClickListener(this);
        tr_setting.setOnClickListener(this);
        tr_manage_user.setOnClickListener(this);
        tr_manage_down.setOnClickListener(this);
        tr_partner.setOnClickListener(this);
        tr_partner_sql.setOnClickListener(this);
        tr_update.setOnClickListener(this);
        tr_info.setOnClickListener(this);

        return view;
    }

    private void feedback() {
        Uri uri = Uri.parse("mailto:pdk_studio@foxmail.com");
        String[] email = {"pdk_studio@foxmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "LightNovel:>>意见反馈"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tr_setting_feedback:
                feedback();
                break;
            case R.id.tr_setting:
                Intent intent = new Intent(context,ActSetting.class);
                startActivity(intent);
            case R.id.tr_manage_user:
            case R.id.tr_manage_down:
            case R.id.tr_partner:
            case R.id.tr_partner_sql:
            case R.id.tr_update:
            case R.id.tr_info:
                Toast.makeText(context,"只是为了看起来像布卡....",Toast.LENGTH_SHORT).show();
        }
    }

}
