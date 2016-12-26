package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.fourpeople.campushousekeeper.auction.fragment.AuctionsListFragment;
import com.example.fourpeople.campushousekeeper.R;

public class AuctionsListActivity extends Activity {

    AuctionsListFragment auctionsListFragment=new AuctionsListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity_auctions_list);
        getFragmentManager().beginTransaction().replace(R.id.content,auctionsListFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
