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
	private final float INTENSITY_OFFSET = 10; // so that the angles make sense given a physical stimulus
	
	private float angle; 
	
	private Matrix rotationMatrix;
	
	private Bitmap needleBitmap;
	private Bitmap meterDim;
	private Bitmap meterLit;
	private Bitmap currMeter;


	public Panel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		angle = ZERO_METER;
		
		needleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.needle_tilted);
		meterDim = BitmapFactory.decodeResource(getResources(), R.drawable.meter_dim);
		meterLit = BitmapFactory.decodeResource(getResources(), R.drawable.meter_lit);
		currMeter = meterDim;
	}
	
	public void onDraw(Canvas canvas) {
		rotationMatrix = new Matrix();
		
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
		
		rotationMatrix.postRotate(angle, 185, 97); // rotating to given angle
		rotationMatrix.postTranslate(30, 114); // constant offset

		canvas.drawBitmap(needleBitmap, rotationMatrix, null);

		canvas.drawBitmap(currMeter, 0, 0, null);
	}
	
	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(431, 220); // dimensions of meter
	}

	// sets the angle given some intensity
	public void setAngle(float intensity) {
		if ((intensity*INTENSITY_OFFSET)+ZERO_METER > angle) angle = (intensity*10)+ZERO_METER;
		if(angle > 128) angle = 128;
		
		System.out.println("intensity is "+intensity+" angle is "+angle);
	}
	
	public void lightMeter() {
		currMeter = meterLit;
	}
	public void dimMeter() {
		currMeter = meterDim;
	}

}
