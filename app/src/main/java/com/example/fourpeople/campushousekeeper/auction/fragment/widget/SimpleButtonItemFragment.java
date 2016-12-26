package com.example.fourpeople.campushousekeeper.auction.fragment.widget;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;


public class SimpleButtonItemFragment extends Fragment {

    ImageView picture;
    TextView introduction;
    View view;
    public SimpleButtonItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment


        if (view==null) {
             view=inflater.inflate(R.layout.auction_fragment_simple_button_item, container);
        }
        picture= (ImageView) view.findViewById(R.id.icon);
        introduction= (TextView) view.findViewById(R.id.tv_introduction);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClicklistener.onClick();
            }
        });

        return view;
    }

    public void setOnClicklistener(OnClicklistener OnClicklistener) {
        this.OnClicklistener = OnClicklistener;
    }
    OnClicklistener OnClicklistener;

    public static interface OnClicklistener {
        void onClick();
    }

    public void setDrawable(int drawableResource){
        picture.setImageResource(drawableResource);
    }
    public void setText(String introductionString){
          introduction.setText(introductionString);
    }




}
