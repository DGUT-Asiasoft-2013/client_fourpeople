package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.example.fourpeople.campushousekeeper.fragment.page.GoodsDisposeFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.GoodsListFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.OrderCenterFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.ShopCenterFragment;

/**
 * Created by Administrator on 2016/12/21.
 */

public class ManageShopActivity extends Activity {
    View goodsList, goodsDispose, orderCenter, shopCenter;
    View[] tabs;
    int selectedIndex;//定义一个全局变量来记录当前点击显示的上半部分页面
    GoodsListFragment goodsListFragment = new GoodsListFragment();
    GoodsDisposeFragment goodsDisposeFragment = new GoodsDisposeFragment();
    OrderCenterFragment orderCenterFragment = new OrderCenterFragment();
    ShopCenterFragment shopCenterFragment = new ShopCenterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageshop);
        goodsList = findViewById(R.id.manageShop_goodsList);
        goodsDispose = findViewById(R.id.manageShop_goodsDispose);
        orderCenter = findViewById(R.id.manageShop_orderCenter);
        shopCenter = findViewById(R.id.manageShop_shopCenter);
        tabs = new View[]{
                goodsList, goodsDispose, orderCenter, shopCenter
        };
        for (final View tab : tabs) {
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = -1;
                    for (int i = 0; i < tabs.length; i++) {
                        View otherTab = tabs[i];
                        if (otherTab == tab) {
                            otherTab.setSelected(true);
                            selectedIndex = i;
                        } else {
                            otherTab.setSelected(false);
                        }
                    }
                    if (selectedIndex >= 0) {
                        changeContentFragment(selectedIndex);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedIndex < 0) {
            changeContentFragment(0);
        } else if (selectedIndex >= 0) {
            changeContentFragment(selectedIndex);
        }
    }

    void changeContentFragment(int index) {
        Fragment newFrag = null;

        switch (index) {
            case 0:
                newFrag = goodsListFragment;
                break;
            case 1:
                newFrag = goodsDisposeFragment;
                break;
            case 2:
                newFrag = orderCenterFragment;
                break;
            case 3:
                newFrag = shopCenterFragment;
                break;

            default:
                break;
        }

        if (newFrag == null) return;

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.manageShop_view, newFrag)
                .commit();
    }
}
