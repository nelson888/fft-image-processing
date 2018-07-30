package com.tambapps.math.fourier.padding;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

/**
 * Util class to create a copy of a 2DArray padded with zeros
 * (zero-padding)
 */
public class Padder {

  public static Complex2DArray copy(Complex2DArray array, int padding) {
    Complex2DArray copy = new Complex2DArray(array.getM() + 2 * padding, array.getN() + 2 * padding);
    for (int i = 0; i < copy.getM(); i++) {
      for (int j = 0; j < copy.getN(); j++) {
        if (i < padding || i >= padding + array.getM() ||
        j < padding || j >= padding + array.getN()) {
          copy.set(i, j, Complex.ZERO);
        } else {
          copy.set(i, j, array.get(i - padding, j - padding));
        }
      }
    }
    return copy;
  }
}
