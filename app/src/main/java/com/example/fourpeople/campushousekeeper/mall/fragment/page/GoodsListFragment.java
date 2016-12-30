package com.example.fourpeople.campushousekeeper.mall.fragment.page;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.activity.GoodsListDisposeActivity;
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
            goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onItemClicked(i);
                }
            });
            //加载更多
            getMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadmore();
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
                if (getActivity() == null) return;
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
                    if (getActivity() == null) return;
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

    //加载更多按钮事件
    void loadmore() {
        getMoreView.setEnabled(false);
        getMoreBtn.setText("载入中…");

        Request request = Server.requestBuildWithMall("goods/show/" + (page + 1)).get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call arg0, final Response arg1) throws IOException {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getMoreView.setEnabled(true);
                        getMoreBtn.setText("加载更多");
                        try {
                            final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Goods>>() {
                            });
                            if (data.getNumber() > page) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (goodsData == null) {
                                            goodsData = data.getContent();
                                        } else {
                                            goodsData.addAll(data.getContent());
                                        }
                                        page = data.getNumber();
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
            public void onFailure(Call arg0, IOException arg1) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getMoreView.setEnabled(true);
                        getMoreBtn.setText("加载更多");
                    }
                });
            }
        });
    }

    //点击列表对应事件
    void onItemClicked(final int i) {
        String[] items = {"商品修改", "下架商品"};
        new AlertDialog.Builder(getActivity())
                .setTitle("请选择操作:")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                disposeGoods(i);
                                break;
                            case 1:
                                pullGoods(i);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    //商品修改
    void disposeGoods(int i) {
        Intent intent = new Intent(getActivity(), GoodsListDisposeActivity.class);
        Goods goods = goodsData.get(i);
        intent.putExtra("goods", goods);
        startActivity(intent);
    }

    //下架商品
    void pullGoods(int i) {
        Goods goods = goodsData.get(i);
        MultipartBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("id", goods.getId().toString())
                .build();
        Request request = Server.requestBuildWithMall("goods/delete")
                .method("post", null)
                .post(requestBody)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("网络连接失败...")
                                .setMessage(e.getLocalizedMessage())
                                .setNegativeButton("OK", null)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (getActivity() == null) return;
                final Boolean succeed = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (succeed) {
                            getGoodsMessage();
                            Toast.makeText(getActivity(), "下架成功，要加油哦！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "下架失败，请重新操作", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
