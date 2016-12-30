package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindJobActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindPersonActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.ReleaseActivity;

/**
 * Created by Administrator on 2016/12/28.
 */

public class PartTimeMenuFragment extends Fragment {
    PartButtonItemFragment release, findPerson, findJob, me;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.part_fragment_menu, null);
            release = (PartButtonItemFragment) getFragmentManager().findFragmentById(R.id.btn_release);
            findPerson = (PartButtonItemFragment) getFragmentManager().findFragmentById(R.id.btn_findPerson);
            findJob = (PartButtonItemFragment) getFragmentManager().findFragmentById(R.id.btn_findJobs);
            me = (PartButtonItemFragment) getFragmentManager().findFragmentById(R.id.me);
            release.setOnClicklistener(new PartButtonItemFragment.OnClicklistener() {
                @Override
                public void click() {

                    startActivity(new Intent(getActivity(), ReleaseActivity.class));
                }
            });
            findPerson.setOnClicklistener(new PartButtonItemFragment.OnClicklistener() {
                @Override
                public void click() {

                    startActivity(new Intent(getActivity(), FindPersonActivity.class));
                }
            });
            findJob.setOnClicklistener(new PartButtonItemFragment.OnClicklistener() {
                @Override
                public void click() {

                    startActivity(new Intent(getActivity(), FindJobActivity.class));

                }
            });
            me.setOnClicklistener(new PartButtonItemFragment.OnClicklistener() {
                @Override
                public void click() {

                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        release.setText("发布");
        release.setDrawable(R.drawable.part_release);
        findPerson.setText("简历库");
        findPerson.setDrawable(R.drawable.part_resume);
        findJob.setText("全部兼职");
        findJob.setDrawable(R.drawable.part_jobs);
        me.setText("我的");
        me.setDrawable(R.drawable.part_me);

    }
}





