package com.example.fourpeople.campushousekeeper.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.mall.view.MallPictureInputFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/20.
 */

public class OpenShopActivity extends Activity {
    MallPictureInputFragment pictureInputCellFragment;
    EditText shopNameEdit;
    Spinner shopTypeSpinner;
    Button okBtn;
    String shopType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_openshop);
        pictureInputCellFragment = (MallPictureInputFragment) getFragmentManager().findFragmentById(R.id.openShop_avatar);
        shopNameEdit = (EditText) findViewById(R.id.openShop_edit_name);
        shopTypeSpinner = (Spinner) findViewById(R.id.openShop_spinner_type);
        okBtn = (Button) findViewById(R.id.openShop_btn_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        //获取选择的微店类型
        shopTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shopType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //创建微店按钮方法
    void submit() {
        String shopName = shopNameEdit.getText().toString();
        if (shopName.equals("") || shopType.equals("请选择分类:")) {
            new AlertDialog.Builder(OpenShopActivity.this)
                    .setMessage("请输入微店名称或者选择微店类型!")
                    .setNegativeButton("确认", null)
                    .show();
            return;
        }
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("shopName", shopName)
                .addFormDataPart("shopType", shopType);
        //加载图片数据
        if (pictureInputCellFragment.getPngData() != null) {
            body.addFormDataPart(
                    "shopAvatar",
                    "shopAvatar",
                    RequestBody.create(MediaType.parse("image/png"),
                            pictureInputCellFragment.getPngData()));
        }
        Request request = Server.requestBuildWithMall("open")
                .method("post", null)
                .post(body.build())
                .build();
        final ProgressDialog progressDialog = new ProgressDialog(OpenShopActivity.this);
        progressDialog.setMessage("创建中...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(OpenShopActivity.this)
                                .setTitle("连接失败")
                                .setMessage("网络连接失败...\n" + e.getLocalizedMessage())
                                .setPositiveButton("确认", null)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(OpenShopActivity.this)
                                .setTitle("连接成功")
                                .setMessage("创建微店成功！")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        pictureInputCellFragment.setLabelText("微店头像：");
        super.onResume();
    }
}
