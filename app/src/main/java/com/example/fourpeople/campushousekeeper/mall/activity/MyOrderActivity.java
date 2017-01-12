package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.api.MyOrder;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.core.type.TypeReference;
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
 * Created by Administrator on 2017/1/6.
 */

public class MyOrderActivity extends Activity {
    TextView backBtn;
    ListView myOrderList;
    List<MyOrder> myOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_myorder);
        backBtn = (TextView) findViewById(R.id.myOrder_back);
        myOrderList = (ListView) findViewById(R.id.myOrder_list);
        myOrderList.setAdapter(baseAdapter);
        //返回按钮
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myOrderList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyOrderActivity.this, GoodsBuyActivity.class);
                Goods goods = myOrder.get(i).getGoods();
                intent.putExtra("goods", goods);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return myOrder == null ? 0 : myOrder.size();
        }

        @Override
        public Object getItem(int i) {
            return myOrder.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.mall_myorder_listview, null);
            }
            TextView mallName = (TextView) view.findViewById(R.id.myOrder_mallName);
            TextView state = (TextView) view.findViewById(R.id.myOrder_state);
            GoodsAvatar goodsAvatar = (GoodsAvatar) view.findViewById(R.id.myOrder_avatar);
            TextView goodsName = (TextView) view.findViewById(R.id.myOrder_name);
            TextView goodsDate = (TextView) view.findViewById(R.id.myOrder_date);
            TextView goodsPiece = (TextView) view.findViewById(R.id.myOrder_piece);
            TextView goodsNumber = (TextView) view.findViewById(R.id.myOrder_number);
            TextView number = (TextView) view.findViewById(R.id.myOrder_goodsNumber);
            TextView money = (TextView) view.findViewById(R.id.myOrder_money);
            Button delete = (Button) view.findViewById(R.id.myOrder_delete);
            Button comment = (Button) view.findViewById(R.id.myOrder_comment);
            //
            final MyOrder currentOrder = myOrder.get(i);
            Goods goods = currentOrder.getGoods();
            //
            mallName.setText(goods.getMall().getShopName());
            if (!currentOrder.getOver() && currentOrder.getOrderState() == 1) {
                state.setText("待发货");
            } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 2) {
                state.setText("待收货");
            } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && !currentOrder.getCommentState()) {
                state.setText("待评价");
            } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                state.setText("已完成");
            } else if (currentOrder.getOver()) {
                state.setText("订单已取消");
            } else {
                state.setText("未知错误");
            }
            goodsAvatar.load(goods.getGoodsAvatar());
            goodsName.setText(goods.getGoodsName() + "  " + goods.getGoodsAbout());
            goodsDate.setText("   订单编号:" + new SimpleDateFormat("yyyyMMddhhmmss").format(currentOrder.getCreateDate()));
            goodsPiece.setText("￥" + goods.getGoodsPiece());
            goodsNumber.setText("×" + String.valueOf(currentOrder.getBuyNumber()));
            number.setText("共" + String.valueOf(currentOrder.getBuyNumber()) + "件商品");
            money.setText("合计:￥" + currentOrder.getMoney());
            if (!currentOrder.getOver() && currentOrder.getOrderState() < 3) {
                delete.setText("取消订单");
            } else if (currentOrder.getOver()) {
                delete.setText("订单已取消");
            } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && !currentOrder.getCommentState()) {
                delete.setText("删除订单");
            } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                delete.setText("删除订单");
            } else {
                delete.setText("未知错误");
            }
            //，取消,删除按钮事件
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!currentOrder.getOver() && currentOrder.getOrderState() < 3) {
                        //进行取消按钮事件
                        new AlertDialog.Builder(MyOrderActivity.this)
                                .setTitle("提示")
                                .setMessage("你确定要取消订单？取消后将关闭交易!")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        cancel(currentOrder.getId());
                                    }
                                })
                                .setPositiveButton("取消", null)
                                .show();
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                        //删除订单按钮（已评价）
                        new AlertDialog.Builder(MyOrderActivity.this)
                                .setTitle("提示")
                                .setMessage("你确定要删除订单？删除后将清除信息!")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        delete(currentOrder.getId());
                                    }
                                })
                                .setPositiveButton("取消", null)
                                .show();
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && !currentOrder.getCommentState()) {
                        Toast.makeText(MyOrderActivity.this, "订单未完成，不能删除！", Toast.LENGTH_SHORT).show();
                    } else if (currentOrder.getOver()) {
                        Toast.makeText(MyOrderActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyOrderActivity.this, "删除按钮未知错误!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //
            if (currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                comment.setText("已评价");
            } else if (currentOrder.getOrderState() == 3 && !currentOrder.getCommentState()) {
                comment.setText("评价");
            } else {
                comment.setText("收货");
            }
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!currentOrder.getOver() && currentOrder.getOrderState() < 2) {
                        Toast.makeText(MyOrderActivity.this, "请耐心等待店家发货！", Toast.LENGTH_SHORT).show();
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 2) {
                        //收货功能按钮
                        new AlertDialog.Builder(MyOrderActivity.this)
                                .setTitle("提示")
                                .setMessage("确认收货？收货钱财将汇到对方")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        change(currentOrder.getId(), false, false, 3);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();

                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && !currentOrder.getCommentState()) {
                        //评论按钮功能
                        Intent intent = new Intent(MyOrderActivity.this, GoodsCommentActivity.class);
                        intent.putExtra("currentOrder", currentOrder);
                        startActivity(intent);
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                        Toast.makeText(MyOrderActivity.this, "你已评价，谢谢你的购买！", Toast.LENGTH_SHORT).show();
                    } else if (currentOrder.getOver()) {
                        Toast.makeText(MyOrderActivity.this, "订单已经取消，不能进行操作", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyOrderActivity.this, "评论按钮异常错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    //取消订单按钮事件
    void cancel(Integer id) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("orderId", String.valueOf(id))
                .build();
        Request request = Server.requestBuildWithMall("cancelOrder")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyOrderActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    MyOrder myOrder = new ObjectMapper().readValue(response.body().string(), MyOrder.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapter.notifyDataSetInvalidated();
                        }
                    });
                    getMessage();
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(MyOrderActivity.this)
                                    .setTitle("cancel Error")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    //删除订单按钮（已评价）
    void delete(Integer id) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("orderId", String.valueOf(id))
                .build();
        Request request = Server.requestBuildWithMall("deleteOrder")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyOrderActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Boolean succssed = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (succssed) {
                                Toast.makeText(MyOrderActivity.this, "删除成功...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyOrderActivity.this, "删除失败...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    getMessage();
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(MyOrderActivity.this)
                                    .setTitle("delete Error")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("getCustomerOrder")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyOrderActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    myOrder = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<MyOrder>>() {
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
                            new AlertDialog.Builder(MyOrderActivity.this)
                                    .setTitle("MyOrder Error")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    void change(Integer overId, Boolean over, Boolean commentState, int orderState) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("who", "customer")
                .addFormDataPart("overId", String.valueOf(overId))
                .addFormDataPart("over", String.valueOf(over))
                .addFormDataPart("commentState", String.valueOf(commentState))
                .addFormDataPart("orderState", String.valueOf(orderState))
                .build();
        Request request = Server.requestBuildWithMall("updateOrder")
                .post(multipartBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyOrderActivity.this, "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    myOrder = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<MyOrder>>() {
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
                            new AlertDialog.Builder(MyOrderActivity.this)
                                    .setTitle("OrderCenter Error")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
