package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.mall.activity.ManageShopActivity;
import com.example.fourpeople.campushousekeeper.mall.activity.OpenShopActivity;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.person.InfoItemCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class PersonFragment extends Fragment {

    InfoItemCellFragment fragBalance;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_person, null);

            fragBalance = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_balance);

            view.findViewById(R.id.btn_info).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    goInfo();
                }
            });

            view.findViewById(R.id.btn_charge).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    goCharge();
                }
            });

            view.findViewById(R.id.btn_store_management).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    goStoreManagement();
                }
            });

            view.findViewById(R.id.btn_orders).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    goOrders();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        fragBalance.setItemName("余额");
        fragBalance.setItemInfo("0");
    }

    //去个人信息Fragment
    public static interface OnGoInfoListener{ void onGoInfo(); }

    OnGoInfoListener onGoInfoListener;

    public void setOnGoInfoListener(OnGoInfoListener onGoInfoListener)
    {
        this.onGoInfoListener = onGoInfoListener;
    }

    void goInfo()
    {
        if (onGoInfoListener!=null)
            onGoInfoListener.onGoInfo();
    }

    //去充值页面
    void goCharge()
    {
//        Intent itnt = new Intent(getActivity(), ChargeActivity.class);
//        startActivity(itnt);
    }

    //去商店管理页面
    void goStoreManagement() {
        Request request = Server.requestBuildWithMall("ishaveshop")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络挂了,宝宝连不上...", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Boolean ishaveshop = new ObjectMapper().readValue(response.body().bytes(), Boolean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ishaveshop){
                            //如果已经有小店，进入管理界面
                            manageShop();
                        }else{
                            //没有小店，进入创建界面
                            openShop();
                        }
                    }
                });

            }
        });
    }
    void openShop(){
        Intent intent = new Intent(getActivity(), OpenShopActivity.class);
        startActivity(intent);
    }
    void manageShop(){
        Intent intent = new Intent(getActivity(), ManageShopActivity.class);
        startActivity(intent);
    }

    //去个人订单详情页面
    void goOrders() {
        //
    }
}
