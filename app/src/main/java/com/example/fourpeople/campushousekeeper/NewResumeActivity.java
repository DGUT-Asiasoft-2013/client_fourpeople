package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.fragments_widgets.PictureInputCellFragment;

import java.io.IOException;
import java.io.StreamCorruptedException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/20.
 */

public class NewResumeActivity extends Activity{
    EditText name,details,time,money,phone;
    PictureInputCellFragment avata;
     RadioGroup sex=null;
     RadioButton male=null;
     RadioButton female=null;
     String temp;
    CheckBox lg,cy,gy,gk;
    StringBuilder sb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newresume);
        //取出单选按钮的数据
        this.sex=(RadioGroup) super.findViewById(R.id.sex);
        this.male=(RadioButton) super.findViewById(R.id.male);
        this.female=(RadioButton) super.findViewById(R.id.female);
        this.sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(NewResumeActivity.this.male.getId()==i){
                    temp="男";
                }
                else if(NewResumeActivity.this.female.getId()==i){
                    temp="女";
                }
            }
        });
        //取出多选按钮的内容
        sb = new StringBuilder();
        lg=(CheckBox)findViewById(R.id.ligCheckBoxId);
        cy=(CheckBox)findViewById(R.id.chengyCheckBoxId);
        gy=(CheckBox)findViewById(R.id.guangyCheckBoxId);
        gk=(CheckBox)findViewById(R.id.guangkeCheckBoxId) ;
        lg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sb.append("理工");
            }
        });
        cy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sb.append("城院");
            }
        });
        gy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sb.append("广医");
            }
        });
        gk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sb.append("广科");
            }
        });
        name=(EditText)findViewById(R.id.text_name);
        details=(EditText)findViewById(R.id.details);
        time=(EditText)findViewById(R.id.text_time);
        money=(EditText)findViewById(R.id.job_money);
        phone=(EditText)findViewById(R.id.phone);
        avata=(PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.choice_image);
        final Button submit=(Button)findViewById(R.id.btn_sumit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }
    void submit() {
        //EditText name,details,time,money,phone;
        String pname = name.getText().toString();
        String pdetails = details.getText().toString();
        String ptime = time.getText().toString();
        String pmoney = money.getText().toString();
        String pphone = phone.getText().toString();
        OkHttpClient client = Server.getSharedClient();
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", pname)
                .addFormDataPart("sex", temp)
                .addFormDataPart("details", pdetails)
                .addFormDataPart("time", ptime)
                .addFormDataPart("money", pmoney)
                .addFormDataPart("phone", pphone)
                .addFormDataPart("area", String.valueOf(sb));
        //定义一个字节型数组，保存图片文件
        byte[] pngData = avata.getPngData();
        if (pngData != null) {
            RequestBody pngRequestBody = RequestBody.create(MediaType.parse("image/png"), pngData);
            bodyBuilder.addFormDataPart("avater", "avater.png", pngRequestBody);
        }
        //创建连接数据的url,以及数据
        MultipartBody postBody = bodyBuilder.build();
        Request request = Server.requestBuilderWithPartTime("resume")
                .method("post", null)
                .post(postBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                NewResumeActivity.this.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewResumeActivity.this.onSucceed(responseBody);
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
                new AlertDialog.Builder(NewResumeActivity.this).setMessage(text)
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
                new AlertDialog.Builder(NewResumeActivity.this).setMessage(e.getMessage()).show();
            }
        });

    }
}



