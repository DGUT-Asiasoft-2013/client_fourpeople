package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindJobActivity;

/**
 * Created by Administrator on 2016/12/20.
 */

public class JobsKinds extends Fragment{
    Button btn_daike,btn_paidan,btn_nakuaidi,btn_dabao,btn_lol,btn_other;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.part_fragment_job_kind,null);
        btn_daike=(Button) view.findViewById(R.id.btn_01);
        btn_paidan=(Button)view.findViewById(R.id.btn_02);
        btn_nakuaidi=(Button)view.findViewById(R.id.btn_03);
        btn_dabao=(Button)view.findViewById(R.id.btn_04);
        btn_lol=(Button)view.findViewById(R.id.btn_05);
        btn_other=(Button)view.findViewById(R.id.btn_06);
        btn_daike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}
