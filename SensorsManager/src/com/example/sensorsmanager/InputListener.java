package com.example.sensorsmanager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class InputListener implements SensorEventListener {

	public float[] previousValues;
	public float[] sensorLectures;
	public int[] codifiedValues;

	public float offsetX, offsetY, offsetZ;
	public float accuracy;
	int min = 0;
	int max = 255;

	public TextView labelAccelerometer;
	public TextView labelAccuracy;
	public TextView labelCod;

	public InputListener() {
		accuracy = 0.0f;
		previousValues = new float[3];
		sensorLectures = new float[3];
		codifiedValues = new int[3];
		for (int axisIndex = 0; axisIndex < 3; axisIndex++) {
			previousValues[axisIndex] = 0;
			sensorLectures[axisIndex] = 0;
			codifiedValues[axisIndex] = 0;
		}

		offsetX = offsetY = offsetZ = 0f;
		accuracy = 0f;
	}

	public void processValues(float[] imputValues) {
		float temporalEventValues[] = imputValues;
		float calibratedEventValues[];

		sensorLectures = imputValues;

		calibratedEventValues = applyOffsetToImputValues(temporalEventValues);
		if (significativeChanges(calibratedEventValues)) {
			codifiedValues = CodificateAndUpdateValues(calibratedEventValues);
		}

		updateInformation(calibratedEventValues);
	}

	public float[] applyOffsetToImputValues(float[] imputValuesToCalibrate) {

		float[] calibratedImputValues = new float[3];
		calibratedImputValues[0] = imputValuesToCalibrate[0] + offsetX;
		calibratedImputValues[1] = imputValuesToCalibrate[1] + offsetY;
		calibratedImputValues[2] = imputValuesToCalibrate[2] + offsetZ;

		return calibratedImputValues;
	}

	public boolean significativeChanges(float[] calibratedEventValues) {
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

		for (int axisIndex = 0; axisIndex < 3; axisIndex++) {
			if (calibratedEventValues[axisIndex] != 0) {
				temporalCodifiedValue = (int) ((calibratedEventValues[axisIndex] + 10) / 0.078125);

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
			processValues(event.values);
		}
	}

	public float modifyAccuracy(String action) {
		if (action.equals("IncreaseAccuracy")) {
			accuracy -= 0.01f;
			accuracy = Math.max(accuracy, 0.0f);

		} else {
			if (action.equals("ReduceAccuracy")) {
				accuracy += 0.01f;
				accuracy = Math.min(accuracy, 10.5f);

			}
		}
		return accuracy;
	}

	public float[] setBaseValues() {
		float[] testResult = new float[3];
		if (sensorLectures != null) {
			offsetX = -sensorLectures[0];
			offsetY = -sensorLectures[1];
			offsetZ = -sensorLectures[2];
			testResult[0] = offsetX;
			testResult[1] = offsetY;
			testResult[2] = offsetZ;
		}
		return testResult;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
