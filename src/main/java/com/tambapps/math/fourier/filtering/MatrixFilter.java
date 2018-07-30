package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Double2DArray;
import com.tambapps.math.complex.Complex;

public class MatrixFilter extends AbstractFilter {
  private final Double2DArray array;

  MatrixFilter(Double2DArray array) {
    this.array = array;
  }

  @Override
  Complex apply(Complex c, int i, int j, int M, int N) {
    return c.scl(array.get(i, j));
  }
}
