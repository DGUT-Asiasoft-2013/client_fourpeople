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
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/29.
 */

public class GoodsShowActivity extends Activity {
    Mall mall;
    TextView titleText, backBtn;
    ListView goodsShowList;
    List<Goods> goodsData;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_goodsshow);
        //接受传送的信息
        mall = (Mall) getIntent().getSerializableExtra("mall");
        titleText = (TextView) findViewById(R.id.goodsShow_text);
        backBtn = (TextView) findViewById(R.id.goodsShow_back);
        goodsShowList = (ListView) findViewById(R.id.goodsShow__lisView);
        goodsShowList.setAdapter(baseAdapter);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        goodsShowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onGoodsItemClick(i);
            }
        });
    }

    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return goodsData == null ? 0 : goodsData.size();
        }

        @Override
        public Object getItem(int i) {
            return goodsData.get(i);
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
            Goods goods = goodsData.get(i);

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
        titleText.setText(mall.getUser().getName());
        getMessage();
        super.onResume();
    }

    void getMessage() {
        MultipartBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("id", mall.getId().toString())
                .build();
        Request request = Server.requestBuildWithMall("shop/goods/show")
                .method("post", null)
                .post(requestBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsShowActivity.this, "网络挂了...", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Page<Goods> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Goods>>() {
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GoodsShowActivity.this.goodsData = data.getContent();
                            GoodsShowActivity.this.page = data.getNumber();
                            System.out.println(GoodsShowActivity.this.page);
                            baseAdapter.notifyDataSetInvalidated();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsShowActivity.this, "显示错误...", Toast.LENGTH_SHORT);
                        }
                    });
                }

            }
        });
    }

    void onGoodsItemClick(int i) {
        Intent intent = new Intent(GoodsShowActivity.this, GoodsBuyActivity.class);
        Goods goods = goodsData.get(i);
        intent.putExtra("goods", goods);
        startActivity(intent);
    }
}
