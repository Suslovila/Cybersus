package com.suslovila.cybersus.utils;

import java.util.Random;

public class SusUtils {
    public static final Random random = new Random();

    public static double nextDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}
