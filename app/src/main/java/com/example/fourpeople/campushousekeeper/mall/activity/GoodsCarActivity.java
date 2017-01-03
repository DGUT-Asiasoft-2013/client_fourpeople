package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GoodsCarActivity extends Activity {
    ListView goodsCarList;
    CheckBox goodsCarCheckBox;
    Button goodsCarBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_goodscar);
        goodsCarList = (ListView) findViewById(R.id.goodsCar_goodsList);
        goodsCarCheckBox = (CheckBox) findViewById(R.id.goodsCar_choiceAll);
        goodsCarBuy = (Button) findViewById(R.id.goodsCar_buy);
        goodsCarList.setAdapter(baseAdapter);
    }

    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.mall_list_goodscar, null);
            }
            TextView goodsCarMall = (TextView) findViewById(R.id.goodsCarList_mallName);
            TextView goodsCarDelete = (TextView) findViewById(R.id.goodsCarList_delete);
            //
            CheckBox goodsCarCheck = (CheckBox) findViewById(R.id.goodsCarList_checkBox);
            GoodsAvatar goodsCarAvatar = (GoodsAvatar) findViewById(R.id.goodsCarList_goodsAvatar);
            //
            TextView goodsCarName = (TextView) findViewById(R.id.goodsCarList_goodsName);
            TextView goodsCarAbout = (TextView) findViewById(R.id.goodsCarList_goodsAbout);
            TextView goodsCarDate = (TextView) findViewById(R.id.goodsCarList_goodsDate);
            //
            TextView goodsCarPiece = (TextView) findViewById(R.id.goodsCarList_goodsPiece);
            TextView goodsCarNmuber = (TextView) findViewById(R.id.goodsCarList_goodsNumber);
            TextView goodsCarAdd = (TextView) findViewById(R.id.goodsCarList_add);
            TextView goodsCarBuyNumber = (TextView) findViewById(R.id.goodsCarList_buyNumber);
            TextView goodsCarReduce = (TextView) findViewById(R.id.goodsCarList_reduce);
            return view;
        }
    };

    @Override
    protected void onResume() {
        getMessage();
        super.onResume();
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsCarActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
