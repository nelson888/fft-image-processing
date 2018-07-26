package com.tambapps.math.fft_2d;

import com.tambapps.math.complex.Complex;

//TODO TO DELETE, USE FastFourierTransformer
//TODO faire des fonctions qui aggrandissent l'image comme dans le TP (permet une plus grande precision)
public class FastFourier {

  public static Complex[][] transform(int[][] f) {
    final double M = f.length;
    final double N = f[0].length;

    Complex[][] fft = new Complex[(int)M][(int)N];

    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        fft[i][j] = transformPoint(M, N, f, i, j);
      }
    }

    return fft;
  }

  private static Complex transformPoint(double M, double N, int[][] f, int m, int n) {
    Complex result = Complex.ZERO;
    for (int i = 0; i < f.length; i++) {
      for (int j=0; j < f[i].length; j++) {
        result = result.add(Complex.expI(-2 * Math.PI * (i*m/M + j*n/N)).scl(f[i][j]));
      }
    }

    return result;
  }

  public static Complex[][] inverse(Complex[][] f) {
    final double M = f.length;
    final double N = f[0].length;

    Complex[][] inverse = new Complex[(int)M][(int)N];
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        inverse[i][j] = inversePoint(M, N, f, i, j);
      }
    }
    return inverse;
  }

  private static Complex inversePoint(double M, double N, Complex[][] f, int m, int n) {
    Complex result = Complex.ZERO;
    for (int i = 0; i < f.length; i++) {
      for (int j=0; j < f[i].length; j++) {
        result = result.add(Complex.expI(2 * Math.PI * (i*m/M + j*n/N)).mul(f[i][j]));
      }
    }

    return result.scl(1d / (M * N));
  }
}
