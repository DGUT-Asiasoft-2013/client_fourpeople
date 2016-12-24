package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.example.fourpeople.campushousekeeper.fragment.MainChoiceFragment;
import com.example.fourpeople.campushousekeeper.fragment.ModifyInfoFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.AuctionFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.MallFragment;
import com.example.fourpeople.campushousekeeper.fragment.MyInfoFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.PartTimeFragment;
import com.example.fourpeople.campushousekeeper.fragment.page.PersonFragment;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BootActivity extends Activity {
    //获取四个按钮对应的页面显示
    MallFragment mallFragment = new MallFragment();
    PartTimeFragment parttimeFragment = new PartTimeFragment();
    AuctionFragment auctionFragment = new AuctionFragment();
    PersonFragment personFragment = new PersonFragment();
    //获取四个按钮
    MainChoiceFragment mainChoiceFragment;
    int selectedIndex = -1;

    MyInfoFragment myInfo = new MyInfoFragment();
    ModifyInfoFragment modifyInfo = new ModifyInfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        mainChoiceFragment = (MainChoiceFragment) getFragmentManager().findFragmentById(R.id.boot_tabBar);
        //重写四个按钮事件
        mainChoiceFragment.setOnBtnSelectedListener(new MainChoiceFragment.OnBtnSelectedListener() {
            @Override
            public void onBtnSelected(int index) {
                selectedIndex = index;
                changeFragment(index);
            }
        });

        personFragment.setOnGoInfoListener(new PersonFragment.OnGoInfoListener() {
            @Override
            public void onGoInfo() {
                goInfo();
            }
        });

        myInfo.setOnGoModifyListener(new MyInfoFragment.OnGoModifyListener() {

            @Override
            public void onGoModify() {
                goModify();
            }
        });
    }

    //四个按钮事件
    void changeFragment(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = mallFragment;
                break;
            case 1:
                fragment = parttimeFragment;
                break;
            case 2:
                fragment = auctionFragment;
                break;
            case 3:
                fragment = personFragment;
                break;
            default:
                break;
        }
        if (fragment == null) return;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.boot_show, fragment)
                .commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置没有点击过按钮时显示第一个页面
        if (selectedIndex < 0) {
            mainChoiceFragment.setSelectedItem(0);
        } else if (selectedIndex >= 0) {
            mainChoiceFragment.setSelectedItem(selectedIndex);

        }
    }

    void goInfo() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
                        R.animator.slide_in_left, R.animator.slide_out_right).replace(R.id.boot_show,myInfo)
                .addToBackStack(null)
                .commit();
    }

    void goModify() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
                        R.animator.slide_in_left, R.animator.slide_out_right).replace(R.id.boot_show, modifyInfo)
                .addToBackStack(null)
                .commit();
    }
}
