package com.example.sensorsmanager;

import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView textViewAccuracy, textViewImputValues, textViewCodified;
	Button button, plus, less, stop;
	Sensor accelerometer;
	SensorManager sensorManager;
	InputListener inputListenerInstance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textViewAccuracy = (TextView) findViewById(R.id.label1);
		textViewImputValues = (TextView) findViewById(R.id.label2);
		textViewCodified = (TextView) findViewById(R.id.label3);

		// Create input listener
		inputListenerInstance = new InputListener();
		inputListenerInstance.labelAccelerometer = textViewAccuracy;
		inputListenerInstance.labelAccuracy = textViewImputValues;
		inputListenerInstance.labelCod = textViewCodified;

		// Set button action
		setButtonAction();

		// Set up accelerometer
		createAccelerometer();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set sensor events
		sensorManager.registerListener(inputListenerInstance, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(inputListenerInstance);
	}

	private void setButtonAction() {
		button = (Button) findViewById(R.id.buttonSetBaseValues);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (inputListenerInstance != null) {
					inputListenerInstance.setBaseValues();
				}
			}
		});

		plus = (Button) findViewById(R.id.buttonAccuracyPlus);
		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(inputListenerInstance != null){
					inputListenerInstance.modifyAccuracy("IncreaseAccuracy");
				}
			}
		});

		less = (Button) findViewById(R.id.buttonAccuracyLess);
		less.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(inputListenerInstance != null){
					inputListenerInstance.modifyAccuracy("ReduceAccuracy");
				}
			}
		});
	}

	private void createAccelerometer() {
		// Start sensors
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

