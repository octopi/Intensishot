package me.davidhu.intensishot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class Panel extends View {

	public Panel(Context context) {
		super(context);
	}
	
	public Panel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public Panel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public void onDraw(Canvas canvas) {
		Bitmap needleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.needle);
		Bitmap meterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meter);
		canvas.drawBitmap(needleBitmap, 202, 0, null);
		canvas.drawBitmap(meterBitmap, 0, 0, null);
	}

}
