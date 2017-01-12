package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.api.Mall;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.AvatarDispose;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/10.
 */

public class MyFavoriteActivity extends Activity {
    TextView backBtn, shopBtn, goodsBtn;
    ListView list;
    List<Mall> mallList;
    List<Goods> goodsList;
    Boolean choice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_myfavorite);
        backBtn = (TextView) findViewById(R.id.myFavorite_back);
        shopBtn = (TextView) findViewById(R.id.myFavorite_shop);
        goodsBtn = (TextView) findViewById(R.id.myFavorite_goods);
        list = (ListView) findViewById(R.id.myFavorite_list);
        list.setAdapter(baseAdapterMall);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setAdapter(baseAdapterMall);
                getMallMessage();
                choice = false;
            }
        });
        goodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setAdapter(baseAdapterGoods);
                getGoodsMessage();
                choice = true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!choice){
                    Intent intent = new Intent(MyFavoriteActivity.this, GoodsShowActivity.class);
                    Mall mall = mallList.get(i);
                    intent.putExtra("mall", mall);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MyFavoriteActivity.this, GoodsBuyActivity.class);
                    Goods goods = goodsList.get(i);
                    intent.putExtra("goods", goods);
                    startActivity(intent);
                }
            }
        });
    }

    BaseAdapter baseAdapterMall = new BaseAdapter() {
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
    protected void onResume() {
        super.onResume();
        getMallMessage();
    }

    void getMallMessage() {
        Request request = Server.requestBuildWithMall("shop/favorite")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyFavoriteActivity.this, "网络挂了，宝宝连不上...", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    mallList = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<Mall>>() {
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapterMall.notifyDataSetInvalidated();//更新视图
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getGoodsMessage() {
        Request request = Server.requestBuildWithMall("goods/favorite")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyFavoriteActivity.this, "网络挂了，宝宝连不上...", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    goodsList = new ObjectMapper().readValue(response.body().string(), new TypeReference<List<Goods>>() {
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapterGoods.notifyDataSetInvalidated();//更新视图
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
