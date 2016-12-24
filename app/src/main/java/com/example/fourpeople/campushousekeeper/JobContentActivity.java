package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class JobContentActivity extends Activity{
    Jobs jobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobcontent);
        jobs=(Jobs) getIntent().getSerializableExtra("content");
        TextView contentKind=(TextView)findViewById(R.id.content_kind);
        TextView contentMoney=(TextView)findViewById(R.id.content_money);
        TextView contentAmount=(TextView)findViewById(R.id.content_amount);
        TextView contentTime=(TextView)findViewById(R.id.content_time);
        //以后补上
       // TextView contentArea=(TextView)findViewById(R.id.content_area);

        TextView contentDetails=(TextView)findViewById(R.id.content_details);
        TextView contentRemark=(TextView)findViewById(R.id.content_remark);
        TextView contentPhone=(TextView)findViewById(R.id.content_phone);
        contentKind.setText("类型："+jobs.getKind());
        contentMoney.setText("薪资："+jobs.getMoney());
        contentAmount.setText("人数："+jobs.getAmount());
        contentTime.setText("时间："+jobs.getTime());
        contentDetails.setText("工作内容："+jobs.getDetails());
        contentRemark.setText("备注："+jobs.getRemark());
        contentPhone.setText("联系方式："+jobs.getPhone());
        Button btnJoin=(Button)findViewById(R.id.btn_release_jobs);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goJoin();
            }
        });
    }
    void goJoin() {
        OkHttpClient client = Server.getSharedClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("joinsId", String.valueOf(jobs.getId())).build();
        Request request = Server.requestBuilderWithPartTime("joins")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                JobContentActivity.this.onFailure(e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JobContentActivity.this.onSucceed(responseBody);
                        }
                    });

                } catch (final Exception e) {

                }
            }
        });
    }
    void onSucceed(final String text)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(JobContentActivity.this).setMessage(text)
                        .setPositiveButton("oK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }
        });

    }
    void onFailure(final Exception e)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(JobContentActivity.this).setMessage(e.getMessage()).show();
            }
        });

    }
}
