package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fourpeople.campushousekeeper.api.Server;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/19.
 */

public class NewJobsActivity extends Activity{
    Spinner spinner;
    ArrayAdapter arr_adapter;
    ArrayList<String> data_list;
    EditText jobContent,jobTime,jobMoney,jobPhone,jobRemark,personAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_jobs);
        spinner = (Spinner) findViewById(R.id.spinner);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("代课");
        data_list.add("lol代练");
        data_list.add("派单");
        data_list.add("其他");

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        jobContent=(EditText)findViewById(R.id.text_content);
        jobTime=(EditText)findViewById(R.id.job_time);
        jobMoney=(EditText)findViewById(R.id.text_money);
        jobPhone=(EditText)findViewById(R.id.text_phone);
        jobRemark=(EditText)findViewById(R.id.text_remark);
        personAccount=(EditText)findViewById(R.id.person_account);
        findViewById(R.id.btn_release_jobs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendContent();

            }
        });

    }
    void sendContent()
    {
        String content=jobContent.getText().toString();
        String time=jobTime.getText().toString();
        String money=jobMoney.getText().toString();
        String phone=jobPhone.getText().toString();
        String remark=jobRemark.getText().toString();
        String kinds=spinner.getSelectedItem().toString();
        String account=personAccount.getText().toString();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("kind",kinds)
                .addFormDataPart("details",content)
                .addFormDataPart("time",time)
                .addFormDataPart("money",money)
                .addFormDataPart("amount",account)
                .addFormDataPart("remark",remark)

                .addFormDataPart("phone",phone).build();
        Request request= Server.requestBuilderWithPartTime("jobs")
                .post(body)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NewJobsActivity.this.onFailure(e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseBody = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewJobsActivity.this.onSucceed(responseBody);
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
                new AlertDialog.Builder(NewJobsActivity.this).setMessage(text)
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
                new AlertDialog.Builder(NewJobsActivity.this).setMessage(e.getMessage()).show();
            }
        });

    }
}






