package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Interview;
import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MyInterviewRequestFragment extends Fragment {
    ListView listView;
    View view;
    Integer page = 0;
    List<Interview> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.part_fragment_interview, null);
            listView = (ListView) view.findViewById(R.id.My_Interview);
            listView.setAdapter(InterviewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }
        return view;
    }

    BaseAdapter InterviewAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if ((convertView == null)) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.part_interview_item, null);
            } else {
                view = convertView;

            }
            TextView user_name=(TextView)view.findViewById(R.id.user_name);
            TextView interview_time=(TextView)view.findViewById(R.id.interview_time);
            TextView relearce_time=(TextView)view.findViewById(R.id.relearce_time);
            TextView interview_area=(TextView)view.findViewById(R.id.interview_area);
            TextView interview_phone=(TextView)view.findViewById(R.id.interview_phone);
            TextView interview_remark=(TextView)view.findViewById(R.id.interview_remark);
            Interview interview=data.get(position);
            user_name.setText(interview.getEmployer());
            interview_time.setText("面试时间："+interview.getTime());
            interview_area.setText("面试地点："+interview.getArea());
            interview_phone.setText("联系方式："+interview.getPhone());
            interview_remark.setText("备    注："+interview.getRemark());
            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", interview.getCreateDate()).toString();
            relearce_time.setText("发布时间："+dateStr);
            AvatarView avater=(AvatarView)view.findViewById(R.id.user_image);
            avater.load(Server.serverAddress+interview.getAuthorAvater());
            return view;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        onreload();
    }
    void onreload()
    {
        OkHttpClient client= Server.getSharedClient();
        Request request=Server.requestBuilderWithPartTime("interview/request")
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Page<Interview> data=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Interview>>(){});
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyInterviewRequestFragment.this.page=data.getNumber();
                            MyInterviewRequestFragment.this.data=data.getContent();
                            InterviewAdapter.notifyDataSetChanged();
                        }
                    });

                }catch (final Exception e)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity()).setMessage(e.getMessage())
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
