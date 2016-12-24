package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class MallFragment extends Fragment {
    private LinearLayout fragmentMall;//定义Mall界面的变量
    View view;
    Spinner mallSpinner;//下拉框选择器
    ListView mallListView;//列表
    Button defaultBtn, creditBtn;//默认排序和信用排序按钮
    //加载更多视图与按钮
    View getMoreView;
    TextView getMoreBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mall, null);
            //关联对应的布局,设置监听事件关闭小键盘
            fragmentMall = (LinearLayout) view.findViewById(R.id.fragment_mall);
            fragmentMall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMallClick();
                }
            });
            //列表设置
            mallListView = (ListView) view.findViewById(R.id.fragment_mall_listView);
            getMoreView = inflater.inflate(R.layout.button_get_more, null);
            getMoreBtn = (TextView) getMoreView.findViewById(R.id.btn_get_more);
            mallListView.addFooterView(getMoreView);
            mallListView.setAdapter(baseAdapter);
            //控件连接
            mallSpinner = (Spinner) view.findViewById(R.id.fragment_mall_spinner);
            defaultBtn = (Button) view.findViewById(R.id.fragment_mall_defaultBtn);
            creditBtn = (Button) view.findViewById(R.id.fragment_mall_creditBtn);
        }
        return view;
    }

    //关闭小键盘事件
    void onMallClick() {
        View view = getActivity().getWindow().peekDecorView();
        if (view != null && view.getWindowToken() != null) {
            try {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                Log.w("SOS", "==软键盘关闭时出了异常");
            }
        }
    }

    BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    };
}
