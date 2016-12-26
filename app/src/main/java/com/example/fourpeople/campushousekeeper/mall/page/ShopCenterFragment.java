package com.example.fourpeople.campushousekeeper.mall.page;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;

/**
 * Created by Administrator on 2016/12/21.
 */

public class ShopCenterFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mall_fragment_shopcenter, null);
        }
        return view;
    }
}
