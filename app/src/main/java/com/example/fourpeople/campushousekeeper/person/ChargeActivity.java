package com.example.fourpeople.campushousekeeper.person;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.person.inputcells.PersonSimpleTextInputCellFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/28.
 */


public class ChargeActivity extends Activity {

    EditText inputCharge;
    TextView btnConsumRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity_charge);

        inputCharge = (EditText) findViewById(R.id.input_charge);
        btnConsumRecord = (TextView) findViewById(R.id.btn_consum_record);
        btnConsumRecord.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        findViewById(R.id.btn_charge_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeSubmit();
            }
        });

        btnConsumRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去消费记录
            }
        });
    }

    void chargeSubmit() {
        String balance = inputCharge.getText().toString();

        if (balance.equals("")) {
            new AlertDialog.Builder(ChargeActivity.this)
                    .setMessage("请先输入数额.")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }

        if (Double.parseDouble(balance)<=0)
        {
            new AlertDialog.Builder(ChargeActivity.this)
                    .setMessage("无效的充值数额.")
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("balance",balance);

        Request request = Server.requestBuildWithApi("charge")
                .method("post", null)
                .post(requestBodyBuilder.build())
                .build();

        //进度对话框ProgressDialog显示“请稍候”
        final ProgressDialog progressDialog = new ProgressDialog(ChargeActivity.this);
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
                            ChargeActivity.this.onResponse(arg0, responseString);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ChargeActivity.this.onFailure(arg0, e);
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
                        ChargeActivity.this.onFailure(arg0, arg1);
                    }
                });

            }
        });
    }

    void onResponse(Call arg0, String responseBody)
    {
        new AlertDialog.Builder(this)
                .setTitle("充值成功")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    void onFailure(Call arg0, Exception arg1)
    {
        new AlertDialog.Builder(this)
                .setTitle("网络异常...")
                .setMessage(arg1.getLocalizedMessage())
                .setNegativeButton("OK", null)
                .show();

    }
}
