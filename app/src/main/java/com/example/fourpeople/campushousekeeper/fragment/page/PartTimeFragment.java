package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fourpeople.campushousekeeper.parttime.activity.FindJobActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindPersonActivity;
import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.parttime.activity.ReleaseActivity;

/**
 * Created by Administrator on 2016/12/19.
 */

public class PartTimeFragment extends Fragment {
    View view;
    Button release,findPerson,findJob;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_parttime, null);
        release=(Button)view.findViewById(R.id.btn_release);
        findJob=(Button)view.findViewById(R.id.btn_findJobs);
        findPerson=(Button)view.findViewById(R.id.btn_findPerson);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gorelease();
            }
        });
        findPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gofindperson();
            }
        });
        findJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gofindjob();
            }
        });

        return view;

    }
    void gorelease()
    {
        Intent itnt;
        itnt = new Intent(getActivity(), ReleaseActivity.class);
        startActivity(itnt);
    }
    void gofindperson()
    {
        Intent itnt;
        itnt = new Intent(getActivity(), FindPersonActivity.class);
        startActivity(itnt);
    }
    void gofindjob()
    {
        Intent intent=new Intent(getActivity(), FindJobActivity.class);
        startActivity(intent);
    }


}