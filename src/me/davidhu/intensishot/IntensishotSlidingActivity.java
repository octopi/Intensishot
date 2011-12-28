package me.davidhu.intensishot;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Playing around with new UX
 * 
 * @author d
 *
 */
public class IntensishotSlidingActivity extends Activity {
	
	private SlidingPanel panel;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sliding_main);
		
		
	}
}
