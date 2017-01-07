package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.EmployPage;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
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
import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Resume;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindPersonActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.PersonContentActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.ResumeContentActivity;
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
 * Created by Administrator on 2016/12/27.
 */

public class EmployResumeFragment extends Fragment{
    View view;
    ListView listView;
    List<Resume> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;
    Jobs jobs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.part_employ_resume_fragment,null);
        jobs=(Jobs)getActivity().getIntent().getSerializableExtra("content");
        listView=(ListView) view.findViewById(R.id.Employ_list);
        listView.setAdapter(EmployAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClicked(i);
            }
        });
        return view;

    }
    BaseAdapter EmployAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            if ((convertView == null)) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.part_employ_list_item, null);
            } else {
                view = convertView;

            }
            TextView username=(TextView)view.findViewById(R.id.person_name);
            TextView title=(TextView)view.findViewById(R.id.title);
            TextView time=(TextView)view.findViewById(R.id.time);
            TextView area=(TextView)view.findViewById(R.id.person_details);
            TextView personmoney=(TextView)view.findViewById(R.id.person_money);
            Resume resume=data.get(position);
            username.setText(resume.getName());
            title.setText("求职内容："+resume.getDetails());
            personmoney.setText("期待薪资："+resume.getMoney());
            area.setText("工作区域："+resume.getArea());
            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", resume.getCreateDate()).toString();
            time.setText("发布时间"+dateStr);
            AvatarView avater=(AvatarView)view.findViewById(R.id.user_image);
            avater.load(Server.serverAddress+resume.getAvater());
            return view;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }
    void reload()
    {
        OkHttpClient client= Server.getSharedClient();
        Request request=Server.requestBuilderWithPartTime("resume/jobsId/"+jobs.getId())
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                {
                    try {
                        final Page<Resume> data=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Resume>>(){});
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EmployResumeFragment.this.page=data.getNumber();
                                EmployResumeFragment.this.data=data.getContent();
                                EmployAdapter.notifyDataSetChanged();
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
            }
        });

    }
    void onItemClicked(int position) {


        Intent itnt = new Intent(getActivity(), ResumeContentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("content",data.get(position));
        bundle.putSerializable("jobs",jobs);
        itnt.putExtras(bundle);
        startActivity(itnt);
    }
}
