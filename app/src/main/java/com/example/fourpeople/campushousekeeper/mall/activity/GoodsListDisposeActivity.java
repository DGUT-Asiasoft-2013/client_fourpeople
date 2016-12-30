package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.Closeed;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/27.
 */

public class GoodsListDisposeActivity extends Activity {
    LinearLayout linearLayout;
    Goods goods;
    EditText name, piece, number, about;
    GoodsAvatar avatar;
    TextView backBtn, okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_goodslistdispose);
        //接受传送的信息
        goods = (Goods) getIntent().getSerializableExtra("goods");
        linearLayout = (LinearLayout) findViewById(R.id.goodsListDispose);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Closeed.onCloseClick(GoodsListDisposeActivity.this);
            }
        });
        //获取各个控件
        name = (EditText) findViewById(R.id.goodsListDispose_name);
        piece = (EditText) findViewById(R.id.goodsListDispose_piece);
        number = (EditText) findViewById(R.id.goodsListDispose_number);
        about = (EditText) findViewById(R.id.goodsListDispose_about);
        avatar = (GoodsAvatar) findViewById(R.id.goodsListDispose_avatar);
        backBtn = (TextView) findViewById(R.id.goodsListDispose_back);
        okBtn = (TextView) findViewById(R.id.goodsListDispose_ok);
        //设置当前的显示
        avatar.load(goods.getGoodsAvatar());
        name.setText(goods.getGoodsName());
        piece.setText(goods.getGoodsPiece());
        number.setText(goods.getGoodsNumber());
        about.setText(goods.getGoodsAbout());
        //返回按钮事件
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushAgain();
            }
        });
    }

    void pushAgain() {
        String goodsName = name.getText().toString();
        String goodsPiece = piece.getText().toString();
        String goodsNumber = number.getText().toString();
        String goodsAbout = about.getText().toString();
        MultipartBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("id", goods.getId().toString())
                .addFormDataPart("goodsName", goodsName)
                .addFormDataPart("goodsPiece", goodsPiece)
                .addFormDataPart("goodsNumber", goodsNumber)
                .addFormDataPart("goodsAbout", goodsAbout)
                .build();
        Request request = Server.requestBuildWithMall("goods/dispose")
                .method("post", null)
                .post(requestBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(GoodsListDisposeActivity.this)
                                .setTitle("网络连接失败...")
                                .setMessage(e.getLocalizedMessage())
                                .setNegativeButton("OK", null)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }
}
