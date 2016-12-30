package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.fourpeople.campushousekeeper.auction.fragment.AuctionsListFragment;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.MenuFragment;

public class AuctionsListActivity extends Activity {
    ImageButton menu;
    Boolean isOpen = false;
    MenuFragment menuFragment = new MenuFragment();
    AuctionsListFragment auctionsListFragment = new AuctionsListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity_auctions_list);
        initView();
        getFragmentManager().beginTransaction().replace(R.id.content, auctionsListFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        menu.setImageDrawable(getResources().getDrawable(R.drawable.auction_menu));

    }

    void initView() {
        menu = (ImageButton) findViewById(R.id.btn_menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isOpen == false) {

                    getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                            R.animator.slide_out_right
                    ).replace(R.id.menu_container, menuFragment).commit();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.auction_drag_down));
                        }
                    });
                    //isOpen=true;
                } else {

                    getFragmentManager().beginTransaction().remove(menuFragment).commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.auction_menu));
                        }
                    });
                    // isOpen=false;
                }
                isOpen = !isOpen;
            }

        });
    }
}
