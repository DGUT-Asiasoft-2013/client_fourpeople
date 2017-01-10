package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.LoginActivity;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.mall.activity.ManageShopActivity;
import com.example.fourpeople.campushousekeeper.mall.activity.OpenShopActivity;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.person.AvatarView;
import com.example.fourpeople.campushousekeeper.person.ChargeActivity;
import com.example.fourpeople.campushousekeeper.person.MyInfoActivity;
import com.example.fourpeople.campushousekeeper.person.OrdersActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonFragment extends Fragment {
    View view;

    TextView myName;
    TextView infoBalance;
    AvatarView avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_person, null);

            myName = (TextView) view.findViewById(R.id.my_name);
            infoBalance = (TextView) view.findViewById(R.id.info_balance);
            avatar = (AvatarView) view.findViewById(R.id.person_avatar);

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

            view.findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    goLogout();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuildWithApi("info")
                .method("get", null)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(final Call arg0, Response arg1) throws IOException {
                try {
                    final User user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            PersonFragment.this.onResponse(arg0,user);

                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            PersonFragment.this.onFailure(arg0, e);

                        }
                    });
                }

            }

            @Override
            public void onFailure(final Call arg0, final IOException arg1) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        PersonFragment.this.onFailure(arg0, arg1);

                    }
                });

            }
        });
    }

    void onResponse(Call arg0, User user)
    {
        myName.setText(user.getName());
        avatar.load(user);
        infoBalance.setText("￥"+user.getBalance());
    }

    void onFailure(Call call, Exception e) {
        new AlertDialog.Builder(getActivity())
                .setTitle("ERROR")
                .setMessage(e.getMessage())
                .setNegativeButton("OK", null)
                .show();
    }

    //去个人信息Activity
    void goInfo()
    {
        Intent itnt = new Intent(getActivity(), MyInfoActivity.class);
        startActivity(itnt);
    }

    //去充值页面
    void goCharge()
    {
        Intent itnt = new Intent(getActivity(), ChargeActivity.class);
        startActivity(itnt);
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
        Intent intent = new Intent(getActivity(), OrdersActivity.class);
        startActivity(intent);
    }

    void goLogout() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确定注销吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent itnt = new Intent(getActivity(), LoginActivity.class);
                        startActivity(itnt);
                        getActivity().finish();
                    }
                }).show();
    }
}
