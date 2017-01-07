package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Jobs;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Resume;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.parttime.activity.FindJobActivity;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/4.
 */

public class MyApplicationsRecordFragment extends Fragment{
    ListView listView;
    List<Jobs> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;
    View view;

    SparseArray<Boolean> dataCheckResult = new SparseArray<Boolean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null)
        {
            view=inflater.inflate(R.layout.part_fragment_myapplicationsrecord,null);
            listView=(ListView)view.findViewById(R.id.My_Applications_Record);
            listView.setAdapter(applicationAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }

        return view;
    }
    BaseAdapter applicationAdapter=new BaseAdapter() {
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
                view = inflater.inflate(R.layout.part_fragment_record_list_item, null);
            } else {
                view = convertView;

            }
            //TextView username=(TextView)view.findViewById(R.id.user_name);
            //要通过view去寻找
            //TextView username=(TextView)findViewById(R.id.user_name);这样会空指针异常
            TextView username=(TextView)view.findViewById(R.id.user_name);
            TextView jobKind=(TextView)view.findViewById(R.id.job_kind);
            TextView relearceTime=(TextView)view.findViewById(R.id.relearce_time);
            TextView jobDetails=(TextView)view.findViewById(R.id.job_details);
            TextView jobMoney=(TextView)view.findViewById(R.id.job_money);
            ImageView image_employ=(ImageView)view.findViewById(R.id.image_employ);
            TextView textView=(TextView)view.findViewById(R.id.text);
            TextView text_connect=(TextView)view.findViewById(R.id.text_connect);
            text_connect.setText("联系老板");
            Jobs jobs=data.get(position);
            boolean checked = dataCheckResult.get(jobs.getId(),false);
            if(checked)
            {
                image_employ.setImageDrawable(getResources().getDrawable(R.drawable.part_employ));
                textView.setText("录用");
            }
            else {
                image_employ.setImageDrawable(getResources().getDrawable(R.drawable.part_wait));
                textView.setText("等候");

            }
            username.setText(jobs.getName());
            jobKind.setText("类型："+jobs.getKind());
            jobDetails.setText("内容："+jobs.getDetails());
            jobMoney.setText("酬劳："+jobs.getMoney());
            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", jobs.getCreateDate()).toString();
            relearceTime.setText("发布时间"+dateStr);
            AvatarView avater=(AvatarView)view.findViewById(R.id.user_image);
            avater.load(Server.serverAddress+jobs.getAuthorAvater());
            return view;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        reload();
       // checkEngage();
    }
    void reload()
    {
        OkHttpClient client=Server.getSharedClient();
        Request request=Server.requestBuilderWithPartTime("MyApplicationRecord")

                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Page<Jobs> data=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Jobs>>(){});
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplicationsRecordFragment.this.page=data.getNumber();
                            MyApplicationsRecordFragment.this.data=data.getContent();
                            applicationAdapter.notifyDataSetChanged();
                            final List<Jobs> tempData=MyApplicationsRecordFragment.this.data;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < tempData.size(); i++) {
                                       checkEngage(tempData.get(i).getId(),tempData.get(i));
                                    }
                                }
                            });
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
    //检查是否录用
    void checkEngage(final int index,Jobs jobs)
    {

        Request request=Server.requestBuilderWithPartTime("checkEmploy/"+jobs.getId())
                .method("get",null)
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try
                {
                    final String responseString=response.body().string();

                    final Boolean result=new ObjectMapper().readValue(responseString,Boolean.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(index,result);
                        }
                    });
                }catch (final Exception e)
                {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(index,false);
                        }
                    });
                }
            }
        });
    }
    void onCheckLikedResult(int index,boolean result)
    {
        /*if(result)
        {
            image_employ.setImageDrawable(getResources().getDrawable(R.drawable.part_ic_launcher));
        }
        else {
            image_employ.setImageDrawable(getResources().getDrawable(R.drawable.part_1));

        }*/
        dataCheckResult.append(index,result);
        if (index==data.size()) {
            applicationAdapter.notifyDataSetChanged();
        }

    }
}
