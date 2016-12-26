package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Resume;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.AvatarView;

/**
 * Created by Administrator on 2016/12/21.
 */

public class PersonContentActivity extends Activity{
    Resume resume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_personcontent);
        resume=(Resume) getIntent().getSerializableExtra("content");
        TextView contentPname=(TextView)findViewById(R.id.content_pname);
        TextView contentPsex=(TextView)findViewById(R.id.content_Psex);
        TextView contentPdetails=(TextView)findViewById(R.id.content_pdetails);
        TextView contentPtime=(TextView)findViewById(R.id.content_ptime);
        TextView contentMoney=(TextView)findViewById(R.id.content_money);
        TextView contentParea=(TextView)findViewById(R.id.content_area);
        TextView contentPhone=(TextView)findViewById(R.id.content_phone);
        contentMoney.setText("期待薪资："+resume.getMoney());
        contentParea.setText("求职区域："+resume.getArea());
        contentPdetails.setText("求职内容："+resume.getDetails());
        contentPhone.setText("联系方式："+resume.getPhone());
        contentPsex.setText("性别："+resume.getSex());
        contentPtime.setText("时间段："+resume.getTime());
        contentPname.setText(resume.getName());
        AvatarView avatarView=(AvatarView)findViewById(R.id.user_image);
        avatarView.load(Server.serverAddress+resume.getAvater());
    }
}
