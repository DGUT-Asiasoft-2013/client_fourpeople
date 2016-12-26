package com.example.fourpeople.campushousekeeper.mall.page;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Goods;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class GoodsListFragment extends Fragment {
    View view;
    ListView goodsList;
    //加载更多视图与按钮
    View getMoreView;
    TextView getMoreBtn;
    //
    List<Goods> goodsData;
    int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mall_fragment_goodslist, null);
            //获取当前布局控件
            goodsList = (ListView) view.findViewById(R.id.goodsList_lisView);
            //获取加载更多视图和对应TextView
            getMoreView = inflater.inflate(R.layout.mall_button_get_more, null);
            getMoreBtn = (TextView) getMoreView.findViewById(R.id.btn_get_more);
            //加载视图
            goodsList.addFooterView(getMoreView);
            goodsList.setAdapter(baseAdapter);
            goodsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            getMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        return view;
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
            //AvatarDispose goodsAvatar = (AvatarDispose) view.findViewById(R.id.goodsList_goodsAvatar);
            ImageView goodsAvatar=(ImageView)view.findViewById(R.id.goodsList_goodsAvatar);
            Goods goods = goodsData.get(i);

            goodsAvatar.setImageBitmap(load(goods.getGoodsAvatar()));
            //goodsAvatar.load(goods.getGoodsAvatar());
            goodsName.setText(goods.getGoodsName());
            goodsAbout.setText("备注：" + goods.getGoodsAbout());
            goodsPiece.setText("￥ " + goods.getGoodsPiece());
            goodsNumber.setText("货存："+goods.getGoodsNumber());
            return view;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getGoodsMessage();
    }

    //获取显示信息
    void getGoodsMessage() {
        Request request = Server.requestBuildWithMall("goods/show")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity()==null)return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("网络连接失败...")
                                .setNegativeButton("OK", null)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final Page<Goods> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Goods>>() {
                    });
                    //跑到主线程
                    if (getActivity()==null)return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GoodsListFragment.this.goodsData = data.getContent();
                            GoodsListFragment.this.page = data.getNumber();
                            baseAdapter.notifyDataSetInvalidated();//更新视图
                        }
                    });

                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("提示")
                                    .setMessage("读取数据出错..." + e.getMessage())
                                    .setNegativeButton("OK", null)
                                    .show();
                        }
                    });
                }

            }
        });
    }
    Bitmap avatarbmp;
    public Bitmap load(String avatarAddress) {

        Request request = new Request.Builder()
                .url(Server.serverAddress + avatarAddress)
                .method("get", null)
                .build();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                try {
                    byte[] bytes = arg1.body().bytes();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (bmp != null && !bmp.isRecycled()) {
                                avatarbmp=bmp;
                                // setBitmap(bmp);
                            } else {
                                //1.没有头像的情况，绘制默认头像，从资源中获取Bitmap
                                Resources res = getResources();
                                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app);
                                //setBitmap(bmp);
                                //2.没有头像的情况，传输null，绘制
                                avatarbmp=bmp;
                                //setBitmap(null);
                            }

                        }
                    });
                } catch (Exception ex) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //1.没有头像的情况，绘制默认头像，从资源中获取Bitmap
                            Resources res = getResources();
                            Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app);
                            //setBitmap(bmp);
                            avatarbmp=bmp;
                            //解释错误,绘制null
                            //setBitmap(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                //1连接错误，绘制默认头像，从资源中获取Bitmap
                Resources res = getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app);
                //setBitmap(bmp);
                avatarbmp=bmp;
                //2连接错误，绘制null
                //setBitmap(null);
            }
        });
        return avatarbmp;
    }
}
