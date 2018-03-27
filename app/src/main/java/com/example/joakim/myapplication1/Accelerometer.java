package com.example.joakim.myapplication1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private TextView TextViewX, TextViewY, TextViewZ, TextViewDir;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean hasAccelerometerSensor = false;
    private float[] values = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        TextViewX = findViewById(R.id.TextViewX);
        TextViewY = findViewById(R.id.TextViewY);
        TextViewZ = findViewById(R.id.TextViewZ);
        TextViewDir = findViewById(R.id.TextViewDir);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    protected void onResume() {
        super.onResume();
        startSensors();
    }

    protected void onPause() {
        super.onPause();
        stopSensors();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Save sensor data via lowPassFilter.
        values = lowPassFilter(event.values.clone(), values);

        // Retrieve accelerometer values.
        float xValue = values[0];
        float yValue = values[1];
        float zValue = values[2];

        // Sets direction of the phone based on accelerometer values
        String direction;
        if (xValue > 2.5) {
            direction = "VÄNSTER";
            if (yValue > 2.5) {
                direction = "VÄNSTER OCH UPP";
            } else if (yValue < -2.5) {
                direction = "VÄNSTER OCH NER";
            }
        } else if (xValue < -2.5){
            direction = "HÖGER";
            if (yValue > 2.5) {
                direction = "HÖGER OCH UPP";
            } else if (yValue < -2.5) {
                direction = "HÖGER OCH NER";
            }
        } else if (yValue > 2.5){
            direction = "UPP";
        } else if (yValue < -2.5){
            direction = "NER";
        } else{
            direction = null;
        }

        // Sets texts in TextViews.
        TextViewX.setText(getString(R.string.coordinate, "X", Float.toString(Math.round(xValue))));
        TextViewY.setText(getString(R.string.coordinate, "Y", Float.toString(Math.round(yValue))));
        TextViewZ.setText(getString(R.string.coordinate, "Z", Float.toString(Math.round(zValue))));
        TextViewDir.setText(direction);
    }

    /**
     * Stops the sensors
     */
    private void startSensors() {
        // Try to create accelerometer sensor
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Check if accelerometer sensor was created successfully
        if (accelerometerSensor == null) {
            noSensorsAlert();
        } else {
            hasAccelerometerSensor = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * Stops the sensors
     */
    private void stopSensors() {
        // Unregister sensors
        if (hasAccelerometerSensor) {
            sensorManager.unregisterListener(this, accelerometerSensor);
        }
    }

    /**
     * Shows no sensors alert
     */
    private void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device does not support the desired sensors")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }
    /**
     * Lowpassfilter. Changes a value only if the change is greater than 0.5.
     */
    private static float[] lowPassFilter(float[] currentValue, float[] preValue) {
        if (preValue == null) {
            return currentValue;
        }
        for ( int i=0; i<currentValue.length; i++ ) {
            if ((currentValue[i]-preValue[i]) > 0.5 || (currentValue[i]-preValue[i]) < -0.5){
                preValue[i] = currentValue[i];
            }
        }
        return preValue;
    }

}
