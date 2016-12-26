package com.example.fourpeople.campushousekeeper.mall.view;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.example.fourpeople.campushousekeeper.fragment.inputcells.BaseInputCellFragment;

import java.io.ByteArrayOutputStream;

public class MallPictureInputFragment extends BaseInputCellFragment {

    final int REQUEST_CAMERA = 1;
    final int REQUEST_ALBUM = 2;

    ImageView imageView;
    TextView labelText;
    TextView hintText;

    byte[] pngData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mall_fragment_inputcell_picture, container);

        imageView = (ImageView) view.findViewById(R.id.image);
        labelText = (TextView) view.findViewById(R.id.label);
        hintText = (TextView) view.findViewById(R.id.hint);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onImageViewClicked();

            }
        });

        return view;
    }

    void onImageViewClicked() {
        String[] items = {"拍照", "相册"};

        new AlertDialog.Builder(getActivity())
                .setTitle(hintText.getText())
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                pickFromAlbum();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    void takePhoto() {
        Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(itnt, REQUEST_CAMERA);
    }

    void pickFromAlbum() {
        Intent itnt = new Intent(Intent.ACTION_GET_CONTENT);
        itnt.setType("image/*");
        startActivityForResult(itnt, REQUEST_ALBUM);
    }

    void saveBitmap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        pngData = baos.toByteArray();
    }

    public byte[] getPngData() {
        return pngData;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (resultCode == Activity.RESULT_CANCELED) return;

        if (requestCode == REQUEST_CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            saveBitmap(bmp);

            imageView.setImageBitmap(bmp);
        } else if (requestCode == REQUEST_ALBUM) {
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                saveBitmap(bmp);

                imageView.setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void setLabelText(String labelText) {
        this.labelText.setText(labelText);
    }

    public void setHintText(String hintText) {
        this.hintText.setText(hintText);
    }


}