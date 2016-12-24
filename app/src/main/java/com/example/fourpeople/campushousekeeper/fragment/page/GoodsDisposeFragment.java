package com.example.fourpeople.campushousekeeper.fragment.page;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.PictureInputCellFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class GoodsDisposeFragment extends Fragment {
    EditText goodsDispose_name, goodsDispose_piece, goodsDispose_number, goodsDispose_about;
    PictureInputCellFragment goodsDispose_avatar;
    Button goodsPush;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_goodsdispose, null);
            goodsDispose_name = (EditText) view.findViewById(R.id.goodsDispose_name);
            goodsDispose_piece = (EditText) view.findViewById(R.id.goodsDispose_piece);
            goodsDispose_number = (EditText) view.findViewById(R.id.goodsDispose_number);
            goodsDispose_about = (EditText) view.findViewById(R.id.goodsDispose_about);
            goodsDispose_avatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.goodsDispose_avatar);
            goodsPush = (Button) view.findViewById(R.id.goodsDispose_push);
            goodsPush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    push();
                }
            });
        }
        return view;
    }

    //上架商品
    void push() {
        String goodsName = goodsDispose_name.getText().toString();
        String goodsPiece = goodsDispose_piece.getText().toString();
        String goodsNumber = goodsDispose_number.getText().toString();
        String goodsAbout = goodsDispose_about.getText().toString();
        if (goodsName.equals("") || goodsNumber.equals("") || goodsPiece.equals("")) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("请填写必填内容!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton("OK", null)
                    .show();
            return;
        }
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("goodsName", goodsName)
                .addFormDataPart("goodsPiece", goodsPiece)
                .addFormDataPart("goodsNumber", goodsNumber)
                .addFormDataPart("goodsAbout", goodsAbout);
        if (goodsDispose_avatar.getPngData() != null)  //上传照片
        {
            requestBodyBuilder.addFormDataPart(
                    "goodsAvatar",
                    "goodsAvatar",
                    RequestBody
                            .create(MediaType.parse("image/png"), goodsDispose_avatar.getPngData()));
        }
        Request request = Server.requestBuildWithMall("goods/push")
                .method("post", null)
                .post(requestBodyBuilder.build())
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("网络连接失败...")
                                .setMessage(e.getLocalizedMessage())
                                .setNegativeButton("OK", null)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goodsDispose_name.setText("");
                        goodsDispose_piece.setText("");
                        goodsDispose_number.setText("");
                        goodsDispose_about.setText("");

                        Toast.makeText(getActivity(), "上架成功，加油哦！", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        goodsDispose_avatar.setLabelText("商品图片：");
        super.onResume();
    }
}
