package com.tambapps.math.array_2d;

import com.tambapps.math.complex.Complex;

public class Complex2DArray extends Array2D<Complex> {

  public Complex2DArray(int M, int N) {
    super(M, N);
  }

  Complex2DArray(int M, int N, Complex[] values) {
    super(M, N, values);
  }

  @Override
  Complex[] initialize(int size) {
    return new Complex[size];
  }

  public static Complex2DArray immutableCopy(Complex2DArray array) {
    return new ImmutableComplexArray2D(array.getM(), array.getN(), filledArray(array));
  }

  public static Complex2DArray copy(Complex2DArray array) {
    return new Complex2DArray(array.getM(), array.getN(), filledArray(array));
  }

  private static Complex[] filledArray(Complex2DArray array) {
    Complex[] complexes = new Complex[array.getM() * array.getN()];
    for (int i = 0; i < array.getM() * array.getN(); i++) {
      complexes[i]  = array.get(i);
    }
    return complexes;
  }

}
