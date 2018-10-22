package com.tambapps.math.util;

public class DoubleTester {

  private static double EQUAL_THRESHOLD = 0.000001;

  public static boolean equal(double d1, double d2) {
    return Math.abs(d1 - d2) < EQUAL_THRESHOLD;
  }

  public static void setThreshold(double threshold) {
    EQUAL_THRESHOLD = threshold;
  }
}
