package me.eltacshikhsaidov.dailytech;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getRandomColor());
        }
    }

    public static String getRandomColor() {

        Random random = new Random();

        String randomColor = "#" + (char)(random.nextInt(26) + 'a') + (int) (Math.random() * 10)
                 + (char)(random.nextInt(26) + 'a') + (int) (Math.random() * 10) +
                (char)(random.nextInt(26) + 'a') + (int) (Math.random() * 10);

        return randomColor;

    }


}
