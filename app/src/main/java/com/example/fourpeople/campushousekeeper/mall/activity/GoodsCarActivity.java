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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Car;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GoodsCarActivity extends Activity {
    ListView goodsCarList;
    CheckBox goodsCarCheckBox;
    Button goodsCarBuy;
    TextView goodsCarMoney;
    double money = 0;
    List<Car> car = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_goodscar);
        goodsCarList = (ListView) findViewById(R.id.goodsCar_goodsList);
        goodsCarCheckBox = (CheckBox) findViewById(R.id.goodsCar_choiceAll);
        goodsCarBuy = (Button) findViewById(R.id.goodsCar_buy);
        goodsCarMoney = (TextView) findViewById(R.id.goodsCar_money);
        goodsCarList.setAdapter(baseAdapter);
        //全选按钮事件
        goodsCarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (goodsCarCheckBox.isChecked()) {
                    if (car != null) {
                        for (int i = 0; i < car.size(); i++) {
                            car.get(i).setChoice(true);
                        }
                    }
                } else {
                    if (car != null) {
                        for (int i = 0; i < car.size(); i++) {
                            car.get(i).setChoice(false);
                        }
                    }
                }
                setMoney();
                baseAdapter.notifyDataSetInvalidated();
            }
        });
        goodsCarBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(GoodsCarActivity.this)
                        .setTitle("提示")
                        .setMessage("是否确认下单？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Car> buyCar = new ArrayList<>();
                                for (int n = 0; n < car.size(); n++) {
                                    if (car.get(n).getChoice()) {
                                        buyCar.add(car.get(n));
                                    }
                                }
                                if (buyCar.size() > 0) {
                                    Intent intent = new Intent(GoodsCarActivity.this, GoodsOrderActivity.class);
                                    intent.putExtra("car", (Serializable) buyCar);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(GoodsCarActivity.this, "订单内容为空，请选择商品！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", null)
                        .show();

            }
        });
    }

    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return car == null ? 0 : car.size();
        }

        @Override
        public Object getItem(int i) {
            return car.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.mall_list_goodscar, null);
            }
            TextView goodsCarMall = (TextView) view.findViewById(R.id.goodsCarList_mallName);
            TextView goodsCarDelete = (TextView) view.findViewById(R.id.goodsCarList_delete);
            //
            final CheckBox goodsCarCheck = (CheckBox) view.findViewById(R.id.goodsCarList_checkBox);
            GoodsAvatar goodsCarAvatar = (GoodsAvatar) view.findViewById(R.id.goodsCarList_goodsAvatar);
            //
            TextView goodsCarName = (TextView) view.findViewById(R.id.goodsCarList_goodsName);
            TextView goodsCarAbout = (TextView) view.findViewById(R.id.goodsCarList_goodsAbout);
            TextView goodsCarDate = (TextView) view.findViewById(R.id.goodsCarList_goodsDate);
            //
            TextView goodsCarPiece = (TextView) view.findViewById(R.id.goodsCarList_goodsPiece);
            TextView goodsCarNumber = (TextView) view.findViewById(R.id.goodsCarList_goodsNumber);
            TextView goodsCarAdd = (TextView) view.findViewById(R.id.goodsCarList_add);
            final TextView goodsCarBuyNumber = (TextView) view.findViewById(R.id.goodsCarList_buyNumber);
            TextView goodsCarReduce = (TextView) view.findViewById(R.id.goodsCarList_reduce);
            //
            final Car currentCar = car.get(i);
            Log.d("car", "getView: " + currentCar);
            goodsCarMall.setText("店铺:"
                    + currentCar.getGoods().getMall().getShopName());
            goodsCarAvatar.load(currentCar.getGoods().getGoodsAvatar());
            goodsCarName.setText(currentCar.getGoods().getGoodsName());
            goodsCarAbout.setText(currentCar.getGoods().getGoodsAbout());
            goodsCarDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentCar.getCreateDate()));
            goodsCarPiece.setText("价格:￥" + currentCar.getGoods().getGoodsPiece());
            goodsCarNumber.setText("库存:" + currentCar.getGoods().getGoodsNumber());
            //当前购买数量设置
            if (Integer.valueOf(currentCar.getBuyNumber()).intValue()
                    <= Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue()
                    && Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue() > 1) {
                goodsCarBuyNumber.setText(String.valueOf(currentCar.getBuyNumber()));
            } else if (Integer.valueOf(currentCar.getBuyNumber()).intValue()
                    > Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue()
                    && Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue() > 1) {
                goodsCarBuyNumber.setText("1");
                currentCar.setBuyNumber(1);
            } else if (Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue() == 1) {
                goodsCarBuyNumber.setText("1");
                currentCar.setBuyNumber(1);
            } else {
                goodsCarBuyNumber.setText("0");
                currentCar.setBuyNumber(0);
            }

            //设置是否选中
            if (currentCar.getChoice()) {
                goodsCarCheck.setChecked(true);
            } else {
                goodsCarCheck.setChecked(false);
            }
            //加号事件
            goodsCarAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(goodsCarBuyNumber.getText().toString()).intValue()
                            < Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue()) {
                        int i = Integer.valueOf(goodsCarBuyNumber.getText().toString()).intValue() + 1;
                        goodsCarBuyNumber.setText(String.valueOf(i));
                        currentCar.setBuyNumber(i);
                    } else if (Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue() <= 0) {
                        goodsCarBuyNumber.setText(String.valueOf(0));
                        currentCar.setBuyNumber(0);
                    } else {
                        Toast.makeText(GoodsCarActivity.this, "已达到最大购买数量!", Toast.LENGTH_SHORT).show();
                    }
                    setMoney();
                }
            });
            //减号事件
            goodsCarReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(goodsCarBuyNumber.getText().toString()).intValue()
                            > 1 && Integer.valueOf(currentCar.getGoods().getGoodsNumber().toString()).intValue() > 1) {
                        int i = Integer.valueOf(goodsCarBuyNumber.getText().toString()).intValue() - 1;
                        goodsCarBuyNumber.setText(String.valueOf(i));
                        currentCar.setBuyNumber(i);
                        setMoney();
                    }
                }
            });
            //删除对应购物车商品
            goodsCarDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCar(currentCar.getId());
                }
            });
            //选中按钮事件
            goodsCarCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (goodsCarCheck.isChecked()) {
                        currentCar.setChoice(true);
                        setMoney();
                    } else {
                        currentCar.setChoice(false);
                        setMoney();
                    }
                }
            });
            return view;
        }
    };

    //删除对应购物车商品
    void deleteCar(Integer id) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("carId", String.valueOf(id))
                .build();
        Log.d("deleteCar", "getView" + String.valueOf(id));
        Request request = Server.requestBuildWithMall("shop/goods/deleteCart")
                .post(multipartBody)
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
                try {
                    Boolean succeed = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                    if (succeed) {
                        getMessage();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoodsCarActivity.this, "删除出错...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(GoodsCarActivity.this)
                                        .setTitle("ERROR")
                                        .setMessage(e.getMessage())
                                        .setNegativeButton("OK", null)
                                        .show();
                            }
                        });
                    }
                }
            }
        });
    }

    void setMoney() {
        money = 0.0;
        if (car != null) {
            for (int i = 0; i < car.size(); i++) {
                if (car.get(i).getChoice()) {
                    money = money + Double.valueOf(car.get(i).getGoods().getGoodsPiece())
                            * Double.valueOf(car.get(i).getBuyNumber());
                }
            }
            goodsCarMoney.setText("总金额:" + String.valueOf(money));
        } else {
            goodsCarMoney.setText("总金额:" + String.valueOf(money));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
        setMoney();
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("shop/goods/getCart")
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
                try {
                    car = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<Car>>() {
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapter.notifyDataSetInvalidated();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(GoodsCarActivity.this)
                                    .setTitle("ERROR")
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
