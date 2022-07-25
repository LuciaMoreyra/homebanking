package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils{

    private static Random random = new Random();

    // >= min, < max
    public static int getRandomNumber(int min, int max) {
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public static String getCardNumber() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 4; i++) {
            sb.append(Integer.toString(getRandomNumber(1000, 10000)));
        }
        return sb.toString();
    }

    public static int getCvv() {
        return CardUtils.getRandomNumber(100, 1000);
    }

private CardUtils(){

}

}