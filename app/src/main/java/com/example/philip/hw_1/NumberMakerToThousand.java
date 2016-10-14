package com.example.philip.hw_1;

class NumberMakerToThousand {

    private final String zero = "ноль";
    private final String[] to19 = {"", "один", "два", "три", "четыре",
            "пять", "шесть", "семь", "восемь", "девять",
            "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать",
            "пятнадцать", "шестнадцать", "семнадцать", "восемьнадцать", "девятнадцать", "ноль"};
    private final String[] to90 = {"", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят",
            "семьдесят", "восемьдесят", "девяносто"};
    private final String[] to900 = {"", "сто", "двести", "триста", "четыреста", "пятьсот",
            "шестьсот", "семьсот", "восемьсот", "девятьсот", "тысяча"};
    private final String thousand = "тысяча";
    private String[] components = {"", "", ""};
    private int to19index = 0;
    private int to90index = 0;
    private int to900index = 0;

    NumberMakerToThousand(int init) throws BadInitException {
        if (init <= 1000 && init >= 0) {
            if (init == 1000) {
                to900index = 10; //тысяча
            } else {
                if (init == 0) {
                    to19index = 20; //ноль
                } else {
                    int hundreds = init / 100;
                    if (hundreds != 0) {
                        to900index = hundreds;
                        components[0] = to900[to900index];
                    }
                    int residue = init % 100;
                    if (residue < 20 && residue != 0) {
                        to19index = residue;
                        components[2] = to19[to19index];
                    } else {
                        int tens = residue / 10;
                        residue %= 10;
                        to90index = tens - 1;
                        components[1] = to90[to90index];
                        to19index = residue;
                        components[2] = to19[to19index];
                    }
                }
            }
        } else {
            throw new BadInitException(init);
        }
    }

    String showNumber() {
        StringBuilder res = new StringBuilder();
        components[0] = to900[to900index];
        components[1] = to90[to90index];
        components[2] = to19[to19index];
        for (int i = 0; i < 3; i++) {
            if (!components[i].isEmpty()) {
                res.append(components[i]).append(" ");
            }
        }
        return res.toString();
    }

    NumberMakerToThousand next() {
        if (to19index == 20) { //если было значение ноль,
            to19index = 0;
        }
        if (to900index == 10) { //если предыдущее значение было тысяча, то следующее это 0
            to900index = 0;
            to19index = 20;
            return this;
        }
        if (to90index == 0) {
            to19index = (to19index + 1) % 20; //если число десятков меньше 2
        } else {
            to19index = (to19index + 1) % 10;
        }
        if (to19index == 0) {
            to90index = (to90index + 1) % 9;
            if (to90index == 0) {
                to900index = (to900index + 1) % 10;
                if (to900index == 0) {
                    to900index = 10;
                }
            }
        }
        return this;
    }

    static class BadNumberException extends Exception {
        Integer got;

        BadNumberException(int num) {
            got = num;
        }
    }

    public static class BadInitException extends BadNumberException {
        BadInitException(int num) {
            super(num);
        }

        public String toString() {
            return "Expected init number between 0 and 1000 got " + got.toString();
        }
    }
}
