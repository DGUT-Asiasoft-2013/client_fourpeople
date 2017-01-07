package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.WantJobPage;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
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
 * Created by Administrator on 2017/1/4.
 */

public class MyResumeFragment extends Fragment {
    Resume resume;
    View view;
    Handler myHandler;
    TextView contentPname,contentPsex,contentPdetails,contentPtime,contentMoney,contentParea,contentPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.part_fragment_myresume, null);
             contentPname = (TextView) view.findViewById(R.id.content_pname);
             contentPsex = (TextView) view.findViewById(R.id.content_Psex);
             contentPdetails = (TextView) view.findViewById(R.id.content_pdetails);
             contentPtime = (TextView) view.findViewById(R.id.content_ptime);
             contentMoney = (TextView) view.findViewById(R.id.content_money);
             contentParea = (TextView) view.findViewById(R.id.content_area);
             contentPhone = (TextView) view.findViewById(R.id.content_phone);
            myHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Resume resumeItem= (Resume) msg.obj;
                    Log.d("resume", "onCreateView: " + resume);
                    contentMoney.setText("期待薪资："
                            + resume.getMoney());
                    contentParea.setText("求职区域：" + resume.getArea());
                    contentPdetails.setText("求职内容：" + resume.getDetails());
                    contentPhone.setText("联系方式：" + resume.getPhone());
                    contentPsex.setText("性别：" + resume.getSex());
                    contentPtime.setText("时间段：" + resume.getTime());
                    contentPname.setText(resume.getName());

                }
            };
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    void reload() {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithPartTime("myResume")
                .method("Get", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //如果是返回一个类的，那么后面直接加“类名.clss”
                    resume = new ObjectMapper().readValue(response.body().string(), Resume.class);
                    Message message = new Message();
                    message.what=0x234;
                    message.obj=resume;
                    myHandler.sendMessage(message);
                    AvatarView avatarView = (AvatarView) view.findViewById(R.id.user_image);
                    avatarView.load(Server.serverAddress + resume.getAvater());

                } catch (final Exception e) {
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
