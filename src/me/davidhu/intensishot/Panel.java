package me.davidhu.intensishot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

class Panel extends View {

	private final float ZERO_METER = 0;
	private final float INTENSITY_OFFSET = 10; // so that the angles make sense given a physical stimulus
	
	private float angle; 
	
	private Matrix rotationMatrix;
	
	private Bitmap needleBitmap;
	private Bitmap[] meters;
	private Bitmap meterLit;
	private Bitmap currMeter;
	private Paint textPaint;
	private boolean countdown;
	private int currCountdown;


	public Panel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		angle = ZERO_METER;
		
		needleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.needle_tilted);
		meters = new Bitmap[4];
		meters[0] = BitmapFactory.decodeResource(getResources(), R.drawable.meter_dim0);
		meters[1] = BitmapFactory.decodeResource(getResources(), R.drawable.meter_dim1);
		meters[2] = BitmapFactory.decodeResource(getResources(), R.drawable.meter_dim2);
		meters[3] = BitmapFactory.decodeResource(getResources(), R.drawable.meter_dim3);
		meterLit = BitmapFactory.decodeResource(getResources(), R.drawable.meter_lit);
		
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(72);
		textPaint.setAntiAlias(true);
		countdown = false;
		currCountdown = 3;
		
		currMeter = meters[0];
	}
	
	public void onDraw(Canvas canvas) {
		System.out.println(canvas.getDensity());
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
		
		//rotationMatrix.postRotate(angle, 185, 97); // rotating to given angle
		System.out.println(canvas.getWidth()+"x"+canvas.getHeight());
		/*rotationMatrix.postRotate(angle, 170*(480/canvas.getWidth()), 90*(480/canvas.getWidth())); // rotating to given angle
		rotationMatrix.postTranslate(40*(480/(canvas.getWidth()+100)), 118*(800/(canvas.getHeight()+400))); // constant offset*/
		rotationMatrix.postRotate(angle, 110, 54); // rotating to given angle
		rotationMatrix.postTranslate(36, 84); // constant offset

		canvas.drawBitmap(needleBitmap, rotationMatrix, null);

		canvas.drawBitmap(currMeter, 0, 0, null);
		
	}
	
	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//setMeasuredDimension(431, 220); // dimensions of meter
	}

	// sets the angle given some intensity
	public void setAngle(float intensity) {
		if ((intensity*INTENSITY_OFFSET)+ZERO_METER > angle) angle = (intensity*10)+ZERO_METER;
		if(angle > 128) angle = 128;
		
		System.out.println("intensity is "+intensity+" angle is "+angle);
	}
	
	public void startCountdown() {
		countdown = true;
		currCountdown = 1;
		currMeter = meters[1];
	}
	public void stopCountdown() {
		countdown = false;
	}
	public void decreaseCountdown() {
		if(currCountdown < 3) {
			currCountdown++;
			currMeter = meters[currCountdown];
		}
	}
	public boolean countingDown() {
		return countdown;
	}
	
	
	public void lightMeter() {
		currMeter = meterLit;
	}
	public void dimMeter() {
		currMeter = meters[0];
	}

}
