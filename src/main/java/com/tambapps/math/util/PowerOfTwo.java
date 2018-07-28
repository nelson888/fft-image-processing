package com.tambapps.math.util;

import java.util.Arrays;

public class PowerOfTwo {

  //supports images up to 4096
  private static final int[] LIST = new int[]{1, 2, 4, 8, 16, 32, 64,
      128, 256, 512, 1024, 2048, 4096};

  public static int getExponent(int powerOfTwo) {
    int p = Arrays.binarySearch(LIST, powerOfTwo);
    if (p < 0) {
      throw new IllegalArgumentException(powerOfTwo + " is not a power of two");
    }
    return p;
  }

}
