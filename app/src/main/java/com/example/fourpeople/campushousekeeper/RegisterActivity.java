package com.example.fourpeople.campushousekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.PictureInputCellFragment;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.SimpleTextInputCellFragment;
import com.example.fourpeople.campushousekeeper.information.MD5;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    String spinnerStr;//用来获得当前的性别下拉框的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragInputStudentId = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_student_id);
        fragInputEmail = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
        fragInputName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
        fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
        spinnerInputSex = (Spinner) findViewById(R.id.input_sex);
        fragInputAddress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_address);
        fragInputTel = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_tel);
        fragInputAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_avatar);
//获取下拉框内容
        spinnerInputSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerStr = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerStr = adapterView.getItemAtPosition(0).toString();
            }
        });

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
        fragInputStudentId.setLabelText("学号(必填):");
        fragInputStudentId.setHintText("请输入学号");
        fragInputName.setLabelText("昵称:");
        fragInputName.setHintText("请输入昵称");
        fragInputCellPassword.setLabelText("密码（必填）:");
        fragInputCellPassword.setHintText("请输入密码");
        fragInputCellPassword.setIsPassword(true);
        fragInputCellPasswordRepeat.setLabelText("重复密码（必填）：");
        fragInputCellPasswordRepeat.setHintText("请重复密码");
        fragInputCellPasswordRepeat.setIsPassword(true);
        fragInputEmail.setLabelText("电子邮箱（必填）：");
        fragInputEmail.setHintText("请输入电子邮箱");
        fragInputAddress.setLabelText("联系地址");
        fragInputAddress.setHintText("请输入联系地址");
        fragInputTel.setLabelText("联系方式");
        fragInputTel.setHintText("请输入联系方式");
    }

    void submit() {
        String studentId = fragInputStudentId.getText();
        String name = fragInputName.getText();
        String passwordHash = fragInputCellPassword.getText();
        String passwordRepeat = fragInputCellPasswordRepeat.getText();
        String sex = spinnerStr;
        String email = fragInputEmail.getText();
        String address = fragInputAddress.getText();
        String tel = fragInputTel.getText();

        if (studentId.equals("") || passwordHash.equals("") || passwordRepeat.equals("") || email.equals("")) {
            new AlertDialog.Builder(RegisterActivity.this)
                    .setMessage("请填写必填内容!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton("OK", null)
                    .show();

            return;
        }

        if (!email.matches("^\\w+@\\w+\\.(com|cn)")) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("不是有效的邮箱.")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }

        if (!passwordHash.equals(passwordRepeat)) {
            new AlertDialog.Builder(RegisterActivity.this)
                    .setMessage("重复密码不一致!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton("OK", null)
                    .show();

            return;
        }

        passwordHash = MD5.getMD5(passwordHash);

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("studentId", studentId)
                .addFormDataPart("name", name)
                .addFormDataPart("passwordHash", passwordHash)
                .addFormDataPart("sex", sex)
                .addFormDataPart("email", email)
                .addFormDataPart("address", address)
                .addFormDataPart("tel", tel)
                .addFormDataPart("balance","0");


        if (fragInputAvatar.getPngData() != null)  //上传照片
        {
            requestBodyBuilder.addFormDataPart(
                    "avatar",
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
        progressDialog.setMessage("请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    void onFailure(Call arg0, Exception arg1)//注册失败提示
    {
        new AlertDialog.Builder(this)
                .setTitle("网络连接失败...")
                .setMessage(arg1.getLocalizedMessage())
                .setNegativeButton("OK", null)
                .show();

    }
}