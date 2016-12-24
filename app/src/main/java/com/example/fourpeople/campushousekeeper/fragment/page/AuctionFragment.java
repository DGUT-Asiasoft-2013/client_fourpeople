package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.AddAuctionActivity;
import com.example.fourpeople.campushousekeeper.AuctionsListActivity;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.fragment.ADFragment;

/**
 * Created by Administrator on 2016/12/19.
 */

public class AuctionFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_auction, null);
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
       /* Drawable rewardIcon=getResources().getDrawable(R.drawable.menu);
        if(rewardIcon!=null)
        {
            rewardIcon.setBounds(0, 0, rewardIcon.getMinimumWidth(), rewardIcon.getMinimumHeight());
            menu.setCompoundDrawables(rewardIcon, null, null, null);
        }*/


    }
}
