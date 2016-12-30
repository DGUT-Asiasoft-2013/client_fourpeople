package com.example.fourpeople.campushousekeeper.mall.fragment.page;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Mall;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.AvatarDispose;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class ShopCenterFragment extends Fragment {
    View view;
    AvatarDispose shopCenterAvatar;
    TextView shopCenterName, shopCenterType, shopCenterUser, shopCenterAbout, shopCenterId;
    Mall mall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mall_fragment_shopcenter, null);
            shopCenterId = (TextView) view.findViewById(R.id.shopCenter_id);
            shopCenterAvatar = (AvatarDispose) view.findViewById(R.id.shopCenter_avatar);
            shopCenterName = (TextView) view.findViewById(R.id.shopCenter_name);
            shopCenterType = (TextView) view.findViewById(R.id.shopCenter_type);
            shopCenterUser = (TextView) view.findViewById(R.id.shopCenter_user);
            shopCenterAbout = (TextView) view.findViewById(R.id.shopCenter_about);
        }
        return view;
    }

    @Override
    public void onResume() {
        getMessage();
        super.onResume();
    }

    void getMessage() {
        Request request = Server.requestBuildWithMall("shop/shopcenter")
                .get()
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
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    mall = new ObjectMapper().readValue(response.body().string(), Mall.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setMessage();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "解释出错...", Toast.LENGTH_SHORT);
                        }
                    });
                }

            }
        });

    }

    void setMessage() {
        if (mall != null) {
            shopCenterId.setText("ID:" + mall.getId());
            shopCenterAvatar.load(mall.getShopAvatar());
            shopCenterName.setText(mall.getShopName());
            shopCenterType.setText(mall.getShopType());
            shopCenterUser.setText("By:" + mall.getUser().getStudentId());
            shopCenterAbout.setText(mall.getShopAbout());
        }
    }
}
