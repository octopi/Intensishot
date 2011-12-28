package me.davidhu.intensishot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

class SlidingPanel extends View {

	private final float ZERO_METER = 0;
	private final float INTENSITY_OFFSET = 10; // so that the angles make sense
	
	private float angle; 

	public SlidingPanel(Context context) {
		super(context);
		angle = ZERO_METER;
	}

	public SlidingPanel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		angle = ZERO_METER;
	}
	public SlidingPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onDraw(Canvas canvas) {
		Bitmap needleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.needle_tilted);
		Bitmap meterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meter);

		Matrix rotationMatrix = new Matrix();
		rotationMatrix.postRotate(angle, 185, 97);
		rotationMatrix.postTranslate(30, 114);

		canvas.drawBitmap(needleBitmap, rotationMatrix, null);
		canvas.drawBitmap(meterBitmap, 0, 0, null);
		
		
	}
}
