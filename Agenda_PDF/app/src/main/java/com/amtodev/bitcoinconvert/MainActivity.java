package com.amtodev.bitcoinconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask Splash = new TimerTask(){
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Contact.class);
                startActivity(intent);
                finish();
            }
        };

        Timer time = new Timer();
        time.schedule(Splash, 4000);
    }
}