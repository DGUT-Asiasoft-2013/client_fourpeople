package com.example.fourpeople.campushousekeeper.person;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


public class AvatarView extends View {
	public AvatarView(Context context) {
		super(context);
	}
	
	public AvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	Paint paint;
	float srcWidth, srcHeight;
	float radius;
	Handler mainThreadHandler = new Handler();
	
	public void setBitmap(Bitmap bmp){
//		if (bmp==null)  //δ�ϴ�ͷ�����ʾ
//		{
//			paint = new Paint();
//			paint.setColor(Color.GRAY);  //����ɫ
//			paint.setStyle(Paint.Style.STROKE);//����Ϊ����
//			paint.setStrokeWidth(1);
//			paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
//			paint.setAntiAlias(true);
//		}
//		else
//		{
//			paint = new Paint();
//			paint.setShader(new BitmapShader(bmp, TileMode.REPEAT, TileMode.REPEAT));
//			paint.setAntiAlias(true);
//			
//			srcWidth = bmp.getWidth();
//			srcHeight = bmp.getHeight();
//			
//		}
		
		paint = new Paint();
		paint.setShader(new BitmapShader(bmp, TileMode.REPEAT, TileMode.REPEAT));
		radius = Math.min(bmp.getWidth(), bmp.getHeight())/2;
		invalidate();
	}
	
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(paint!=null){
			canvas.save();
			canvas.drawCircle(getWidth()/2, getHeight()/2, radius, paint);
//			float dstWidth = getWidth();
//			float dstHeight = getHeight();
//		
//			float scaleX = srcWidth / dstWidth;
//			float scaleY = srcHeight / dstHeight;
//			
//			canvas.scale(1/scaleX, 1/scaleY);
//			canvas.drawCircle(srcWidth/2, srcHeight/2, Math.min(srcHeight, srcWidth)/2, paint);
			canvas.restore();
		}
		
}
}
