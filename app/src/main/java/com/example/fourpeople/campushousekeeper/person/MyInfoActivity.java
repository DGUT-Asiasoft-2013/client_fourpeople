package com.example.fourpeople.campushousekeeper.person;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/26.
 */

public class MyInfoActivity extends Activity {
    InfoItemCellFragment fragName;
    InfoItemCellFragment fragStudentId;
    InfoItemCellFragment fragSex;
    InfoItemCellFragment fragAddress;
    InfoItemCellFragment fragTel;
    InfoItemCellFragment fragEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity_my_info);

        fragName = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_name);
        fragStudentId = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_student_id);
        fragSex = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_sex);
        fragAddress = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_address);
        fragTel = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_tel);
        fragEmail = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_email);

        findViewById(R.id.btn_update_info).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goModify();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragName.setItemName("昵称：");
        fragStudentId.setItemName("学号：");
        fragSex.setItemName("性别：");
        fragAddress.setItemName("地址：");
        fragTel.setItemName("电话：");
        fragEmail.setItemName("邮箱：");

        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuildWithApi("info")
                .method("get", null)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(final Call arg0, Response arg1) throws IOException {
                try {
                    final User user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            MyInfoActivity.this.onResponse(arg0,user);

                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            MyInfoActivity.this.onFailure(arg0, e);

                        }
                    });
                }

            }

            @Override
            public void onFailure(final Call arg0, final IOException arg1) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        MyInfoActivity.this.onFailure(arg0, arg1);

                    }
                });

            }
        });
    }

    protected void onResponse(Call arg0, User user)
    {
        fragName.setItemInfo(user.getName());
        fragStudentId.setItemInfo(user.getStudentId());
        fragSex.setItemInfo(user.getSex());
        fragAddress.setItemInfo(user.getAddress());
        fragTel.setItemInfo(user.getTel());
        fragEmail.setItemInfo(user.getEmail());
    }

    void onFailure(Call call, Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("ERROR")
                .setMessage(e.getLocalizedMessage())
                .setNegativeButton("OK", null)
                .show();
    }

    void goModify() {
        Intent itnt = new Intent(this, ModifyInfoActivity.class);
        startActivity(itnt);
    }
}
