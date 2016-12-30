package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class JobContentActivity extends Activity {
    boolean isRelease;
    Button btnJoin;
    Jobs jobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_jobcontent);
        jobs = (Jobs) getIntent().getSerializableExtra("content");
        TextView contentKind = (TextView) findViewById(R.id.content_kind);
        TextView contentMoney = (TextView) findViewById(R.id.content_money);
        TextView contentAmount = (TextView) findViewById(R.id.content_amount);
        TextView contentTime = (TextView) findViewById(R.id.content_time);
        //以后补上
        // TextView contentArea=(TextView)findViewById(R.id.content_area);

        TextView contentDetails = (TextView) findViewById(R.id.content_details);
        TextView contentRemark = (TextView) findViewById(R.id.content_remark);
        TextView contentPhone = (TextView) findViewById(R.id.content_phone);
        contentKind.setText("类型：" + jobs.getKind());
        contentMoney.setText("薪资：" + jobs.getMoney());
        contentAmount.setText("人数：" + jobs.getAmount());
        contentTime.setText("时间：" + jobs.getTime());
        contentDetails.setText("工作内容：" + jobs.getDetails());
        contentRemark.setText("备注：" + jobs.getRemark());
        contentPhone.setText("联系方式：" + jobs.getPhone());
        btnJoin = (Button) findViewById(R.id.btn_release_jobs);
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
                .addFormDataPart("release", String.valueOf(!isRelease)).build();
        Request request = Server.requestBuilderWithPartTime("joins/" + jobs.getId() + "/release")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                reload();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reload();
                        }
                    });

                } catch (final Exception e) {

                }
            }
        });
    }

    //检查自己是否报名


    @Override
    protected void onResume() {
        super.onResume();
        reload();

    }

    void reload() {
        reloadRelease();
        checkRelease();
    }

    void reloadRelease() {
        {
            Request request = Server.requestBuilderWithPartTime("joins/" + jobs.getId() + "/release")
                    .get().build();
            Server.getSharedClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReloadLikesResult(0);
                        }
                    });


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {
                        String responseString = response.body().string();
                        final Integer count = new ObjectMapper().readValue(responseString, Integer.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onReloadLikesResult(count);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onReloadLikesResult(0);
                            }
                        });

                    }
                }
            });
        }

    }

    void checkRelease() {
        {
            Request request = Server.requestBuilderWithPartTime("joins/" + jobs.getId() + "/released").get().build();
            Server.getSharedClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {
                        final String responseString = response.body().string();
                        final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onCheckLikedResult(result);
                            }
                        });
                    } catch (final Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onCheckLikedResult(false);
                            }
                        });
                    }
                }
            });

        }

    }

    void onReloadLikesResult(int count) {
        if (count > 0) {
            btnJoin.setText("已申请(" + count + ")");
        } else {
            btnJoin.setText("报名");
        }
    }

    void onCheckLikedResult(boolean result) {
        isRelease = result;
        btnJoin.setTextColor(result ? Color.BLUE : Color.BLACK);
    }
}
