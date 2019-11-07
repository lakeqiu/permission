package com.lakeqiu.utils;

import java.util.Random;

/**
 * @author lakeqiu
 */
public class PasswordUtil {
    private final static Integer PASSWORDLENGTH = 8;

    private final static String[] WORD = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    private final static String[] NUM = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String randomPassword(){
        StringBuilder buffer = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < PASSWORDLENGTH; i++){
            if (random.nextBoolean()){
                buffer.append(WORD[random.nextInt(WORD.length)]);
            } else {
                buffer.append(NUM[random.nextInt(NUM.length)]);
            }
        }
        return buffer.toString();
    }
}
