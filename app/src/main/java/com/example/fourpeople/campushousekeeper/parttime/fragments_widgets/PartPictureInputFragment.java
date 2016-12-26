package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/20.
 */

public class PartPictureInputFragment extends Fragment {
    ImageView imageView;
    TextView labelText;
    TextView hintText;
    final  int REQUESTCODE_CAMERA=1;
    final int REQUESTCODE_ALBUM=2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.part_fragment_inputcell_picture,null);
        imageView=(ImageView) view.findViewById(R.id.image);
        labelText=(TextView)view.findViewById(R.id.label);
        hintText=(TextView)view.findViewById(R.id.hint);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choicePicture();

            }
        });

        return view ;
    }
    void choicePicture() {
        String[] items =
                {
                        "拍照", "相册"
                };

        new AlertDialog.Builder(getActivity())
                .setTitle(labelText.getText())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                pickFromAlbm();
                                break;
                            default:
                                break;
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    void takePhoto()
    {
        //调用系统拍照功能
        Intent itnt=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(itnt,REQUESTCODE_CAMERA);
        //有反馈的意图机制


    }
    void pickFromAlbm()
    {
        Intent itnt=new Intent(Intent.ACTION_GET_CONTENT);
        itnt.setType("image/*");
        startActivityForResult(itnt,REQUESTCODE_ALBUM);

    }

    byte[] pngData;
    //字节组流ByteArrayOutputStream:    可以捕获内存缓冲区的数据，转换成字节数组。
    void saveBitamp(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //android bitmap compress（图片压缩）
        bmp.compress(Bitmap.CompressFormat.PNG,100,baos);
        pngData=baos.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_CANCELED) return;
        //用户拍照后的结果
        if (requestCode==REQUESTCODE_CAMERA)
        {
            Bitmap bmp=(Bitmap) data.getExtras().get("data");
            saveBitamp(bmp);
            imageView.setImageBitmap(bmp);
            //Log.d("camara data",dataObj.getClass().toString());

        }else if (requestCode==REQUESTCODE_ALBUM)
        {
            // Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            Bitmap bmp= null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                saveBitamp(bmp);
                imageView.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(bmp);

        }
        //Log.d("camera capture",data.getExtras().keySet().toString());



    }
    public void setLabelText(String labelText)
    {
        this.labelText.setText(labelText);
    }

    public void setHintText(String hintText)
    {
        this.hintText.setHint(hintText);
    }
    public  byte[] getPngData()
    {
        return pngData;
    }

}
