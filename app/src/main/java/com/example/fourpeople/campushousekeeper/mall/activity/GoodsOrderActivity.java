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
import com.example.fourpeople.campushousekeeper.mall.view.Closeed;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
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
    Double money = 0.0;

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
                money = money + Double.valueOf(buyCar.get(i).getGoods().getGoodsPiece())
                        * Double.valueOf(buyCar.get(i).getBuyNumber());
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
                //验证地址电话
                if (currentUser.getAddress().equals("") || currentUser.getTel().equals("")) {
                    Toast.makeText(GoodsOrderActivity.this, "支付失败，请先完善配送资料!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //验证支付密码
                LayoutInflater inflater = LayoutInflater.from(GoodsOrderActivity.this);
                final EditText editText = (EditText) inflater.inflate(R.layout.mall_edit, null);
                new AlertDialog.Builder(GoodsOrderActivity.this)
                        .setTitle("请输入支付密码：")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //关闭小键盘
                                Closeed.onCloseClick(GoodsOrderActivity.this);
                                //
                                String password = editText.getText().toString();
                                if (MD5.getMD5(password).equals(currentUser.getPasswordHash())) {
                                    //验证购买是否为空
                                    if (buyCar != null) {
                                        //验证余额够不够
                                        Log.d("getmoney1", "!!!!!!!!!" + money);
                                        Log.d("getmoney2", "!!!!!!!!!" + currentUser.getBalance());
                                        Log.d("getmoney3", "!!!!!!!!!" + Double.valueOf(currentUser.getBalance()));
                                        Log.d("getmoney4", "!!!!!!!!!" + currentUser);
                                        if (money <= Double.valueOf(currentUser.getBalance()) && money > 0) {
                                            for (int n = 0; n < buyCar.size(); n++) {
                                                //一个个地下单
                                                buyGoods(n);
                                            }
                                        } else {
                                            Toast.makeText(GoodsOrderActivity.this, "余额不足，支付失败，请及时充值!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(GoodsOrderActivity.this, "购买内容为空，支付失败", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(GoodsOrderActivity.this, "密码错误，支付失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    void buyGoods(final int n) {
        final Car car = buyCar.get(n);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("carId", car.getId().toString())
                .addFormDataPart("money", String.valueOf(Integer.valueOf(car.getGoods().getGoodsPiece()).intValue() * Integer.valueOf(car.getBuyNumber()).intValue()))
                .addFormDataPart("goodsBuyNumber", String.valueOf(car.getBuyNumber()))
                .addFormDataPart("goodsId", car.getGoods().getId().toString())
                .build();
        Request request = Server.requestBuildWithMall("shop/goods/addOrder")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsOrderActivity.this, "网络错误，支付失败了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final int number = new ObjectMapper().readValue(response.body().string(), int.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (number == 0) {
                                Toast.makeText(GoodsOrderActivity.this, car.getGoods().getGoodsName() + " 该商品数量不够,该商品支付失败...", Toast.LENGTH_SHORT).show();
                            } else if (number == 1) {
                                Toast.makeText(GoodsOrderActivity.this, currentUser.getName() + " 你的余额不足以购买 " + car.getGoods().getGoodsName() + " ,该商品支付失败...", Toast.LENGTH_SHORT).show();
                            } else if (number == 2) {
                                Toast.makeText(GoodsOrderActivity.this, "老板不买你,支付失败了...", Toast.LENGTH_SHORT).show();
                            } else if (number == 3) {
                                //支付成功
                                //确认是最后的一件
                                if (n == buyCar.size() - 1) {
                                    Toast.makeText(GoodsOrderActivity.this, "下单成功!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                Toast.makeText(GoodsOrderActivity.this, "未知错误，我也不知道怎么了...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(GoodsOrderActivity.this)
                                    .setTitle("Order ERROR")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    });
                }
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
                    + String.valueOf(Double.valueOf(car.getGoods().getGoodsPiece()) * Double.valueOf(car.getBuyNumber())));
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
