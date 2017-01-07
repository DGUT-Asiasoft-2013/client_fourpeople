package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobChoiceFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage.CheckInterviewFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage.MyApplicationsRecordFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage.MyInterviewRequestFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage.MyResumeFragment;


/**
 * Created by Administrator on 2017/1/4.
 */

public class MyWantJobActivity extends Activity {
    WantJobChoiceFragment wantJobChoiceFragment = new WantJobChoiceFragment();
    MyResumeFragment myResumeFragment = new MyResumeFragment();
    CheckInterviewFragment checkInterviewFragment=new CheckInterviewFragment();
    MyInterviewRequestFragment myInterviewRequestFragment=new MyInterviewRequestFragment();
    MyApplicationsRecordFragment myApplicationsRecordFragment=new MyApplicationsRecordFragment();

    int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_mywantjob);
        wantJobChoiceFragment = (WantJobChoiceFragment) getFragmentManager().findFragmentById(R.id.WantJob_tabBar);
        wantJobChoiceFragment.setOnBtnSelectedListener(new WantJobChoiceFragment.OnBtnSelectedListener() {
            @Override
            public void onBtnSelected(int index) {
                selectedIndex = index;
                changeFragment(index);
            }
        });
    }

    //四个按钮事件
    void changeFragment(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = myResumeFragment;
                break;
             case 1:
              fragment = myInterviewRequestFragment;
             break;
             case 2:
             fragment = myApplicationsRecordFragment;
              break;
            default:
                break;
        }
        if (fragment == null) return;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.MyWantJob_show, fragment)
                .commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置没有点击过按钮时显示第一个页面
        if (selectedIndex < 0) {
            wantJobChoiceFragment.setSelectedItem(0);
        } else if (selectedIndex >= 0) {
            wantJobChoiceFragment.setSelectedItem(selectedIndex);

        }
    }


}
