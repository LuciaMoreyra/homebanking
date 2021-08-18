package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils{

    // >= min, < max
    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public static String geCardNumber() {
        String number = "";
        for (int i = 0; i < 4; i++) {
            number += Integer.toString(getRandomNumber(1000, 10000));
        }
        return number;
    }

    public static int getCvv() {
        return CardUtils.getRandomNumber(100, 1000);
    }

private CardUtils(){

}

}