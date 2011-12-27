package me.davidhu.intensishot;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntensishotActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    
    private TextView countdownTimer;
    
    private TextView xText;
    private TextView yText;
    private TextView zText;
    
    private Button button;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        countdownTimer = (TextView) findViewById(R.id.countdownTimer);
        
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        
        button = (Button) findViewById(R.id.button);
        
        // start listening to sensor data at the start... is there a better way to do this?
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				countdownTimer.setText("Clicked!");
				CountDownTimer myTimer = new CountDownTimer(5000, 1000) {

					@Override
					public void onFinish() {
						
					}

					@Override
					public void onTick(long msLeft) {
						countdownTimer.setText(String.valueOf((int)msLeft/1000));
					}
					
				}.start();
			}
		});
        
        //xText.setText("yo");
        
        
    }

    protected void onResume() {
        super.onResume();
        System.out.println("HERERERERE");
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    	if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
    		float[] values = event.values;
    		//System.out.println(values[0]);
			xText.setText(String.valueOf(values[0]));
			yText.setText(String.valueOf(values[1]));
			zText.setText(String.valueOf(values[2]));
    	}
    }

}
