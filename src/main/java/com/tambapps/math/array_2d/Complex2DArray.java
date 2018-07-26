package com.tambapps.math.array_2d;

import com.tambapps.math.complex.Complex;

public class Complex2DArray extends Array2D<Complex> {

  public Complex2DArray(int M, int N) {
    super(M, N);
  }

  @Override
  Complex[] initialize(int size) {
    return new Complex[size];
  }

}
