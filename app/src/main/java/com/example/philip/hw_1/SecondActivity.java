package com.example.philip.hw_1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    Button button;
    TextView tw;
    Thread counterThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        button = (Button) findViewById(R.id.button);
        tw = (TextView) findViewById(R.id.tw);
    }

    public void count(View view) {

        if (button.getText().equals("Start")) {

            button.setText(R.string.stop);
            (counterThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 1000; ++i) {
                        try {
                            if (button.getText().equals("Start")) { // stop counting on pressing "Stop"
                                return;
                            } else {
                                final int counter = i;
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tw.setText(String.valueOf(counter));

                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setText(R.string.start);
                            tw.setText("");
                        }
                    });
                }
            })).start();
        } else {
            button.setText(R.string.start);
            tw.setText("");
            counterThread.interrupt();
        }
    }
}
