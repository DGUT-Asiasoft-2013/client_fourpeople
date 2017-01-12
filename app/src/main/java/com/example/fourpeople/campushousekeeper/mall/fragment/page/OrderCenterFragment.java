package com.example.fourpeople.campushousekeeper.mall.fragment.page;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Administrator on 2016/12/21.
 */

public class OrderCenterFragment extends Fragment {
    View view;
    ListView myOrderList;
    List<MyOrder> myOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mall_fragment_ordercenter, null);
        }
        myOrderList = (ListView) view.findViewById(R.id.myOrder_list);
        myOrderList.setAdapter(baseAdapter);
        return view;
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
                view = inflater.inflate(R.layout.mall_fragment_ordercenter_list, null);
            }
            TextView state = (TextView) view.findViewById(R.id.myOrder_state);
            TextView dateId = (TextView) view.findViewById(R.id.myOrder__dateId);
            GoodsAvatar goodsAvatar = (GoodsAvatar) view.findViewById(R.id.myOrder_avatar);
            TextView goodsName = (TextView) view.findViewById(R.id.myOrder_name);
            TextView goodsDate = (TextView) view.findViewById(R.id.myOrder_date);
            TextView goodsPiece = (TextView) view.findViewById(R.id.myOrder_piece);
            TextView goodsNumber = (TextView) view.findViewById(R.id.myOrder_number);
            TextView number = (TextView) view.findViewById(R.id.myOrder_goodsNumber);
            TextView money = (TextView) view.findViewById(R.id.myOrder_money);
            TextView address = (TextView) view.findViewById(R.id.myOrder_address);
            Button delete = (Button) view.findViewById(R.id.myOrder_delete);
            Button comment = (Button) view.findViewById(R.id.myOrder_comment);
            //
            final MyOrder currentOrder = myOrder.get(i);
            Goods goods = currentOrder.getGoods();
            //
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
            dateId.setText("   订单编号:" + new SimpleDateFormat("yyyyMMddhhmmss").format(currentOrder.getCreateDate()));
            goodsAvatar.load(goods.getGoodsAvatar());
            goodsName.setText(goods.getGoodsName() + "  " + goods.getGoodsAbout());
            goodsDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentOrder.getCreateDate()));
            goodsPiece.setText("￥" + goods.getGoodsPiece());
            goodsNumber.setText("×" + String.valueOf(currentOrder.getBuyNumber()));
            number.setText("共" + String.valueOf(currentOrder.getBuyNumber()) + "件商品");
            money.setText("合计:￥" + currentOrder.getMoney());
            address.setText("地址:" + currentOrder.getUser().getAddress() + "  联系方式:" + currentOrder.getUser().getTel());
            if (!currentOrder.getOver() && currentOrder.getOrderState() < 3) {
                delete.setText("取消订单");
            } else if (currentOrder.getOver()) {
                delete.setText("删除订单");
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
                        Toast.makeText(getActivity(), "请联系买家取消订单!", Toast.LENGTH_SHORT).show();
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                        //删除订单按钮（已评价）
                        new AlertDialog.Builder(getActivity())
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
                        Toast.makeText(getActivity(), "正在等待订单被评价，不能删除！", Toast.LENGTH_SHORT).show();
                    } else if (currentOrder.getOver()) {
                        //删除订单按钮（已取消）
                        new AlertDialog.Builder(getActivity())
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
                    } else {
                        Toast.makeText(getActivity(), "删除按钮未知错误!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //
            if (!currentOrder.getOver() && currentOrder.getOrderState() == 1) {
                comment.setText("发货");
            } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 2) {
                comment.setText("已发货");
            } else {
                comment.setText("已完成");
            }
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!currentOrder.getOver() && currentOrder.getOrderState() < 2) {
                        //发货按钮事件
                        change(currentOrder.getId(), currentOrder.getOver(), currentOrder.getCommentState(), 2);
                        //
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 2) {
                        Toast.makeText(getActivity(), "请耐心等待买家收货！", Toast.LENGTH_SHORT).show();
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && !currentOrder.getCommentState()) {
                        Toast.makeText(getActivity(), "买家已收货，等待评价", Toast.LENGTH_SHORT).show();
                    } else if (!currentOrder.getOver() && currentOrder.getOrderState() == 3 && currentOrder.getCommentState()) {
                        Toast.makeText(getActivity(), "买家已评价！", Toast.LENGTH_SHORT).show();
                    } else if (currentOrder.getOver()) {
                        Toast.makeText(getActivity(), "订单已经取消，不能进行操作", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "收货按钮异常错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    };

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
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Boolean succssed = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (succssed) {
                                Toast.makeText(getActivity(), "删除成功...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "删除失败...", Toast.LENGTH_SHORT).show();
                            }
                            baseAdapter.notifyDataSetInvalidated();
                        }
                    });
                    getMessage();
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
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

    @Override
    public void onResume() {
        super.onResume();
        getMessage();
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("getShopOrder")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    myOrder = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<MyOrder>>() {
                    });
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapter.notifyDataSetInvalidated();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
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

    void change(Integer overId, Boolean over, Boolean commentState, int orderState) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("who", "shop")
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
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络挂了...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    myOrder = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<MyOrder>>() {
                    });
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapter.notifyDataSetInvalidated();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
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
