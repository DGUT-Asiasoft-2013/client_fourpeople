package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
/**
 * Created by Administrator on 2016/12/29.
 */

public class PartButtonItemFragment extends Fragment{
    ImageView picture;
    TextView introduction;
    public PartButtonItemFragment() {
        // Required empty public constructor
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.part_fragment_buttonitem,null);
        picture= (ImageView) view.findViewById(R.id.icon);
        introduction= (TextView) view.findViewById(R.id.tv_introduction);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClicklistener.click();

            }
        });

        return view;
    }
    public void setOnClicklistener(OnClicklistener onClicklistener)
    {
        this.OnClicklistener=onClicklistener;
    }
    OnClicklistener OnClicklistener;
    public static interface OnClicklistener
    {
        void click();
    }



    public void setDrawable(int drawableResource){
        picture.setImageResource(drawableResource);
    }
    public void setText(String introductionString){
        introduction.setText(introductionString);
    }




}

