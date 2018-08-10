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

  public static int getClosestSuperior(int number) {
    if (number > LIST[LIST.length - 1]) {
      throw new IllegalArgumentException("number is superior of max exponent");
    }
    for (int i = 1; i < LIST.length; i++) {
      int inf = LIST[i - 1];
      int sup = LIST[i];
      if (number >= inf && number <= sup) {
        return sup;
      }
    }
    return LIST[LIST.length - 1];
  }
  public static boolean isPowerOfTwo(int number) {
    return Arrays.binarySearch(LIST, number) >= 0;
  }
}
