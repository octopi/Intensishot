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

	// UI components
	private TextView countdownTimer;
	private TextView xText;
	private TextView yText;
	private TextView zText;
	private Button button;
	private TextView xDiffText;
	private TextView yDiffText;
	private TextView zDiffText;

	private float[] zeroes; // original zeroed states of sensors
	private float[] maxDiff; // max difference between zeroed and some observed state

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
		xDiffText = (TextView) findViewById(R.id.xDiffText);
		yDiffText = (TextView) findViewById(R.id.yDiffText);
		zDiffText = (TextView) findViewById(R.id.zDiffText);

		// initialize the sensors
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		zeroes = null;

		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				countdownTimer.setText("Clicked!");
				CountDownTimer myTimer = new CountDownTimer(5000, 1000) {

					@Override
					public void onFinish() {
						countdownTimer.setText("GO!");
						startSensing();
					}

					@Override
					public void onTick(long msLeft) {
						countdownTimer.setText(String.valueOf((int)msLeft/1000));
					}

				}.start();
			}
		});       
	}

	private void startSensing() {
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onResume() {
		super.onResume();
		System.out.println("HERERERERE");
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
			if(zeroes == null) {
				zeroes = new float[3];
				zeroes[0] = values[0];
				zeroes[1] = values[1];
				zeroes[2] = values[2];
				maxDiff = new float[3];
				maxDiff[0] = 0;
				maxDiff[1] = 0;
				maxDiff[2] = 0;
			}
			float xDiff = Math.abs(values[0]-zeroes[0]);
			float yDiff = Math.abs(values[1]-zeroes[1]);
			float zDiff = Math.abs(values[2]-zeroes[2]);

			xText.setText("x: "+String.valueOf(xDiff));
			yText.setText("y: "+String.valueOf(yDiff));
			zText.setText("z: "+String.valueOf(zDiff));

			if(xDiff > maxDiff[0]) {
				maxDiff[0] = xDiff;
			}
			if(yDiff > maxDiff[1]) {
				maxDiff[1] = yDiff;
			}
			if(zDiff > maxDiff[2]) {
				maxDiff[2] = zDiff;
			}

			xDiffText.setText("x: "+String.valueOf(maxDiff[0]));
			yDiffText.setText("y: "+String.valueOf(maxDiff[1]));
			zDiffText.setText("z: "+String.valueOf(maxDiff[2]));    	
		}
	}

}
