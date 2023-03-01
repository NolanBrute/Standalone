package com.example.standalone.objects;

public class Utils {

    /**
     * getDistanceBetweenPoints mengembalikan jarak antara objek 2d
     * @param p1x
     * @param p1y
     * @param p2x
     * @param p2y
     * @return
     */

    public static double getDistanceBetweenPoints(double p1x, double p1y, double p2x, double p2y) {
        return Math.sqrt(
                Math.pow(p1x - p2x, 2) +
                Math.pow(p1y - p2y, 2)
        );

    }
}
