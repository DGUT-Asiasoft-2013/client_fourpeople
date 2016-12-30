package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.EmployChoiceFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.EmployPage.EmployResumeFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.EmployPage.JobsContentFragment;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.EmployPage.ResumeWarehouseFragment;

/**
 * Created by Administrator on 2016/12/28.
 */

public class MyReleaseJobContentActivity extends Activity{
    JobsContentFragment jobscontentFragment=new JobsContentFragment();
    EmployResumeFragment employResumeFragment=new EmployResumeFragment();
    ResumeWarehouseFragment resumeWarehouseFragment=new ResumeWarehouseFragment();
    EmployChoiceFragment employChoiceFragment;
    int selectedIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_release_job);
       Jobs jobs = (Jobs) getIntent().getSerializableExtra("content");
        employChoiceFragment=(EmployChoiceFragment)getFragmentManager().findFragmentById(R.id.Employ_tabBar);
        employChoiceFragment.setOnBtnSelectedListener(new EmployChoiceFragment.OnBtnSelectedListener() {
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
                fragment = jobscontentFragment;
                break;
           case 1:
              fragment = employResumeFragment;
                break;
           case 2:
               fragment = resumeWarehouseFragment;
                break;
          //  case 3:
              //  fragment = personFragment;
              //  break;
           // default:
             //   break;
        }
        if (fragment == null) return;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.ReleaseJobContent_show, fragment)
                .commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置没有点击过按钮时显示第一个页面
        if (selectedIndex < 0) {
            employChoiceFragment.setSelectedItem(0);
        } else if (selectedIndex >= 0) {
            employChoiceFragment.setSelectedItem(selectedIndex);

        }
    }
}
