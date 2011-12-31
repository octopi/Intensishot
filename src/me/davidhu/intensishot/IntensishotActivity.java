package me.davidhu.intensishot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.RotateDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Html;

public class IntensishotActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	// UI components
	private Button button;
	private ImageView meterImage;
	private RotateDrawable rotator; 
	private Panel meterPanel;

	private float[] zeroes; // original zeroed states of sensors
	private float[] maxDiff; // max difference between zeroed and some observed state
	
	// this will continually be replaced as we log more "significant" pounds
	private CountDownTimer endingTimer;
	
	private final int SCORE_DIALOG = 0;
	private float score;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		button = (Button) findViewById(R.id.button);
		/*TextView instr = (TextView) findViewById(R.id.instructions);
		instr.setBackgroundResource(R.color.black);*/

		// initialize the sensors
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		
		meterPanel = (Panel) findViewById(R.id.meterPanel);
		
		zeroes = null;
		
		endingTimer = null;

		button.setOnClickListener(new View.OnClickListener() {

			/**
			 * After clicking, start countdown. 
			 */
			public void onClick(View v) {
				
				// reset diffs for each run
				maxDiff = new float[3];
				maxDiff[0] = 0;
				maxDiff[1] = 0;
				maxDiff[2] = 0;
				
				new CountDownTimer(4000, 1000) {

					@Override
					/**
					 * After countdown, actually start the sensors. 
					 */
					public void onFinish() {
						System.out.println("countdown finished");
						meterPanel.stopCountdown();
						startSensing();
						restartEndTimer();
					}

					@Override
					public void onTick(long msLeft) {
						System.out.println(String.valueOf(((int)msLeft/1000)));
						if(meterPanel.countingDown()) {
							meterPanel.decreaseCountdown();
						} else {
							meterPanel.startCountdown();
						}
						meterPanel.invalidate();
					}

				}.start();
				
				button.setClickable(false);
			}
		});       
	}

	private void startSensing() {
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		meterPanel.lightMeter();
	}
	
	private void stopSensing() {
		mSensorManager.unregisterListener(this);
		meterPanel.dimMeter();
		meterPanel.invalidate();
		zeroes = null; // reset so it'll recalibrate next time
		button.setClickable(true);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		stopSensing();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			
			// recalibrate on each new run
			if(zeroes == null) {
				zeroes = new float[3];
				zeroes[0] = values[0];
				zeroes[1] = values[1];
				zeroes[2] = values[2];
			}
			
			float xDiff = Math.abs(values[0]-zeroes[0]);
			float yDiff = Math.abs(values[1]-zeroes[1]);
			float zDiff = Math.abs(values[2]-zeroes[2]);
			
			meterPanel.invalidate(); // tell the panel to draw itself again

			if(xDiff > maxDiff[0]) {
				maxDiff[0] = xDiff;
				meterPanel.setAngle(xDiff+yDiff+zDiff);
				restartEndTimer();
			}
			if(yDiff > maxDiff[1]) {
				maxDiff[1] = yDiff;
				meterPanel.setAngle(xDiff+yDiff+zDiff);
				restartEndTimer();
			}
			if(zDiff > maxDiff[2]) {
				maxDiff[2] = zDiff;
				meterPanel.setAngle(xDiff+yDiff+zDiff);
				restartEndTimer();
			} 	
		}
	}
	
	/**
	 * Restarts the timer that will end the sensing. 
	 */
	private void restartEndTimer() {
		System.out.println("inside restart");
		if(endingTimer != null) endingTimer.cancel();
		endingTimer = new CountDownTimer(5000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				System.out.println(String.valueOf(((int)millisUntilFinished/1000)));
			}
			
			@Override
			public void onFinish() {
				stopSensing();
				
				// display score
				score = maxDiff[0]+maxDiff[1]+maxDiff[2];
				System.out.println("score: "+score);
				showDialog(SCORE_DIALOG);
				
			}
		}.start();
	}
	
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		switch(id) {
		case SCORE_DIALOG:
			dialog = new Dialog(this);

			dialog.setContentView(R.layout.dialog);
			dialog.setTitle("Your Intensishot Rating: "+Math.floor(score));

			TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
			if(score < 5) {
				text.setText(Html.fromHtml("<center><big><b>Meh.</b></big></center><br /><br />Is that seriously the best you can do?"));
			} else if(score < 10) {
				text.setText(Html.fromHtml("<center><big><b>Nice!</b></big></center><br /><br />You guys know how to take some shots... now for some more!"));
			} else if(score < 15) {
				text.setText(Html.fromHtml("<center><big><b>True champs!</b></big></center><br /><br />Nobody goes as hard as you. Hats off."));
			} else {
				text.setText(Html.fromHtml("<center><big><b>Inhuman!</b></big></center><br /><br />Damn. Chill with the roids, man."));
			}
			ImageView image = (ImageView) dialog.findViewById(R.id.dialog_logo);
			image.setImageResource(R.drawable.icon);
			Button button = (Button) dialog.findViewById(R.id.dialog_button);
			button.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					dialog.dismiss();
					removeDialog(SCORE_DIALOG);
				}
			});
			break;
		default:
			dialog = null;	
		}
		return dialog;
	}

}
