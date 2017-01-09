package com.example.fourpeople.campushousekeeper.auction.fragment.widget;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;


public class TitleFragment extends Fragment {

    TextView title;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.auction_fragment_title, container, false);
        title= (TextView) view.findViewById(R.id.tv_title);
        image= (ImageView) view.findViewById(R.id.img_title_image);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClicklistener.onClick();
            }
        });
        return view;
    }

    public void  setTitleText(String text){
        title.setText(text);
    }
    public  void setImageBitmap(int resourceId){
        image.setImageResource(resourceId);
    }
    public void setOnClicklistener(OnClicklistener OnClicklistener) {
        this.OnClicklistener = OnClicklistener;
    }
    OnClicklistener OnClicklistener;

    public static interface OnClicklistener {
        void onClick();
    }

}
