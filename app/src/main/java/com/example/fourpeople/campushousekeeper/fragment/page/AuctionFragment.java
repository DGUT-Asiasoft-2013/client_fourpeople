package com.example.fourpeople.campushousekeeper.fragment.page;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.auction.activity.AddAuctionActivity;
import com.example.fourpeople.campushousekeeper.auction.activity.AuctionsListActivity;
import com.example.fourpeople.campushousekeeper.auction.activity.MyAuctionActivity;
import com.example.fourpeople.campushousekeeper.auction.fragment.ADFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.SimpleButtonItemFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.TitleFragment;

/**
 * Created by Administrator on 2016/12/19.
 */

public class AuctionFragment extends Fragment {
    View view;
    SimpleButtonItemFragment enter;
    SimpleButtonItemFragment addAuction;
    SimpleButtonItemFragment myAuction;
    TitleFragment titleFragment;
    String userName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_auction, null);
            enter= (SimpleButtonItemFragment) getActivity().getFragmentManager().findFragmentById(R.id.fra_enter);
            addAuction= (SimpleButtonItemFragment) getActivity().getFragmentManager().findFragmentById(R.id.fra_add_auction);
            myAuction= (SimpleButtonItemFragment) getActivity().getFragmentManager().findFragmentById(R.id.fra_to_my_auction);
            titleFragment= (TitleFragment)getActivity(). getFragmentManager().findFragmentById(R.id.title);
            userName=getActivity().getIntent().getStringExtra("userName");
        }

       enter.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
           @Override
           public void onClick() {

               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Intent intent = new Intent(getActivity(), AuctionsListActivity.class);
                       intent.putExtra("userName",userName);
                       startActivity(intent);
                       getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.none);
                   }
               });

           }
       });

 /*       view.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("myName","111");
                        intent.putExtra("hisName","333");
                        startActivity(intent);

                    }
                });
            }
        });*/

      addAuction.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
          @Override
          public void onClick() {
              getActivity().runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Intent intent = new Intent(getActivity(), AddAuctionActivity.class);
                      startActivity(intent);
                      getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.none);
                  }
              });
          }
      });
        myAuction.setOnClicklistener(new SimpleButtonItemFragment.OnClicklistener() {
            @Override
            public void onClick() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), MyAuctionActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        ADFragment adFragment = (ADFragment) getFragmentManager().findFragmentById(R.id.frag_ad_main);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        myAuction.setText("我的拍卖");
        myAuction.setDrawable(R.drawable.auction);
        addAuction.setText("添加拍卖");
        addAuction.setDrawable(R.drawable.auction_add_auction);
        enter.setText("随便看看");
        enter.setDrawable(R.drawable.auction_list);

        titleFragment.setTitleText("拍卖单");

    }
}
