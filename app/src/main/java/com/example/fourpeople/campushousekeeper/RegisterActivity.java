package com.example.fourpeople.campushousekeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.PictureInputCellFragment;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.SimpleTextInputCellFragment;
import com.example.fourpeople.campushousekeeper.information.MD5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

    SimpleTextInputCellFragment fragInputStudentId;
    SimpleTextInputCellFragment fragInputName;
    SimpleTextInputCellFragment fragInputCellPassword;
    SimpleTextInputCellFragment fragInputCellPasswordRepeat;
    Spinner spinnerInputSex;

    SimpleTextInputCellFragment fragInputEmail;
    SimpleTextInputCellFragment fragInputAddress;
    SimpleTextInputCellFragment fragInputTel;
    PictureInputCellFragment fragInputAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragInputStudentId = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);
        fragInputEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
        fragInputName = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_name);
        fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
        spinnerInputSex = (Spinner) findViewById(R.id.input_sex);
        fragInputAddress = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_address);
        fragInputTel = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_tel);
        fragInputAvatar = (PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.input_avatar);

        findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置项目名
        fragInputStudentId.setLabelText("学号");
        fragInputStudentId.setHintText("请输入学号");
        fragInputName.setLabelText("昵称");
        fragInputName.setHintText("请输入昵称");
        fragInputCellPassword.setLabelText("密码");
        fragInputCellPassword.setHintText("请输入密码");
        fragInputCellPassword.setIsPassword(true);
        fragInputCellPasswordRepeat.setLabelText("重复密码");
        fragInputCellPasswordRepeat.setHintText("请重复密码");
        fragInputCellPasswordRepeat.setIsPassword(true);
        fragInputEmail.setLabelText("电子邮箱");
        fragInputEmail.setHintText("请输入电子邮箱");
        fragInputAddress.setLabelText("你的地址");
        fragInputAddress.setHintText("请输入地址");
        fragInputTel.setLabelText("电话");
        fragInputTel.setHintText("请输入电话");
    }

    void submit()
    {
        String password = fragInputCellPassword.getText();
        String passwordRepeat = fragInputCellPasswordRepeat.getText();

        if (!password.equals(passwordRepeat))
        {
            new AlertDialog.Builder(RegisterActivity.this)
                    .setMessage("重复密码不一致，")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton("OK", null)
                    .show();

            return;
        }

        password = MD5.getMD5(password);

        String studentId = fragInputStudentId.getText();
        String name = fragInputName.getText();
        String email = fragInputEmail.getText();
        String sex = (String) spinnerInputSex.getSelectedItem();

        OkHttpClient client = Server.getSharedClient();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("studentId", studentId)
                .addFormDataPart("name", name)
                .addFormDataPart("email", email)
                .addFormDataPart("passwordHash", password);

        if (fragInputAvatar.getPngData()!=null )  //上传照片
        {
            requestBodyBuilder.addFormDataPart("avatar",
                    "avatar",
                    RequestBody
                            .create(MediaType.parse("image/png"), fragInputAvatar.getPngData()));

        }

        Request request = Server.requestBuildWithApi("register")
                .method("post", null)
                .post(requestBodyBuilder.build())
                .build();

        //进度对话框ProgressDialog显示“请稍候”
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("请稍候");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        client.newCall(request).enqueue(new Callback() {
            //处理结果
            @Override
            public void onResponse(final Call arg0, final Response arg1) throws IOException {
                final String responseString = arg1.body().string();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        try {
                            RegisterActivity.this.onResponse(arg0, responseString);
                        } catch (Exception e) {
                            e.printStackTrace();
                            RegisterActivity.this.onFailure(arg0, e);
                        }

                    }
                });

            }

            @Override
            public void onFailure(final Call arg0, final IOException arg1) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        RegisterActivity.this.onFailure(arg0, arg1);
                    }
                });

            }
        });

    }

    void onResponse(Call arg0, String responseBody)//注册成功提示
    {
        new AlertDialog.Builder(this)
                .setTitle("注册成功")
                .setMessage(responseBody)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    void onFailure(Call arg0, Exception arg1)//注册失败提示
    {
        new AlertDialog.Builder(this)
                .setTitle("请求失败")
                .setMessage(arg1.getLocalizedMessage())
                .setNegativeButton("好", null)
                .show();

    }
}