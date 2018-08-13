package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

abstract class AbstractFilter implements Filter {

  @Override
  public final void apply(Complex2DArray array) {
    final int M = array.getM();
    final int N = array.getN();
    before(M, N);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        array.set(i, j, apply(array.get(i, j), i, j, M, N));
      }
    }
  }

  void before(int M, int N) {}

  @Override
  public final Complex2DArray applied(Complex2DArray array) {
    final int M = array.getM();
    final int N = array.getN();
    Complex2DArray result = new Complex2DArray(M, N);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        result.set(i, j, apply(array.get(i, j), i, j, M, N));
      }
    }
    return result;
  }

  abstract Complex apply(Complex c, int i, int j, int M, int N);

}
