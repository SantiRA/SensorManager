package com.example.sensorsmanager;

import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView label1, label2, label3;
	Button button, plus, less, stop;
	Sensor accelerometer;
	SensorManager sensorManager;
	InputListener il;	
	
	int valor;
	
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
 
        label1 = (TextView)findViewById(R.id.label1);   
        label2 = (TextView)findViewById(R.id.label2);
        label3 = (TextView)findViewById(R.id.label3);
        
        //	Create input listener
        il = new InputListener();
        il.labelAccelerometer = label1;
        il.labelAccuracy = label2;
        il.labelCod = label3;
        
        //	Set button action
        setButtonAction();
        
        //	Set up accelerometer
        createAccelerometer();        
    }
   
   @Override
   public void onStart() {
	   super.onStart();
   }
   
   @Override
   protected void onResume() {
	   super.onResume();
	   //Set sensor events
       sensorManager.registerListener( il, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
   }
   
   @Override
   protected void onPause() {
	   super.onPause();
	   sensorManager.unregisterListener(il);
   }
   
   private void setButtonAction() {
	   button = (Button) findViewById(R.id.buttonSetBaseValues);
	   button.setOnClickListener(il);
	   
	   plus = (Button) findViewById(R.id.buttonAccuracyPlus);
	   plus.setOnClickListener(il);
	   
	   less = (Button) findViewById(R.id.buttonAccuracyLess);
	   less.setOnClickListener(il);	   
   }
   
      
   private void createAccelerometer() {
	   //Start sensors
       sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
       accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
   }
}