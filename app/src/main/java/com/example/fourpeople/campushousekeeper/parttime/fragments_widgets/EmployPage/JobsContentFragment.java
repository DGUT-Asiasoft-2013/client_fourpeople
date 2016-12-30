package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.EmployPage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Jobs;

/**
 * Created by Administrator on 2016/12/28.
 */

public class JobsContentFragment extends Fragment{
    Jobs jobs;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.part_fragment_jobscontent,null);
        jobs = (Jobs)getActivity(). getIntent().getSerializableExtra("content");
        TextView contentKind = (TextView) view.findViewById(R.id.content_kind);
        TextView contentMoney = (TextView) view.findViewById(R.id.content_money);
        TextView contentAmount = (TextView) view.findViewById(R.id.content_amount);
        TextView contentTime = (TextView) view.findViewById(R.id.content_time);
        //以后补上
        // TextView contentArea=(TextView)findViewById(R.id.content_area);

        TextView contentDetails = (TextView) view.findViewById(R.id.content_details);
        TextView contentRemark = (TextView) view.findViewById(R.id.content_remark);
        TextView contentPhone = (TextView) view.findViewById(R.id.content_phone);
        contentKind.setText("类型：" + jobs.getKind());
        contentMoney.setText("薪资：" + jobs.getMoney());
        contentAmount.setText("人数：" + jobs.getAmount());
        contentTime.setText("时间：" + jobs.getTime());
        contentDetails.setText("工作内容：" + jobs.getDetails());
        contentRemark.setText("备注：" + jobs.getRemark());
        contentPhone.setText("联系方式：" + jobs.getPhone());
        return view;
    }
}
