package com.example.philip.hw_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        })).start();
    }
}
