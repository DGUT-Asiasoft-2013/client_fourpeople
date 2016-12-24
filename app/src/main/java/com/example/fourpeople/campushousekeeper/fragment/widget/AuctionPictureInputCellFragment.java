package com.example.fourpeople.campushousekeeper.fragment.widget;

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

import com.example.fourpeople.campushousekeeper.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AuctionPictureInputCellFragment extends Fragment {

    final int REQURSTCODE_CAMERA = 1;
    final int REQURSTCODE_ALBUM = 2;
    ImageView choose;


    public byte[] getPngData() {
        return pngData;
    }

    byte[] pngData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inputcell_picture_auction, container);
        choose = (ImageView) view.findViewById(R.id.iv_choose);
        //labelText = (TextView) view.findViewById(R.id.tv_clabel);


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageViewClicked();
            }
        });
        return view;
    }


    public void setLabelText(String labelText) {
        //  this.labelText.setText(labelText);
    }

    public void onImageViewClicked() {
        String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(getActivity()).setTitle("物品照片").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        pickFormAlbum();
                        break;
                }
            }


        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bmp;
        if (requestCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQURSTCODE_CAMERA) {
            bmp = (Bitmap) data.getExtras().get("data");
            saveBitmap(bmp);
            choose.setImageBitmap(bmp);
        } else if (requestCode == REQURSTCODE_ALBUM) {
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                saveBitmap(bmp);
                choose.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void takePhoto() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle(e.getMessage()).setMessage("请选择相册图片").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pickFormAlbum();
                }
            }).show();
        }

    }

    void pickFormAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    void saveBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        pngData = byteArrayOutputStream.toByteArray();
    }

}
