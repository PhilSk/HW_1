package com.example.philip.hw_1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.concurrent.atomic.AtomicBoolean;


public class SecondActivity extends AppCompatActivity {

    Button button;
    TextView tw;
    Thread counterThread;
    AtomicBoolean button_bool = new AtomicBoolean(true);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        button = (Button) findViewById(R.id.button);
        tw = (TextView) findViewById(R.id.tw);
    }

    @Override
    protected void onPause() {
        if (counterThread.isAlive()) {
            counterThread.interrupt();
        }
        super.onStop();
    }

    private boolean button_checker() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (button.getText().equals("Start")){
                    button_bool.set(true);
                } else {
                    button_bool.set(false);
                }
            }
        });
        return button_bool.get();
    }

    public void count(View view) {

        if (button_checker()) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  button.setText(R.string.stop);
                }
            });
            try {
                final NumberMakerToThousand counter = new NumberMakerToThousand(0);
                (counterThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 1000; ++i) {
                            try {
                                if (!button_bool.get()) { // stop counting on pressing "Stop"
                                    return;
                                } else {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tw.setText(counter.next().showNumber());
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

            } catch (NumberMakerToThousand.BadInitException e) {
                e.printStackTrace();
            }

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setText(R.string.start);
                    tw.setText("");
                }
            });
            if (counterThread.isAlive()) {
                counterThread.interrupt();
            }
        }
    }
}
