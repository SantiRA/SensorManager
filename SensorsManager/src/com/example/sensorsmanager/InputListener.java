package com.example.sensorsmanager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.TextView;

public class InputListener implements SensorEventListener, View.OnClickListener {

	float x,y,z, xtemp, ytemp, ztemp;
	float tx, ty, tz;
	float xx, yy, zz;
	float accuracy,show;
	float codx,cody,codz;
	int codx2,cody2,codz2;
	int min=0;
	int max = 255;
	
	public TextView labelAccelerometer;
	public TextView labelAccuracy;
	public TextView labelCod;
	int ultim_enviat = 10, valor_mov = 10;
	boolean enviar = true;

	
	public InputListener() {	
		x = y = z = 0f;
		codx = cody = codz = 0;
		xtemp = ytemp = ztemp = 0f;
		xx = yy = zz = 0f;
		accuracy = 0f;
		show =0f;

	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			tx = event.values[0];
			ty = event.values[1];
			tz = event.values[2];
			xtemp = tx + xx;
			ytemp = ty + yy; 
			ztemp = tz + zz;
			if((xtemp > x+accuracy || xtemp < x-accuracy) || (ytemp > y+accuracy || ytemp < y-accuracy) || (ztemp > z+accuracy || ztemp < z-accuracy)){
				x = xtemp;
				y = ytemp;
				z = ztemp;
				codx = x;
				cody = y;
				codz = z;
				
				if (codx != 0){
					codx2 = (int) ((codx+10)/0.078125);
					if (codx2 < min)
						codx2 = min;
					if (codx2 > max)
						codx2 = max;
				}
				else 
					codx2 = 128;

				
				
				if (cody != 0){
					cody2 = (int) ((cody+10)/0.078125);
					if (cody2 < min)
						cody2 = min;
					if (cody2 > max)
						cody2 = max;
				}
				else 
					cody2 = 128;
				
				
				if (codz != 0){
					codz2 = (int) ((codz+10)/0.078125);
					if (codz2 < min)
						codz2 = min;
					if (codz2 > max)
						codz2 = max;
				}
				else 
					codz2 = 128;			
			}	
		}
		if(labelAccelerometer != null)
			labelAccelerometer.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
		if(labelAccuracy != null)
			if(accuracy == 0){
				labelAccuracy.setText("Accuracy: 100%, corrector value = 0");
			}
			else{
				show = (float) (100-(100*accuracy)/10.5);
				labelAccuracy.setText("Accuracy: " + show + "%, corrector value = " + accuracy);
			}
		if(labelCod != null){
			labelCod.setText("Codification: X: " + codx2 + ", Y: " + cody2 + ", Z: " + codz2);
		}
	}			
	
	
	public void onClick(View v) {
		
		switch(v.getId())
		{
			case R.id.buttonSetBaseValues:  
				xx = -tx;
				yy = -ty;
				zz = -tz;
			break;
			case R.id.buttonAccuracyLess:
				accuracy += 0.01f; 
				accuracy = Math.min(accuracy, 10.5f);
			break;
			case R.id.buttonAccuracyPlus:
				accuracy -= 0.01f;
				accuracy = Math.max(accuracy, 0f);
			break;		
		}		
	}
}