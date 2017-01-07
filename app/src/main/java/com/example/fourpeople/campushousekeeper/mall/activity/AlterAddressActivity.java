package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.mall.view.Closeed;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/5.
 */

public class AlterAddressActivity extends Activity {
    EditText address, tel;
    TextView backBtn;
    Button okBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_alteraddress);
        address = (EditText) findViewById(R.id.alterAddress_address);
        tel = (EditText) findViewById(R.id.alterAddress_tel);
        backBtn = (TextView) findViewById(R.id.alterAddress_back);
        okBtn = (Button) findViewById(R.id.alterAddress_ok);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterAddress();
            }
        });
    }

    void alterAddress() {

        Closeed.onCloseClick(AlterAddressActivity.this);
        //
        String userAddress = address.getText().toString();
        String userTel = tel.getText().toString();
        if (userAddress.equals("") || userTel.equals("")) {
            new AlertDialog.Builder(AlterAddressActivity.this)
                    .setTitle("警告")
                    .setMessage("请填写完内容")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("address", userAddress)
                .addFormDataPart("tel", userTel)
                .build();
        Request request = Server.requestBuildWithMall("alterAddress")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AlterAddressActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    User user = new ObjectMapper().readValue(response.body().string(), User.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            address.setText("");
                            tel.setText("");
                            Toast.makeText(AlterAddressActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(AlterAddressActivity.this)
                                    .setTitle("ERROR2")
                                    .setMessage(e.getMessage())
                                    .setNegativeButton("OK", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
