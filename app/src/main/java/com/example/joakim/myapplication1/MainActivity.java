package com.example.joakim.myapplication1;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void testCompass(View view) {
        Intent intent = new Intent(this, testCompass.class);
        startActivity(intent);
    }

    public void testAccelerometer(View view) {
        Intent intent = new Intent(this, testAccelerometer.class);
        startActivity(intent);
    }

}
