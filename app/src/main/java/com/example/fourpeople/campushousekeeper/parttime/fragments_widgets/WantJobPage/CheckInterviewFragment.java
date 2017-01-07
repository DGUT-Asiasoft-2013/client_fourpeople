package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;

/**
 * Created by Administrator on 2017/1/5.
 */

public class CheckInterviewFragment extends Fragment{
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view ==null)
        {
            view=inflater.inflate(R.layout.part_fragment_checkinterview,null);
        }
        return view ;
    }
}
