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

    public class NumberMakerToThousand {

        private final String zero = "ноль";
        private final String[] to19 = {"", "один", "два", "три", "четыре",
                "пять", "шесть", "семь", "восемь", "девять",
                "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать",
                "пятнадцать", "шестнадцать", "семнадцать", "восемьнадцать", "девятнадцать"};
        private final String[] to90 = {"", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят",
                "семьдесят", "восемьдесят", "девяносто"};
        private final String[] to900 = {"", "сто", "двести", "триста", "четыреста", "пятьсот",
                "шестьсот", "семьсот", "восемьсот", "девятьсот"};
        private final String thousand = "тысяча";
        private String[] components = {"", "", "", ""};
        private int to19index = 0;
        private int to90index = 0;
        private int to900index = 0;
        private boolean isfullcycle = false;
        private boolean iszero = false;

        NumberMakerToThousand(int init) throws BadInitException {
            if (init <= 1000 && init >= 0) {
                if (init == 1000) {
                    isfullcycle = true;
                } else {
                    if (init != 0) {
                        int hundreds = init / 100;
                        if (hundreds != 0) {
                            to900index = hundreds;
                            components[1] = to900[to900index];
                        }
                        int residue = init % 100;
                        if (residue < 20 && residue != 0) {
                            to19index = residue;
                            components[3] = to19[to19index];
                        } else {
                            int tens = residue / 10;
                            residue %= 10;
                            to90index = tens - 1;
                            components[2] = to90[to90index];
                            to19index = residue;
                            components[3] = to19[to19index];
                        }
                    } else {
                        iszero = true;
                    }
                }
            } else {
                throw new BadInitException(init);
            }
        }

        public String showNumber() {
            if (isfullcycle) {
                return thousand;
            }
            if (iszero) {
                iszero = false;
                return zero;
            }
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                if (!components[i].isEmpty()) {
                    res.append(components[i] + " ");
                }
            }
            return res.toString();
        }

        public void next() {
            if (isfullcycle) {
                iszero = true;
                isfullcycle = false;
            }
            if (!iszero) {
                if (to90index == 0) {
                    to19index = (to19index + 1) % 20;
                } else {
                    to19index = (to19index + 1) % 10;
                }
            }
            if (to19index == 0) {
                to90index = (to90index + 1) % 10;
                if (to90index == 0) {
                    to900index = (to900index + 1) % 10;
                }
                if (to900index == 0) {
                    isfullcycle = true;
                }
            }
        }

        class BadNumberException extends Exception {
            Integer got;

            BadNumberException(int num) {
                got = num;
            }
        }

        class BadInitException extends BadNumberException {
            BadInitException(int num) {
                super(num);
            }

            public String toString() {
                return "Expected init number between 0 and 1000 got " + got.toString();
            }
        }
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
                                final NumberMakerToThousand counter = new NumberMakerToThousand(0);
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tw.setText(counter.showNumber());

                                    }
                                });
                                counter.next();
                            }
                        } catch (InterruptedException | NumberMakerToThousand.BadInitException e) {
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
