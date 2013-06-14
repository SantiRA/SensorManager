package com.example.sensorsmanager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class InputListener implements SensorEventListener {
	private float[] offset;
	private float[] previousValues;
	private float[] sensorLectures;
	private int[] codifiedValues;
	private boolean newBaseValues;
	private float accuracy;
	private static final int min = 0;
	private static final int max = 255;

	public TextView labelAccelerometer;
	public TextView labelAccuracy;
	public TextView labelCod;

	public InputListener() {

		accuracy = 0.0f;
		offset = new float[3];
		previousValues = new float[3];
		sensorLectures = new float[3];
		codifiedValues = new int[3];
		newBaseValues = false;
		for (int axisIndex = 0; axisIndex < 3; axisIndex++) {
			previousValues[axisIndex] = 0.0f;
			sensorLectures[axisIndex] = 0.0f;
			codifiedValues[axisIndex] = 0;

			offset[axisIndex] = 0.0f;
		}
	}

	public void processValues() {
		float temporalEventValues[] = sensorLectures;
		float calibratedEventValues[];

		calibratedEventValues = applyOffsetToImputValues(temporalEventValues);
		if (significativeChanges(calibratedEventValues)) {
			codifiedValues = CodificateAndUpdateValues(calibratedEventValues);
		}

		updateInformation(calibratedEventValues);
	}

	public float[] applyOffsetToImputValues(float[] imputValuesToCalibrate) {

		float[] calibratedImputValues = new float[3];
		calibratedImputValues[0] = imputValuesToCalibrate[0] + offset[0];
		calibratedImputValues[1] = imputValuesToCalibrate[1] + offset[1];
		calibratedImputValues[2] = imputValuesToCalibrate[2] + offset[2];

		return calibratedImputValues;
	}

	public boolean significativeChanges(float[] calibratedEventValues) {
		if (newBaseValues) {
			newBaseValues = false;
			return true;
		}
		for (int axisIndex = 0; axisIndex < 3; axisIndex++) {
			if (calibratedEventValues[axisIndex] > previousValues[axisIndex]
					+ accuracy
					|| calibratedEventValues[axisIndex] < previousValues[axisIndex]
							- accuracy) {
				return true;
			}
		}
		return false;
	}

	public int[] CodificateAndUpdateValues(float[] calibratedEventValues) {

		int temporalCodifiedValue;
		int[] temporalCodifiedValues = new int[3];
		double slope = 0.078125;

		for (int axisIndex = 0; axisIndex < 3; axisIndex++) {
			if (calibratedEventValues[axisIndex] != 0) {
				temporalCodifiedValue = (int) ((calibratedEventValues[axisIndex] + 10) / slope);

				if (temporalCodifiedValue < min)
					temporalCodifiedValues[axisIndex] = min;

				else if (temporalCodifiedValue > max)
					temporalCodifiedValues[axisIndex] = max;

				else
					temporalCodifiedValues[axisIndex] = temporalCodifiedValue;
			} else
				temporalCodifiedValues[axisIndex] = 128;
		}

		return temporalCodifiedValues;
	}

	public void updateInformation(float[] calibratedEventValues) {
		if (labelAccelerometer != null) {
			labelAccelerometer.setText("X: " + calibratedEventValues[0]
					+ "\nY: " + calibratedEventValues[1] + "\nZ: "
					+ calibratedEventValues[2]);
		}

		if (labelAccuracy != null) {
			float showPercentageAccuracy = (float) (100 - ((100 * accuracy) / 10.5));
			if (accuracy == 0) {
				showPercentageAccuracy = 0;
			}
			labelAccuracy.setText("Accuracy: " + showPercentageAccuracy
					+ "%, corrector value = " + accuracy);
		}

		if (labelCod != null) {
			labelCod.setText("Codification: X: " + codifiedValues[0] + ", Y: "
					+ codifiedValues[1] + ", Z: " + codifiedValues[2]);
		}
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			sensorLectures = event.values;
			processValues();
		}
	}

	public float modifyAccuracy(String action) {
		if (action.equals("IncreaseAccuracy")) {
			accuracy -= 0.01f;
			accuracy = Math.max(accuracy, 0.0f);
			return accuracy;
		} else {
			if (action.equals("ReduceAccuracy")) {
				accuracy += 0.01f;
				accuracy = Math.min(accuracy, 10.5f);
				return accuracy;
			}
		}
		return accuracy;
	}

	public float[] setBaseValues() {
		if (sensorLectures != null) {
			offset[0] = -sensorLectures[0];
			offset[1] = -sensorLectures[1];
			offset[2] = -sensorLectures[2];
		}
		return offset;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
