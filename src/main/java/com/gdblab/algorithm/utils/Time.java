package com.gdblab.algorithm.utils;

public class Time {

    public static String getTimeBetween(long start, long end) {
        return String.format("%.3f", (end - start) / 1000000000.0);
    }
}
