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
import com.example.fourpeople.campushousekeeper.api.Resume;
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

public class FindPersonActivity extends Activity{
    ListView listView;
    List<Resume> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_findperson);
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
        listView =(ListView)findViewById(R.id.list_jods);
        listView.setAdapter(personAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClicked(i);
            }
        });
    }
    BaseAdapter personAdapter=new BaseAdapter() {
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
                view = inflater.inflate(R.layout.part_person_list_item, null);
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
    protected void onResume() {
        super.onResume();
        reload();
    }
    void reload()
    {
        OkHttpClient client= Server.getSharedClient();
        Request request=Server.requestBuilderWithPartTime("resume/list")
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                       final Page<Resume> data=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Resume>>(){});
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FindPersonActivity.this.page=data.getNumber();
                            FindPersonActivity.this.data=data.getContent();
                            personAdapter.notifyDataSetChanged();
                        }
                    });

                }catch (final Exception e)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(FindPersonActivity.this).setMessage(e.getMessage())
                                    .show();
                        }
                    });

                }

            }
        });
    }
    void onItemClicked(int position) {


        Intent itnt = new Intent(FindPersonActivity.this, PersonContentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("content",data.get(position));
        itnt.putExtras(bundle);
        startActivity(itnt);
    }
    void search(String key)
    {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithPartTime("resume/s/" + key)
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(FindPersonActivity.this)
                                .setMessage("当前搜索兼职为空")
                                .show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException

            {
                try {
                    final Page<Resume> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Resume>>() {
                    });
                    FindPersonActivity.this.page = data.getNumber();
                    FindPersonActivity.this.data = data.getContent();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            personAdapter.notifyDataSetChanged();
                        }


                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(FindPersonActivity.this)
                                    .setMessage((CharSequence) e)
                                    .show();
                        }
                    });

                }
            }

        });
    }
}
