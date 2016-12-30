package com.example.fourpeople.campushousekeeper.mall.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/26.
 */

public class GoodsAvatar extends View {
/*
* 绘制正方形的图片
* */

    Paint paint;
    float srcWidth, srcHeight;
    Handler mainThreadHandler = new Handler();

    public GoodsAvatar(Context context) {
        super(context);
    }

    public GoodsAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GoodsAvatar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setBitmap(Bitmap bmp) {
        paint = new Paint();
        paint.setShader(new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        paint.setAntiAlias(true);//使用抗锯齿功能
        //paint.setStyle(Paint.Style.STROKE);//设置填充方式为描边
        srcWidth = bmp.getWidth();
        srcHeight = bmp.getHeight();
        invalidate();
    }

    public void load(String avatarAddress) {
        Request request = new Request.Builder()
                .url(Server.serverAddress + avatarAddress)
                .method("get", null)
                .build();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                try {
                    byte[] bytes = arg1.body().bytes();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mainThreadHandler.post(new Runnable() {
                        public void run() {
                            if (bmp != null && !bmp.isRecycled()) {
                                setBitmap(bmp);
                            } else {
                                //1.没有头像的情况，绘制默认头像，从资源中获取Bitmap
                                Resources res = getResources();
                                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app);
                                setBitmap(bmp);

                            }

                        }
                    });
                } catch (Exception ex) {
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //1.没有头像的情况，绘制默认头像，从资源中获取Bitmap
                            Resources res = getResources();
                            Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app);
                            setBitmap(bmp);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                //1连接错误，绘制默认头像，从资源中获取Bitmap
                Resources res = getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app);
                setBitmap(bmp);
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (paint != null) {
            canvas.save();
            float dstWidth = getWidth();
            float dstHeight = getHeight();
            float scaleX = srcWidth / dstWidth;
            float scaleY = srcHeight / dstHeight;
            canvas.scale(1 / scaleX, 1 / scaleY);
           // canvas.drawCircle(srcWidth / 2, srcHeight / 2, Math.min(srcWidth, srcHeight) / 2, paint);
            canvas.drawRect(0,0,srcWidth,srcHeight,paint);
            canvas.restore();
        }
    }


}
