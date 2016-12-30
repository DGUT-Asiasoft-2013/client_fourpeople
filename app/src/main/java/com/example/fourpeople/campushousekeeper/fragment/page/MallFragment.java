package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Mall;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.activity.GoodsShowActivity;
import com.example.fourpeople.campushousekeeper.mall.view.AvatarDispose;
import com.example.fourpeople.campushousekeeper.mall.view.Closeed;
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
 * Created by Administrator on 2016/12/19.
 */

public class MallFragment extends Fragment {
    LinearLayout fragmentMall;//定义Mall界面的变量
    View view;
    Spinner mallSpinner;//下拉框选择器
    ListView mallListView;//列表
    Button defaultBtn, creditBtn;//默认排序和信用排序按钮
    //加载更多视图与按钮
    View getMoreView;
    TextView getMoreBtn;
    //数据
    List<Mall> mallList;
    int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mall, null);
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
            getMoreView = inflater.inflate(R.layout.mall_button_get_more, null);
            getMoreBtn = (TextView) getMoreView.findViewById(R.id.btn_get_more);
            mallListView.addFooterView(getMoreView);
            mallListView.setAdapter(baseAdapter);
            //控件连接
            mallSpinner = (Spinner) view.findViewById(R.id.fragment_mall_spinner);
            defaultBtn = (Button) view.findViewById(R.id.fragment_mall_defaultBtn);
            creditBtn = (Button) view.findViewById(R.id.fragment_mall_creditBtn);
            //加载更多按钮事件
            getMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadmore();
                }
            });
            //商店列表按钮事件
            mallListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onShopItemClick(i);
                }
            });
        }
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

    @Override
    public void onResume() {
        getMessage();
        super.onResume();
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("shop/show")
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
                            MallFragment.this.mallList = mallData.getContent();
                            MallFragment.this.page = mallData.getNumber();
                            baseAdapter.notifyDataSetInvalidated();//更新视图
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //加载更多按钮事件
    void loadmore() {
        getMoreView.setEnabled(false);
        getMoreBtn.setText("载入中…");

        Request request = Server.requestBuildWithMall("shop/show/" + (page + 1)).get().build();
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
    }

    void onShopItemClick(int i) {
        Intent intent = new Intent(getActivity(), GoodsShowActivity.class);
        Mall mall = mallList.get(i);
        intent.putExtra("mall", mall);
        startActivity(intent);
    }
}
