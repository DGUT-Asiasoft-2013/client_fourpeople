package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Car;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/30.
 */

public class GoodsBuyActivity extends Activity {
    Goods goods;
    GoodsAvatar goodsAvatar;
    TextView goodsName, goodsAbout, goodsDate, goodsPiece, goodsNumber, goodsAdd, goodsBuyNumber, goodsReduce;
    TextView goodsBack, goodsLiked;
    Button goodsAddCart, goodsBuy;
    ListView commentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goods = (Goods) getIntent().getSerializableExtra("goods");
        //
        setContentView(R.layout.mall_activity_goodsbuy);
        goodsAvatar = (GoodsAvatar) findViewById(R.id.goodsBuy_avatar);
        goodsName = (TextView) findViewById(R.id.goodsBuy_name);
        goodsAbout = (TextView) findViewById(R.id.goodsBuy_about);
        goodsDate = (TextView) findViewById(R.id.goodsBuy_date);
        goodsPiece = (TextView) findViewById(R.id.goodsBuy_piece);
        goodsNumber = (TextView) findViewById(R.id.goodsBuy_number);
        goodsAdd = (TextView) findViewById(R.id.goodsBuy_add);
        goodsBuyNumber = (TextView) findViewById(R.id.goodsBuy_buyNumber);
        goodsReduce = (TextView) findViewById(R.id.goodsBuy_reduce);
        //
        goodsBack = (TextView) findViewById(R.id.goodsBuy_back);
        goodsLiked = (TextView) findViewById(R.id.goodsBuy_like);
        //
        commentList = (ListView) findViewById(R.id.goodsBuy_comment);
        goodsAddCart = (Button) findViewById(R.id.goodsBuy_addCart);
        goodsBuy = (Button) findViewById(R.id.goodsBuy_buy);
        goodsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        goodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(goodsBuyNumber.getText().toString()).intValue() < Integer.valueOf(goods.getGoodsNumber().toString()).intValue()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int i = Integer.valueOf(goodsBuyNumber.getText().toString()).intValue() + 1;
                            goodsBuyNumber.setText(String.valueOf(i));
                        }
                    });
                } else if (Integer.valueOf(goods.getGoodsNumber().toString()).intValue() <= 0) {
                    goodsBuyNumber.setText(String.valueOf(0));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsBuyActivity.this, "已达到最大购买数量!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
        goodsReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(goodsBuyNumber.getText().toString()).intValue() > 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int i = Integer.valueOf(goodsBuyNumber.getText().toString()).intValue() - 1;
                            goodsBuyNumber.setText(String.valueOf(i));
                        }
                    });
                }
            }
        });
        //加入购物车
        goodsAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart();
            }
        });
        //购买按钮事件
        goodsBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOrder();
            }
        });
    }

    @Override
    protected void onResume() {
        goodsAvatar.load(goods.getGoodsAvatar());
        goodsName.setText(goods.getGoodsName());
        goodsAbout.setText("备注:" + goods.getGoodsAbout());
        goodsDate.setText("上架时间:" + new SimpleDateFormat("yyyy-MM-dd").format(goods.getCreateDate()));
        goodsPiece.setText("价格:￥" + goods.getGoodsPiece());

        if (Integer.valueOf(goods.getGoodsNumber()).intValue() > 0) {
            goodsBuyNumber.setText("1");
        } else {
            goodsBuyNumber.setText("0");
        }
        super.onResume();
        //及时更新goodsnumber
        getGoodNumber();
    }

    //购买按钮事件
    void goOrder() {
        List<Car> buyCar = new ArrayList<>();
        Car car = new Car();
        if (Integer.valueOf(goodsBuyNumber.getText().toString()).intValue() <= 0) {
            new AlertDialog.Builder(GoodsBuyActivity.this)
                    .setTitle("ERROR")
                    .setMessage("购买数量错误!")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }
        car.setId(0);
        car.setBuyNumber(Integer.valueOf(goodsBuyNumber.getText().toString()).intValue());
        car.setChoice(null);
        car.setCreateDate(new Date());
        car.setCustomerId(null);
        Log.d("GoodsBuyActivity++", "this is ++" + car.getCustomerId());
        car.setEditDate(null);
        car.setGoods(goods);
        buyCar.add(car);
        //
        if (buyCar.size() > 0) {
            Intent intent = new Intent(GoodsBuyActivity.this, GoodsOrderActivity.class);
            intent.putExtra("car", (Serializable) buyCar);
            startActivity(intent);
        }
    }

    void addCart() {
        if (Integer.valueOf(goodsBuyNumber.getText().toString()).intValue() <= 0&&Integer.valueOf(goodsNumber.getText().toString())>0) {
            new AlertDialog.Builder(GoodsBuyActivity.this)
                    .setTitle("ERROR")
                    .setMessage("购买数量错误!")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }

        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("goodsId", goods.getId().toString())
                .addFormDataPart("buyNumber", goodsBuyNumber.getText().toString())
                .build();
        Request request = Server.requestBuildWithMall("shop/goods/addCart")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsBuyActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Boolean succeed = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                    if (succeed) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoodsBuyActivity.this, "添加购物车成功!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoodsBuyActivity.this, "已添加进购物车，请不要重复添加！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsBuyActivity.this, "添加购物车失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    void getGoodNumber() {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("goodId", goods.getId().toString())
                .build();
        Request request = Server.requestBuildWithMall("getGoods")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsBuyActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Goods currentGoods = new ObjectMapper().readValue(response.body().string(), Goods.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //更新number
                            goodsNumber.setText("库存:" + currentGoods.getGoodsNumber());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
