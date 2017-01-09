package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fourpeople.campushousekeeper.auction.fragment.MyAuctionFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.MyDealListFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.MyTransactionFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.MenuFragment;
import com.example.fourpeople.campushousekeeper.R;

public class MyAuctionActivity extends Activity {

    Button menu;
    Button myBid;
    Button myDeal;
    Button myAuction;
    Boolean isOpen = false;
    MenuFragment menuFragment = new MenuFragment();
    MyAuctionFragment myAuctionFragment=new MyAuctionFragment();
    MyTransactionFragment myTransactionFragment =new MyTransactionFragment();
    MyDealListFragment myDealListFragment=new MyDealListFragment();
    ;

    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity_my_auction);
        getFragmentManager().beginTransaction().replace(R.id.container, myAuctionFragment).commit();
        initView();
    }


    void initView() {
        myBid = (Button) findViewById(R.id.btn_my_bid);
        menu = (Button) findViewById(R.id.btn_menu);
        myDeal = (Button) findViewById(R.id.btn_my_deals);
        myAuction = (Button) findViewById(R.id.btn_my_auctions);

        myAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(R.id.container, myAuctionFragment, 0);
            }
        });


        myDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(R.id.container, myDealListFragment, 1);
            }
        });

        myBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(R.id.container, myTransactionFragment, 2);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen == false) {

                    getFragmentManager().beginTransaction().replace(R.id.menu_container, menuFragment).commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Drawable rewardIcon = getResources().getDrawable(R.drawable.auction_drag_down);
                            if (rewardIcon != null) {
                                rewardIcon.setBounds(0, 0, rewardIcon.getMinimumWidth(), rewardIcon.getMinimumHeight());
                                menu.setCompoundDrawables(rewardIcon, null, null, null);
                            }
                        }
                    });
                    isOpen = true;
                } else {
                    getFragmentManager().beginTransaction().remove(menuFragment).commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Drawable rewardIcon = getResources().getDrawable(R.drawable.auction_menu);
                            if (rewardIcon != null) {
                                rewardIcon.setBounds(0, 0, rewardIcon.getMinimumWidth(), rewardIcon.getMinimumHeight());
                                menu.setCompoundDrawables(rewardIcon, null, null, null);
                            }
                        }
                    });
                    isOpen = false;
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        Drawable rewardIcon = getResources().getDrawable(R.drawable.auction_menu);
        if (rewardIcon != null) {
            rewardIcon.setBounds(0, 0, rewardIcon.getMinimumWidth(), rewardIcon.getMinimumHeight());
            menu.setCompoundDrawables
                    (rewardIcon,
                            null,
                            null,
                            null);
        }
    }

    void load(int containner, Fragment fragment, int type) {
        if (fragment != null) {
            if (fragment.isAdded()) {
                getFragmentManager().beginTransaction().show(fragment).commit();
            } else {
                getFragmentManager().beginTransaction().replace(containner, fragment).commit();
            }

        } else {
            if (type == 0) {
                fragment = new MyAuctionFragment();
            } else if (type == 1) {
                fragment = new MyDealListFragment();
            } else if (type == 2) {
                fragment = new MyTransactionFragment();
            }
            getFragmentManager().beginTransaction().replace(containner, fragment).commit();

        }
    }
}