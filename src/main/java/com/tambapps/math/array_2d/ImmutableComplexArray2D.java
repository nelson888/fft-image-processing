package com.tambapps.math.array_2d;

import com.tambapps.math.complex.Complex;

public class ImmutableComplexArray2D extends Complex2DArray {

  public ImmutableComplexArray2D(int M, int N, Complex[] values) {
    super(M, N, values);
  }

  @Override
  public void set(int i, Complex value) {
    throw new UnsupportedOperationException("Cannot modify values of Immutable array");
  }

  @Override
  public void set(int row, int col, Complex value) {
    throw new UnsupportedOperationException("Cannot modify values of Immutable array");
  }
}
