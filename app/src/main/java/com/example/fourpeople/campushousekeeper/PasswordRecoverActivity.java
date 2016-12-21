package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.fragment.PasswordRecoverStep1Fragment;
import com.example.fourpeople.campushousekeeper.fragment.PasswordRecoverStep2Fragment;
import com.example.fourpeople.campushousekeeper.information.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/20.
 */

public class PasswordRecoverActivity extends Activity {
    PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
    PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);

        step1.setOnGoNextListener(new PasswordRecoverStep1Fragment.OnGoNextListener() {

            @Override
            public void onGoNext() {
                goStep2();

            }
        });

        step2.setOnSubmitClickedListener(new PasswordRecoverStep2Fragment.OnSubmitClickedListener() {

            @Override
            public void onSubmitClicked() {
                goSubmit();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
    }

    void goStep2() { // 切换fragment并使用动画渐变效果
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, step2)
                .addToBackStack(null).commit();
    }

    void goSubmit() {
        String email = step1.getEmail();
        String password = step2.getPassword();
        String passwordRepeat = step2.getPasswordRepeat();
        //检查邮箱和密码是否已填写
        if (email.equals("") || password.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("未填写邮箱或密码！")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return;
        }

        if (!password.equals(passwordRepeat)) {
            new AlertDialog.Builder(PasswordRecoverActivity.this)
                    .setMessage("两次输入密码不一致！")
                    .show();
            return;
        }

        OkHttpClient client = Server.getSharedClient();
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("email", step1.getEmail())
                .addFormDataPart("passwordHash", MD5.getMD5(step2.getPassword()))
                .build();
        Request request = Server.requestBuildWithApi("passwordrecover").post(body).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {

                try {
                    final Boolean succeed = new ObjectMapper().readValue(arg1.body().bytes(), Boolean.class);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (succeed) {
                                new AlertDialog.Builder(PasswordRecoverActivity.this)
                                        .setTitle("请求成功")
                                        .setMessage("修改密码成功！")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                            } else {
                                new AlertDialog.Builder(PasswordRecoverActivity.this)
                                        .setTitle("请求成功")
                                        .setMessage("修改密码失败！")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                            }

                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(PasswordRecoverActivity.this, "邮箱错误！", Toast.LENGTH_SHORT).show();
                        }
                    });

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new AlertDialog.Builder(PasswordRecoverActivity.this)
                                .setTitle("请求失败")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                });

            }
        });
    }
}