package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Car;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.information.MD5;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/5.
 */

public class GoodsOrderActivity extends Activity {
    TextView goodsAddress, goodsAlterAddress, goodsMoney, goodsBackBtn;
    Button goodsOrder;
    ListView goodsList;
    User currentUser;
    List<Car> buyCar;
    int money = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_goodsorder);

        //
        buyCar = (List<Car>) getIntent().getSerializableExtra("car");
        //
        goodsAddress = (TextView) findViewById(R.id.goodsOrder_address);
        goodsAlterAddress = (TextView) findViewById(R.id.goodsOrder_alterAddress);
        goodsMoney = (TextView) findViewById(R.id.goodsOrder_money);
        goodsBackBtn = (TextView) findViewById(R.id.goodsOrder_back);
        goodsOrder = (Button) findViewById(R.id.goodsOrder_buy);
        goodsList = (ListView) findViewById(R.id.goodsOrder_goods);
        goodsList.setAdapter(baseAdapter);
        //设置总金额
        if (buyCar != null) {
            for (int i = 0; i < buyCar.size(); i++) {
                money = money + Integer.valueOf(buyCar.get(i).getGoods().getGoodsPiece()).intValue()
                        * Integer.valueOf(buyCar.get(i).getBuyNumber()).intValue();
            }
            goodsMoney.setText("总金额:" + String.valueOf(money));
        } else {
            goodsMoney.setText("总金额:" + String.valueOf(money));
        }
        //返回按钮
        goodsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //修改地址按钮
        goodsAlterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsOrderActivity.this, AlterAddressActivity.class);
                startActivity(intent);
            }
        });
        //继续支付按钮事件
        goodsOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(GoodsOrderActivity.this);
                final EditText editText = (EditText) inflater.inflate(R.layout.mall_edit, null);
                new AlertDialog.Builder(GoodsOrderActivity.this)
                        .setTitle("请输入支付密码：")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String password = editText.getText().toString();
                                Log.d("car", "get!!!!!" + password);
                                if (MD5.getMD5(password).equals(currentUser.getPasswordHash())) {
                                    Toast.makeText(GoodsOrderActivity.this, "12..."+password, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(GoodsOrderActivity.this, "2..."+password, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }


    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return buyCar == null ? 0 : buyCar.size();
        }

        @Override
        public Object getItem(int i) {
            return buyCar.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.mall_activity_order_list, null);
            }
            TextView mall = (TextView) view.findViewById(R.id.orderList_mallName);
            GoodsAvatar avatar = (GoodsAvatar) view.findViewById(R.id.orderList_goodsAvatar);
            TextView name = (TextView) view.findViewById(R.id.orderList_goodsName);
            TextView about = (TextView) view.findViewById(R.id.orderList_goodsAbout);
            TextView date = (TextView) view.findViewById(R.id.orderList_goodsDate);
            TextView piece = (TextView) view.findViewById(R.id.orderList_goodsPiece);
            TextView number = (TextView) view.findViewById(R.id.orderList_buyNumber);
            TextView money = (TextView) view.findViewById(R.id.orderList_money);
            Car car = buyCar.get(i);
            mall.setText("店铺:" + car.getGoods().getMall().getShopName());
            avatar.load(car.getGoods().getGoodsAvatar());
            name.setText(car.getGoods().getGoodsName());
            about.setText(car.getGoods().getGoodsAbout());
            date.setText(new SimpleDateFormat("yyyy-MM-dd").format(car.getCreateDate()));
            piece.setText("单价:" + car.getGoods().getGoodsPiece());
            number.setText("数量:" + car.getBuyNumber());
            money.setText("合计:"
                    + String.valueOf(Integer.valueOf(car.getGoods().getGoodsPiece()).intValue() * Integer.valueOf(car.getBuyNumber()).intValue()));
            return view;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    void getAddress() {
        Request request = Server.requestBuildWithMall("getCurrentUser")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsOrderActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    currentUser = new ObjectMapper().readValue(response.body().string(), User.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodsAddress.setText("地址:" + currentUser.getAddress() + "\n" + "联系方式:" + currentUser.getTel());
                        }
                    });

                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(GoodsOrderActivity.this)
                                    .setTitle("ERROR1")
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
