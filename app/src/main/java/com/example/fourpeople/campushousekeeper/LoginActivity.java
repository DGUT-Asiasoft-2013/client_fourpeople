package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.SimpleTextInputCellFragment;
import com.example.fourpeople.campushousekeeper.information.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
    SimpleTextInputCellFragment fragAccount,fragPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            //点击注册按钮进入RegisterActivity
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            //点击登录按钮进入HelloWorldActivity
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });

        findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {
            //点击忘记密码文本进入密码重置
            @Override
            public void onClick(View v) {
                goRecoverPassword();
            }
        });

        fragAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
        fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置项目名
        fragAccount.setLabelText("账户名");
        fragAccount.setHintText("请输入账户名");
        fragPassword.setLabelText("密码");
        fragPassword.setHintText("请输入密码");
        fragPassword.setIsPassword(true);
    }

    void goRegister(){  //进入注册界面
        Intent itnt = new Intent(this,RegisterActivity.class);
        startActivity(itnt);
    }

    void goLogin(){  //输入用户名、密码进入登录界面

        String account = fragAccount.getText();
        String password = fragPassword.getText();
        //检查用户名密码是否已填
        if (account.equals("") || password.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请填写账号密码")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return ;
        }

        OkHttpClient client = Server.getSharedClient();

        MultipartBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("account", account)
                .addFormDataPart("passwordHash", MD5.getMD5(password))
                .build();

        Request request = Server.requestBuildWithApi("login")
                .method("post", null)
                .post(requestBody)
                .build();

        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setMessage("正在登录");
        dlg.show();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {

                try {//检查用户名、密码是否正确
                    final String responseString = arg1.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    final User user = mapper.readValue(responseString, User.class);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            dlg.dismiss();
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("Hello "+user.getName())
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent itnt = new Intent(LoginActivity.this, BootActivity.class);
                                            startActivity(itnt);

                                        }
                                    }).show();

                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            dlg.dismiss();
                            Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    });

                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call arg0, final IOException arg1) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        dlg.dismiss();
                        Toast.makeText(LoginActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    void goRecoverPassword(){  //进入密码找回界面
        Intent itnt = new Intent(this, PasswordRecoverActivity.class);
        startActivity(itnt);
    }
}
