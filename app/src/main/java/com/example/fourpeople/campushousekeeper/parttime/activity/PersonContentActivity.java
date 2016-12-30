package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Resume;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.AvatarView;
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

public class PersonContentActivity extends Activity{
    Resume resume;
    Button releaseBook;
    boolean isBook;
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
        releaseBook=(Button)findViewById(R.id.btn_release_jobs);
        releaseBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goJoin();
            }
        });
    }
    void goJoin()
    {
        OkHttpClient client = Server.getSharedClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("release", String.valueOf(!isBook)).build();
        Request request = Server.requestBuilderWithPartTime("book/" + resume.getId() + "/release")
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
            Request request = Server.requestBuilderWithPartTime("book/" + resume.getId() + "/release")
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
            Request request = Server.requestBuilderWithPartTime("book/" + resume.getId() + "/released").get().build();
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
            releaseBook.setText("已预约(" + count + ")");
        } else {
            releaseBook.setText("预约");
        }
    }

    void onCheckLikedResult(boolean result) {
        isBook = result;
        releaseBook.setTextColor(result ? Color.BLUE : Color.BLACK);
    }
}

