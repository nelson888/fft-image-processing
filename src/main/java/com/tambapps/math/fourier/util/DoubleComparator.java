package com.tambapps.math.fourier.util;

public class DoubleComparator {

  private static final double COMPARE_THRESHOLD = 0.0001;

  public static boolean equal(double d1, double d2) {
    return Math.abs(d1 - d2) < COMPARE_THRESHOLD;
  }

}
