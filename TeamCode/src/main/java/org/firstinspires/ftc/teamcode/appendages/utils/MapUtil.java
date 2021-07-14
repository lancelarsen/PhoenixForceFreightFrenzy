package org.firstinspires.ftc.teamcode.appendages.utils;

public class MapUtil {
    public static double map(double oldValue, double oldMin, double oldMax, double newMin, double newMax) {
        return (((oldValue - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }
}
