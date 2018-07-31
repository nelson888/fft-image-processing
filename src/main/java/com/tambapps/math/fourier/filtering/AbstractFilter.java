package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

abstract class AbstractFilter implements Filter {

  @Override
  public final void apply(Complex2DArray array) {
    final int M = array.getM();
    final int N = array.getN();
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        array.set(i, j, apply(array.get(i, j), i, j, M, N));
      }
    }
  }

  abstract Complex apply(Complex c, int i, int j, int M, int N);

}
