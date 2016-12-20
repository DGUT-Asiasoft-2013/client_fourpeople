package com.example.fourpeople.campushousekeeper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class MainChoiceFragment extends Fragment {
    View view;//定义MainChoiceFragment布局
    View btnMall, btnPartTime, btnAuction, btnPerson;//定义四个按钮
    View[] btns;//定义四个按钮的数组

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mainchoice, null);
        //关联四个按钮
        btnMall =  view.findViewById(R.id.mainChoice_mall);
        btnPartTime = view.findViewById(R.id.mainChoice_partTime);
        btnAuction = view.findViewById(R.id.mainChoice_auction);
        btnPerson = view.findViewById(R.id.mainChoice_person);

        btns = new View[]{
                btnMall, btnPartTime, btnAuction, btnPerson
        };
        for (final View btn : btns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBtnClicked(btn);
                }
            });
        }
        return view;
    }

    //四个按钮事件的默认响应事件
    public static interface OnBtnSelectedListener {
        void onBtnSelected(int index);
    }

    OnBtnSelectedListener onBtnSelectedListener;

    public void setOnBtnSelectedListener(OnBtnSelectedListener onBtnSelectedListener) {
        this.onBtnSelectedListener = onBtnSelectedListener;
    }

    void onBtnClicked(View btn) {
        int selectedIndex = -1;
        for (int i = 0; i < btns.length; i++) {
            View otherView = btns[i];
            if (btn == otherView) {
                otherView.setSelected(true);
                selectedIndex = i;
            } else {
                otherView.setSelected(false);
            }
        }
        if (onBtnSelectedListener != null && selectedIndex >= 0) {
            onBtnSelectedListener.onBtnSelected(selectedIndex);
        }
    }

    //当没有点击时默认显示的内容
    public void setSelectedItem(int index) {
        if (index >= 0 && index < btns.length) {
            onBtnClicked(btns[index]);
        }
    }

    //获取当前点击的按钮，如果错误返回-1在activity处理
    public int getSelectedIndex() {
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) return i;
        }
        return -1;
    }

}
