package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Jobs;
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

import static android.content.ContentValues.TAG;
import static com.example.fourpeople.campushousekeeper.R.drawable.btn_background;

/**
 * Created by Administrator on 2017/1/5.
 */

public class ResumeContentActivity extends Activity{
    boolean isEngage;
    Button Engage;
    Resume resume;
    Jobs jobs;
    EditText time_text,area_text,pone_text,remak_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_resumecontent);
        jobs=(Jobs)getIntent().getSerializableExtra("jobs");
        resume=(Resume) getIntent().getSerializableExtra("content");
        TextView contentPname=(TextView)findViewById(R.id.content_pname);
        TextView contentPsex=(TextView)findViewById(R.id.content_Psex);
        TextView contentPdetails=(TextView)findViewById(R.id.content_pdetails);
        TextView contentPtime=(TextView)findViewById(R.id.content_ptime);
        TextView contentMoney=(TextView)findViewById(R.id.content_money);
        TextView contentParea=(TextView)findViewById(R.id.content_area);
        TextView contentPhone=(TextView)findViewById(R.id.content_phone);
        time_text=(EditText)findViewById(R.id.time_text);
        area_text=(EditText)findViewById(R.id.area_text);
        pone_text=(EditText)findViewById(R.id.pone_text);
        remak_text=(EditText)findViewById(R.id.remak_text);
        contentMoney.setText("期待薪资："+resume.getMoney());
        contentParea.setText("求职区域："+resume.getArea());
        contentPdetails.setText("求职内容："+resume.getDetails());
        contentPhone.setText("联系方式："+resume.getPhone());
        contentPsex.setText("性别："+resume.getSex());
        contentPtime.setText("时间段："+resume.getTime());
        contentPname.setText(resume.getName());
        AvatarView avatarView=(AvatarView)findViewById(R.id.user_image);
        avatarView.load(Server.serverAddress+resume.getAvater());
        Button invite=(Button)findViewById(R.id.btn_invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goInvite();
            }
        });
        Engage=(Button)findViewById(R.id.btn_enploy);
        Engage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEngage();
            }
        });


    }
    void goInvite()
    {
        String time=time_text.getText().toString();
        String area=area_text.getText().toString();
        String phone=pone_text.getText().toString();
        String remark=remak_text.getText().toString();
        MultipartBody requestBody=new MultipartBody.Builder()
                .addFormDataPart("time",time)
                .addFormDataPart("area",area)
                .addFormDataPart("phone",phone)
                .addFormDataPart("remark",remark)
                .addFormDataPart("interviewer",resume.getAccount())
                .build();
        OkHttpClient client=Server.getSharedClient();
        Request request=Server.requestBuilderWithPartTime("interview")
                .method("post",null)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResumeContentActivity.this.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ResumeContentActivity.this.onSucceed(responseBody);
                    }
                });
            }
        });

    }
    void onSucceed(final String text)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ResumeContentActivity.this).setMessage(text)
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
                new AlertDialog.Builder(ResumeContentActivity.this).setMessage(e.getMessage()).show();
            }
        });

    }
    void goEngage()
    {
        OkHttpClient client=Server.getSharedClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("likes", String.valueOf(!isEngage))
                .addFormDataPart("studentId",resume.getAccount())
                .build();
        Request request=Server.requestBuilderWithPartTime("engage/"+jobs.getId())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    final String responseBody = response.body().string();
                }catch(Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reload();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }
    void reload()
    {
        reloadEngage();
        checkEngage();

    }
    void reloadEngage()
    {
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("studentId",resume.getAccount())
                .build();
        Request request = Server.requestBuilderWithPartTime("countEngage")
                .post(body)
                .build();
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
                    Log.e("responseString",responseString);
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
    void checkEngage()
    {
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("studentId",resume.getAccount())
                .build();
        Request request=Server.requestBuilderWithPartTime("checkEngage/"+jobs.getId())
                .post(body)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try
                {
                    final String responseString=response.body().string();
                    Log.e(responseString, "onResponse: ");
                    final Boolean result=new ObjectMapper().readValue(responseString,Boolean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(result);
                        }
                    });
                }catch (final Exception e)
                {
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
    void onCheckLikedResult(boolean result)
    {
        isEngage = result;
        Engage.setTextColor(result ? Color.BLUE : Color.BLACK);

    }
    void onReloadLikesResult(int count) {
        if (count > 0) {

            Engage.setBackground(getResources().getDrawable(R.drawable.part_employed));
        } else {

            Engage.setBackground(getResources().getDrawable(R.drawable.part_waitjobs));
        }
    }
}
