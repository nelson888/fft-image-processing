package com.tambapps.math.fourier.filtering;

import com.tambapps.math.complex.Complex;

public class RectangleFilter extends AbstractFilter {

  private final int width;
  private final int height;
  private Complex activatedValue, normalValue;

  RectangleFilter(int width, int height, boolean reversed) {

    this.width = width;
    this.height = height;
    activatedValue = reversed ? Complex.ONE : Complex.ZERO;
    normalValue = reversed ? Complex.ZERO : Complex.ONE;
  }

  @Override
  Complex apply(Complex c, int i, int j, int M, int N) {
    if (i >= width / 2 || i < M - width / 2 ||
    j >= height / 2 || j < N - height / 2) {
      return activatedValue;
    }
    return normalValue;
  }

}
