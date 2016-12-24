package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.entity.Auction;
import com.example.fourpeople.campushousekeeper.fragment.AuctionsListFragment;
import com.example.fourpeople.campushousekeeper.widget.AuctionAdapter;

import java.util.List;

public class AuctionsListActivity extends Activity {

    AuctionsListFragment auctionsListFragment=new AuctionsListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions_list);
        getFragmentManager().beginTransaction().replace(R.id.content,auctionsListFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
