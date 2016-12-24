package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fourpeople.campushousekeeper.fragment.widget.MenuFragment;

public class MyAuctionActivity extends Activity {

    Button menu;
    Boolean isOpen = false;
    MenuFragment menuFragment = new MenuFragment();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_auction);
        initView();
    }


    void initView() {
        menu = (Button) findViewById(R.id.btn_menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen == false) {

                    getFragmentManager().beginTransaction().replace(R.id.menu_container, menuFragment).commit();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Drawable rewardIcon = getResources().getDrawable(R.drawable.drag_down);
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

                            Drawable rewardIcon = getResources().getDrawable(R.drawable.menu);
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
        Drawable rewardIcon=getResources().getDrawable(R.drawable.menu);
        if(rewardIcon!=null)
        {
            rewardIcon.setBounds(0, 0, rewardIcon.getMinimumWidth(), rewardIcon.getMinimumHeight());
            menu.setCompoundDrawables
                    (rewardIcon,
                    null,
                    null,
                    null);
        }
    }
}
