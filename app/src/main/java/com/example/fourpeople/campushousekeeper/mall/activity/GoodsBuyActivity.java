package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;

import java.text.SimpleDateFormat;

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
        goodsAddCart = (Button) findViewById(R.id.goodsBuy_addCart);
        goodsBuy = (Button) findViewById(R.id.goodsBuy_buy);
        //
        commentList = (ListView) findViewById(R.id.goodsBuy_commentList);
        goodsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        goodsNumber.setText("库存:" + goods.getGoodsNumber());

        super.onResume();
    }
}
