package com.tambapps.math.fourier.util;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

/**
 * Util functions for Fourier Transform
 */
public class FFTUtils {

  public static void changeCenter(Complex2DArray array) {
    for (int i = 0; i < array.getN() / 2; i++) {
      for (int j = 0; j < array.getM() / 2; j++) {
        swap(array, i, j, i + array.getN() / 2 , j + array.getM() / 2);
        swap(array, i, j + array.getM() / 2, i + array.getN() / 2, j);
      }
    }
  }

  private static void swap(Complex2DArray array, int i1, int j1, int i2, int j2) {
    Complex temp = array.get(i1, j1);
    array.set(i1, j1, array.get(i2, j2));
    array.set(i2, j2, temp);
  }

  /**
   * make a padded copy of the given array
   * (zero-padding => raise resolution)
   * @param array an array of image pixels
   * @param paddingM the padding for the rows
   * @param paddingN the padding for the columns
   * @return the padded copy of the given array
   */
  public static Complex2DArray paddedCopy(Complex2DArray array, int paddingM, int paddingN) {
    Complex2DArray copy = new Complex2DArray(array.getM() + 2 * paddingM, array.getN() + 2 * paddingN);
    for (int i = 0; i < copy.getM(); i++) {
      for (int j = 0; j < copy.getN(); j++) {
        if (i < paddingM || i >= paddingM + array.getM() ||
            j < paddingN || j >= paddingN + array.getN()) {
          copy.set(i, j, Complex.ZERO);
        } else {
          copy.set(i, j, array.get(i - paddingM, j - paddingN));
        }
      }
    }
    return copy;
  }

  public static Complex2DArray unpaddedCopy(Complex2DArray array, int paddingM, int paddingN) {
    Complex2DArray copy = new Complex2DArray(array.getM() - 2 * paddingM, array.getN() - 2 * paddingN);
    for (int i = 0; i < copy.getM(); i++) {
      for (int j = 0; j < copy.getN(); j++) {
        copy.set(i, j, array.get(i + paddingM, j + paddingN));
      }
    }
    return copy;
  }

}
