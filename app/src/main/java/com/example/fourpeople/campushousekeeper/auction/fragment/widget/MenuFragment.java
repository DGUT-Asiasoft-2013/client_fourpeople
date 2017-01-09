package com.example.fourpeople.campushousekeeper.auction.fragment.widget;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.auction.activity.AddAuctionActivity;
import com.example.fourpeople.campushousekeeper.BootActivity;
import com.example.fourpeople.campushousekeeper.auction.activity.MyAuctionActivity;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.auction.view.AuctionnerView;


public class MenuFragment extends Fragment {


    SimpleButtonItemFragment myAcution;
    SimpleButtonItemFragment backToMain;
    SimpleButtonItemFragment addAuction;
    SimpleButtonItemFragment search;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_menu, null);
            AuctionnerView picture = (AuctionnerView) view.findViewById(R.id.picture);
            picture.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.person));

            myAcution = (SimpleButtonItemFragment) getFragmentManager().findFragmentById(R.id.fra_my_auction);
            backToMain = (SimpleButtonItemFragment) getFragmentManager().findFragmentById(R.id.fra_back_to_main);
            search = (SimpleButtonItemFragment) getFragmentManager().findFragmentById(R.id.fra_search);
            addAuction = (SimpleButtonItemFragment) getFragmentManager().findFragmentById(R.id.fra_add_auction);

            myAcution.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
                @Override
                public void onClick() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), MyAuctionActivity.class));

                        }
                    });
                }
            });

            backToMain.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
                @Override
                public void onClick() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), BootActivity.class));

                        }
                    });
                }
            });
            search.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
                @Override
                public void onClick() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), BootActivity.class));


                        }
                    });
                }
            });
            addAuction.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
                @Override
                public void onClick() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), AddAuctionActivity.class));
                        }
                    });
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myAcution.setText("我的拍卖");
        myAcution.setDrawable(R.drawable.auction_my_auctions);

        backToMain.setText("首页");
        backToMain.setDrawable(R.drawable.auction_home);

        addAuction.setText("添加拍卖");
        addAuction.setDrawable(R.drawable.auction_add_auction);

        search.setText("查找拍卖");
        search.setDrawable(R.drawable.auction_search);
    }
}
