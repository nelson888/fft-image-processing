package com.tambapps.math.fourier.filtering;

import com.tambapps.math.complex.Complex;

class RectangleFilter extends AbstractFilter {

  private final int width;
  private final int height;
  private boolean reversed;

  RectangleFilter(int width, int height, boolean reversed) {
    this.width = width;
    this.height = height;
    this.reversed = reversed;
  }

  @Override
  Complex apply(Complex c, int i, int j, int M, int N) {
    if (i >= width / 2 || i < M - width / 2 ||
    j >= height / 2 || j < N - height / 2) {
      return reversed ? Complex.ZERO : c;
    }
    return reversed ? c : Complex.ZERO;
  }

}
