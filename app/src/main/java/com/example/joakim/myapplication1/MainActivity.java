package com.example.joakim.myapplication1;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Compass button */
    public void testCompass(View view) {
        Intent intent = new Intent(this, Compass.class);
        startActivity(intent);
    }
    /** Called when the user taps the Accelerometer button */
    public void testAccelerometer(View view) {
        Intent intent = new Intent(this, Accelerometer.class);
        startActivity(intent);
    }

}
