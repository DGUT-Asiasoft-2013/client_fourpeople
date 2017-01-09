package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.auction.activity.AddAuctionActivity;
import com.example.fourpeople.campushousekeeper.auction.activity.AuctionsListActivity;
import com.example.fourpeople.campushousekeeper.auction.fragment.ADFragment;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.chat.ChatActivity;
import com.example.fourpeople.campushousekeeper.person.ChargeActivity;

/**
 * Created by Administrator on 2016/12/19.
 */

public class AuctionFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_auction, null);
        }

        view.findViewById(R.id.btn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), AuctionsListActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.none);
                    }
                });

            }
        });

        view.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
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
        });

        view.findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        ADFragment adFragment = (ADFragment) getFragmentManager().findFragmentById(R.id.frag_ad_main);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
