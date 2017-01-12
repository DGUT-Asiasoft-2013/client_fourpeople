package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.api.Mall;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.activity.GoodsBuyActivity;
import com.example.fourpeople.campushousekeeper.mall.activity.GoodsShowActivity;
import com.example.fourpeople.campushousekeeper.mall.view.AvatarDispose;
import com.example.fourpeople.campushousekeeper.mall.view.Closeed;
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
 * Created by Administrator on 2016/12/19.
 */

public class MallFragment extends Fragment {
    LinearLayout fragmentMall;//定义Mall界面的变量
    View view;
    Spinner mallSpinner;//下拉框选择器
    ListView mallListView;//列表
    Button defaultBtn, creditBtn, okBtn;//默认排序和信用排序按钮
    //数据
    List<Mall> mallList;
    List<Goods> goodsList;
    int page = 0;
    int choice = 0;//选择排序方式
    String spinner;//下拉框选择的内容
    EditText editText;//搜索输入内容
    Boolean show = false;//判断当前显示店铺还是商品

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mall, null);
        }
        //关联对应的布局,设置监听事件关闭小键盘
        fragmentMall = (LinearLayout) view.findViewById(R.id.fragment_mall);
        fragmentMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭小键盘
                Closeed.onCloseClick(getActivity());
            }
        });
        //列表设置
        mallListView = (ListView) view.findViewById(R.id.fragment_mall_listView);
        mallListView.setAdapter(baseAdapter);
        //控件连接
        mallSpinner = (Spinner) view.findViewById(R.id.fragment_mall_spinner);
        defaultBtn = (Button) view.findViewById(R.id.fragment_mall_defaultBtn);
        creditBtn = (Button) view.findViewById(R.id.fragment_mall_creditBtn);
        okBtn = (Button) view.findViewById(R.id.fragment_mall_ok);
        editText = (EditText) view.findViewById(R.id.fragment_mall_editText);

        //商店列表按钮事件
        mallListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (show) {
                    onGoodsItemClick(i);
                } else {
                    onShopItemClick(i);
                }
            }
        });
        defaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show = false;
                choice = 0;
                mallListView.setAdapter(baseAdapter);
                getMessage();
            }
        });
        creditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show = false;
                choice = 1;
                mallListView.setAdapter(baseAdapter);
                getMessage();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okOnClickListener();
            }
        });
        mallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner = "商品";
            }
        });
        return view;
    }

    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mallList == null ? 0 : mallList.size();
        }

        @Override
        public Object getItem(int i) {
            return mallList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.mall_fragment_mallfragment_malllist, null);
            }
            AvatarDispose shopAvatar = (AvatarDispose) view.findViewById(R.id.mallFragment_mallList_avatar);
            TextView shopName = (TextView) view.findViewById(R.id.mallFragment_mallList_name);
            TextView shopType = (TextView) view.findViewById(R.id.mallFragment_mallList_type);
            TextView shopAbout = (TextView) view.findViewById(R.id.mallFragment_mallList_about);
            TextView shopUser = (TextView) view.findViewById(R.id.mallFragment_mallList_user);
            TextView shopDate = (TextView) view.findViewById(R.id.mallFragment_mallList_date);
            TextView shopLiked = (TextView) view.findViewById(R.id.mallFragment_mallList_like);
            Mall mall = mallList.get(i);
            shopAvatar.load(mall.getShopAvatar());
            shopName.setText(mall.getShopName());
            shopType.setText(mall.getShopType());
            shopAbout.setText(mall.getShopAbout());
            shopUser.setText("店家:" + mall.getUser().getName());
            shopDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mall.getCreateDate()));
            shopLiked.setText("收藏(" + mall.getShopLiked() + ")");
            return view;
        }
    };

    BaseAdapter baseAdapterGoods = new BaseAdapter() {
        @Override
        public int getCount() {
            return goodsList == null ? 0 : goodsList.size();
        }

        @Override
        public Object getItem(int i) {
            return goodsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.mall_fragment_goodslist_show, null);
            }
            TextView goodsName = (TextView) view.findViewById(R.id.goodsList_goodsName);
            TextView goodsPiece = (TextView) view.findViewById(R.id.goodsList_goodsPiece);
            TextView goodsAbout = (TextView) view.findViewById(R.id.goodsList_goodsAbout);
            TextView goodsNumber = (TextView) view.findViewById(R.id.goodsList_goodsNumber);
            GoodsAvatar goodsAvatar = (GoodsAvatar) view.findViewById(R.id.goodsList_goodsAvatar);
            // ImageView goodsAvatar=(ImageView)view.findViewById(R.id.goodsList_goodsAvatar);
            TextView goodsLiked = (TextView) view.findViewById(R.id.goodsList_like);
            Goods goods = goodsList.get(i);

            // goodsAvatar.setImageBitmap(load(goods.getGoodsAvatar()));
            goodsAvatar.load(goods.getGoodsAvatar());
            goodsName.setText(goods.getGoodsName());
            goodsAbout.setText("备注：" + goods.getGoodsAbout());
            goodsPiece.setText("￥ " + goods.getGoodsPiece());
            goodsNumber.setText("货存：" + goods.getGoodsNumber());
            goodsLiked.setText("收藏(" + goods.getGoodsLiked() + ")");
            return view;
        }
    };

    @Override
    public void onResume() {
        getMessage();
        super.onResume();
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("shop/" + choice + "/show")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络挂了，宝宝连不上...", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Page<Mall> mallData = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Mall>>() {
                    });
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mallList = mallData.getContent();
                            baseAdapter.notifyDataSetInvalidated();//更新视图
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*//加载更多按钮事件
    void loadmore() {
        getMoreView.setEnabled(false);
        getMoreBtn.setText("载入中…");

        Request request = Server.requestBuildWithMall("shop/" + choice + "/show/" + (page + 1)).get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getMoreView.setEnabled(true);
                        getMoreBtn.setText("加载更多");
                        try {
                            final Page<Mall> mallData = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Mall>>() {
                            });
                            if (mallData.getNumber() > page) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (mallData == null) {
                                            mallList = mallData.getContent();
                                        } else {
                                            mallList.addAll(mallData.getContent());
                                        }
                                        page = mallData.getNumber();
                                        baseAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getMoreView.setEnabled(true);
                        getMoreBtn.setText("加载更多");
                    }
                });
            }
        });
    }*/

    void onShopItemClick(int i) {
        Intent intent = new Intent(getActivity(), GoodsShowActivity.class);
        Mall mall = mallList.get(i);
        intent.putExtra("mall", mall);
        startActivity(intent);
    }

    void onGoodsItemClick(int i) {
        Intent intent = new Intent(getActivity(), GoodsBuyActivity.class);
        Goods goods = goodsList.get(i);
        intent.putExtra("goods", goods);
        startActivity(intent);
    }

    void okOnClickListener() {
        String edit = editText.getText().toString();
        if (edit.equals("")) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("请输入要搜索的内容！")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        if (spinner.equals("店铺")) {
            show = false;
            mallListView.setAdapter(baseAdapter);
        } else if (spinner.equals("商品")) {
            show = true;
            mallListView.setAdapter(baseAdapterGoods);
        }
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("edit", edit)
                .build();
        Request request = null;

        if (spinner.equals("商品")) {
            request = Server.requestBuildWithMall("getSearchGoods")
                    .post(multipartBody)
                    .build();
        } else if (spinner.equals("店铺")) {
            request = Server.requestBuildWithMall("getSearchShop")
                    .post(multipartBody)
                    .build();
        }
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络挂了，宝宝连不上...", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (spinner.equals("店铺")) {
                    try {
                        mallList = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<Mall>>() {
                        });
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                baseAdapter.notifyDataSetInvalidated();
                            }
                        });
                    } catch (final Exception e) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("error")
                                        .setMessage(e.getMessage())
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });
                    }
                } else {
                    try {
                        goodsList = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<Goods>>() {
                        });
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                baseAdapterGoods.notifyDataSetInvalidated();
                            }
                        });
                    } catch (final Exception e) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("error")
                                        .setMessage(e.getMessage())
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });
                    }
                }
            }
        });
    }

}
