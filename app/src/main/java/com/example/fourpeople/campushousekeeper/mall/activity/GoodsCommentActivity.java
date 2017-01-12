package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.GoodsComment;
import com.example.fourpeople.campushousekeeper.api.MyOrder;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.Closeed;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/9.
 */
public class GoodsCommentActivity extends Activity {
    TextView backBtn, okBtn;
    EditText edit;
    MyOrder currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_comment);
        currentOrder = (MyOrder) getIntent().getSerializableExtra("currentOrder");
        backBtn = (TextView) findViewById(R.id.comment_back);
        okBtn = (TextView) findViewById(R.id.comment_ok);
        edit = (EditText) findViewById(R.id.comment_edit);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushComment();
            }
        });
    }

    void pushComment() {
        Closeed.onCloseClick(GoodsCommentActivity.this);
        String text = edit.getText().toString();
        if (text.equals("")) {
            new AlertDialog.Builder(GoodsCommentActivity.this)
                    .setTitle("提示")
                    .setMessage("请输入评价内容！")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("text", text)
                .addFormDataPart("orderId", String.valueOf(currentOrder.getId()))
                .build();
        Request request = Server.requestBuildWithMall("pushComment")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsCommentActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final GoodsComment comment = new ObjectMapper().readValue(response.body().string(), GoodsComment.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsCommentActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(GoodsCommentActivity.this)
                                    .setTitle("GoodsComment Error")
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
