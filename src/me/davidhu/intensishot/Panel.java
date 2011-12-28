package me.davidhu.intensishot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

class Panel extends View {

	private final float ZERO_METER = 0;
	private final float INTENSITY_OFFSET = 10; // so that the angles make sense
	
	private float angle; 

	public Panel(Context context) {
		super(context);
		angle = ZERO_METER;
	}

	public Panel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		angle = ZERO_METER;
	}
	public Panel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onDraw(Canvas canvas) {
		Bitmap needleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.needle_tilted);
		Bitmap meterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meter);

		Matrix rotationMatrix = new Matrix();
		if(angle > ZERO_METER) {
			if(angle > 40) {
				angle -= 8;
			} else {
				angle *= .7; // damping as it gets close to 0
			}
		}
		if(angle < ZERO_METER) {
			angle = ZERO_METER;
		}
		rotationMatrix.postRotate(angle, 185, 97);
		rotationMatrix.postTranslate(30, 114);

		canvas.drawBitmap(needleBitmap, rotationMatrix, null);

		canvas.drawBitmap(meterBitmap, 0, 0, null);
	}

	// sets the angle given some intensity
	public void setAngle(float intensity) {
		if ((intensity*INTENSITY_OFFSET)+ZERO_METER > angle) angle = (intensity*10)+ZERO_METER;
		if(angle > 128) angle = 128;
		
		System.out.println("intensity is "+intensity+" angle is "+angle);
	}

}
