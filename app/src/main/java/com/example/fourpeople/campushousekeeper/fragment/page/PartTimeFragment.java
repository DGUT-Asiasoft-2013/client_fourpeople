package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindJobActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindPersonActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.JobContentActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.MyParttimeActivity;
import com.example.fourpeople.campushousekeeper.parttime.activity.ReleaseActivity;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.AvatarView;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.ImagePlayFragment;
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
 * Created by Administrator on 2016/12/19.
 */

public class PartTimeFragment extends Fragment {
    View view;
    ListView listView;
    List<Jobs> data;
    Integer page = 0;
    ImagePlayFragment imagePlayFragment = new ImagePlayFragment();
    final int[] image = {R.drawable.part_1, R.drawable.part_2, R.drawable.part_3};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view ==null)
        {
            view = inflater.inflate(R.layout.fragment_parttime, null);
            listView = (ListView) view.findViewById(R.id.new_job);
            imagePlayFragment = (ImagePlayFragment) getActivity().getFragmentManager().findFragmentById(R.id.image_play);
            Button fabu=(Button)view.findViewById(R.id.fabu);
            fabu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getActivity()!=null)
                    {
                        startActivity(new Intent(getActivity(), ReleaseActivity.class));
                    }
                }
            });
            Button jianli=(Button)view.findViewById(R.id.jianli);
            jianli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity()!=null)
                    {
                        startActivity(new Intent(getActivity(), FindJobActivity.class));
                    }

                }
            });
            Button jianzhi=(Button)view.findViewById(R.id.jianzhi);
            jianzhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getActivity()!=null)
                    {
                        startActivity(new Intent(getActivity(), FindPersonActivity.class));
                    }
                }
            });
            Button wode=(Button)view.findViewById(R.id.wode);
            wode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(getActivity()!=null)
                    {
                        startActivity(new Intent(getActivity(), MyParttimeActivity.class));
                    }
                }
            });
        }

        listView.setAdapter(listViewAdapetr);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClicked(i);
            }
        });

        return view;

    }

    BaseAdapter listViewAdapetr = new BaseAdapter() {
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
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.part_job_list_item, null);
            } else {
                view = convertView;
            }
            TextView username = (TextView) view.findViewById(R.id.user_name);
            TextView jobKind = (TextView) view.findViewById(R.id.job_kind);
            TextView relearceTime = (TextView) view.findViewById(R.id.relearce_time);
            TextView jobDetails = (TextView) view.findViewById(R.id.job_details);
            TextView jobMoney = (TextView) view.findViewById(R.id.job_money);
            Jobs jobs = data.get(position);
            username.setText(jobs.getName());
            jobKind.setText("类型：" + jobs.getKind());
            jobDetails.setText("内容：" + jobs.getDetails());
            jobMoney.setText("酬劳：" + jobs.getMoney());
            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", jobs.getCreateDate()).toString();
            relearceTime.setText("发布时间" + dateStr);
            AvatarView avater = (AvatarView) view.findViewById(R.id.user_image);
            avater.load(Server.serverAddress + jobs.getAuthorAvater());
            return view;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    void reload() {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithPartTime("jobs/list")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    final Page<Jobs> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Jobs>>() {
                    });
                    if(getActivity()==null)return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PartTimeFragment.this.page = data.getNumber();
                            PartTimeFragment.this.data = data.getContent();
                            listViewAdapetr.notifyDataSetChanged();
                        }
                    });

                } catch (final Exception e) {
                    if(getActivity()==null)return;
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


    void onItemClicked(int position) {


        Intent itnt = new Intent(getActivity(), JobContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", data.get(position));
        itnt.putExtras(bundle);
        startActivity(itnt);
    }


}