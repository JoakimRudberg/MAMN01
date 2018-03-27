package com.example.joakim.myapplication1;


import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private TextView TextViewX, TextViewY, TextViewZ, TextViewDir;
    public SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean hasSensor = false;
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
        start();
    }

    protected void onPause() {
        super.onPause();
        stop();
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
                direction = "VÄNSTER, UPP";
                if(zValue > 5){
                    direction = "VÄNSTER, UPP, FRAM";
                } else if (zValue < -5){
                    direction = "VÄNSTER, UPP, BAK";
                }
            } else if (yValue < -2.5) {
                direction = "VÄNSTER OCH NER";
                if(zValue > 5){
                    direction = "VÄNSTER, NER, FRAM";
                } else if (zValue < -5){
                    direction = "VÄNSTER, NER, BAK";
                }
            }
        } else if (xValue < -2.5){
            direction = "HÖGER";
            if (yValue > 2.5) {
                direction = "HÖGER, UPP";
                if(zValue > 5){
                    direction = "HÖGER, UPP, FRAM";
                } else if (zValue < -5){
                    direction = "HÖGER, UPP, BAK";
                }
            } else if (yValue < -2.5) {
                direction = "HÖGER, NER";
                if(zValue > 5){
                    direction = "HÖGER, NER, FRAM";
                } else if (zValue < -5){
                    direction = "HÖGER, NER, BAK";
                }
            }
        } else if (yValue > 2.5){
            direction = "UPP";
            if(zValue > 5){
                direction = "UPP, FRAM";
            } else if (zValue < -5){
                direction = "UPP, BAK";
            }
        } else if (yValue < -2.5){
            direction = "NER";
            if(zValue > 5){
                direction = "NER, FRAM";
            } else if (zValue < -5){
                direction = "NER, BAK";
            }
        } else if (zValue > 5){
            direction = "FRAM";
        } else if (zValue < -5){
            direction = "BAK";
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

    private void start() {
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            noSensorsAlert();
        } else {
            hasSensor = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

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

    private void stop() {

        // Unregister sensors
        if (hasSensor) {
            sensorManager.unregisterListener(this, accelerometerSensor);
        }
    }

}
