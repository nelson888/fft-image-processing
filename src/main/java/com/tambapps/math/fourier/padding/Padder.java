package com.tambapps.math.fourier.padding;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

/**
 * Util class to create a copy of a 2DArray padded with zeros
 * (zero-padding)
 */
public class Padder {

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
