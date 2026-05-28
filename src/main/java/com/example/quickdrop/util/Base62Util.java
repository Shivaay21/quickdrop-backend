package com.example.quickdrop.util;

public class Base62Util {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(Long number){
        StringBuilder shortCode = new StringBuilder();
        if (number == 0) return "0";
        while(number > 0){
            int remainder = (int) (number % 62);
            shortCode.append(BASE62.charAt(remainder));
            number /= 62;
        }
        return shortCode.reverse().toString();
    }
}
