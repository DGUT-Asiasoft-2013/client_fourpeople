package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by Administrator on 2016/12/20.
 */

public class FindJobActivity extends Activity {
    ListView listView;
    List<Jobs> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_findjob);
        listView = (ListView) findViewById(R.id.list_jods);
        Button btn_daike = (Button) findViewById(R.id.btn_01);
        btn_daike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("代课");
            }
        });
        Button btn_paidan = (Button) findViewById(R.id.btn_02);
        btn_paidan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("派单");
            }
        });
        Button btn_kuaidi = (Button) findViewById(R.id.btn_03);
        btn_kuaidi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("快递");
            }
        });
        Button btn_dabao = (Button) findViewById(R.id.btn_04);
        btn_dabao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("打包");
            }
        });
        Button btn_lol = (Button) findViewById(R.id.btn_05);
        btn_lol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("lol");
            }
        });
        Button btn_qita = (Button) findViewById(R.id.btn_06);
        btn_qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("其他");
            }
        });
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClicked(i);
            }
        });
    }

    BaseAdapter listAdapter = new BaseAdapter() {
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
            View view;
            if ((convertView == null)) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.part_job_list_item, null);
            } else {
                view = convertView;

            }
            //TextView username=(TextView)view.findViewById(R.id.user_name);
            //要通过view去寻找
            //TextView username=(TextView)findViewById(R.id.user_name);这样会空指针异常
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
    protected void onResume() {
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FindJobActivity.this.page = data.getNumber();
                            FindJobActivity.this.data = data.getContent();
                            listAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(FindJobActivity.this).setMessage(e.getMessage())
                                    .show();
                        }
                    });

                }

            }
        });
    }

    void onItemClicked(int position) {


        Intent itnt = new Intent(FindJobActivity.this, JobContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", data.get(position));
        itnt.putExtras(bundle);
        startActivity(itnt);
    }

    //public static void methodB(){}
    void search(String key)
    {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithPartTime("jobs/s/" + key)
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(FindJobActivity.this)
                                .setMessage("当前搜索兼职为空")
                                .show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException

            {
                try {
                    final Page<Jobs> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Jobs>>() {
                    });
                    FindJobActivity.this.page = data.getNumber();
                    FindJobActivity.this.data = data.getContent();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            listAdapter.notifyDataSetChanged();
                        }


                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(FindJobActivity.this)
                                    .setMessage((CharSequence) e)
                                    .show();
                        }
                    });

                }
            }

        });
    }
}
