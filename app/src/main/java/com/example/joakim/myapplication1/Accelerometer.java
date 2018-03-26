package com.example.joakim.myapplication1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
    private float[] preValues = new float[3];
    private String[] direction = {null, null};

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

        // Adds a low pass filter to the sensor data
        // values = event.values.clone();
        values = lowPass.lowPassFilter(event.values.clone(), values);

        // Get x, y and z coordinates
        float xValue = values[0];
        float yValue = values[1];
        float zValue = values[2];

        // Calculate change in x and y coordinates
        float xChange = preValues[0] - xValue;
        float yChange = preValues[1] - yValue;
        float zChange = preValues[2] - zValue;

        // Save history for coordinates
        preValues[0] = xValue;
        preValues[1] = yValue;
        preValues[2] = zValue;

        // Check if it moved left or right
        if (xChange > 0.1) {
            direction[0] = "left";
        } else if (xChange < -0.1) {
            direction[0] = "right";
        } else {
            direction[0] = null;
        }

        // Check if it moved up or down
        if (yChange > 0.1) {
            direction[1] = "down";
        } else if (yChange < -0.1) {
            direction[1] = "up";
        } else {
            direction[1] = null;
        }

        // Set coordinate text views
        TextViewX.setText(getString(R.string.coordinate, "X", Float.toString(xValue)));
        TextViewY.setText(getString(R.string.coordinate, "Y", Float.toString(yValue)));
        TextViewZ.setText(getString(R.string.coordinate, "Z", Float.toString(zValue)));

        // Print direction in direction text view
        if (direction[0] != null || direction[1] != null) {
            if (direction[0] != null && (direction[1] != null)) {
                TextViewDir.setText(getString(R.string.twoDirections, direction[0], direction[1]));
            } else if (direction[0] != null) {
                TextViewDir.setText(getString(R.string.oneDirection, direction[0]));
            } else if (direction[1] != null) {
                TextViewDir.setText(getString(R.string.oneDirection, direction[1]));
            }
        } else {
            TextViewDir.setText(getString(R.string.noDirection));
        }
    }

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

}
