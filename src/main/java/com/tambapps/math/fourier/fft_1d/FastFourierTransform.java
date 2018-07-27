package com.tambapps.math.fourier.fft_1d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.PowerOfTwo;
import com.tambapps.math.util.Vector;

//from https://rosettacode.org/wiki/Fast_Fourier_transform#Java
public class FastFourierTransform {

  /**
   * Compute the 1D FFT in the given array
   * @param array the discrete function to compute the DFT
   */
  public static void iterativeFFT(Vector<Complex> array) {
    int bits = PowerOfTwo.getExponent(array.getSize());
    bitReverseArray(array, bits);

    int n = array.getSize();

    for (int m = 2; m <= n ; m*=2) {
      double dM = (double)m;
      for (int i = 0; i < n; i++) {
        for (int k = 0; k < m / 2; k++) {
          int evenIndex = i + k;
          int oddIndex = i + k + (m / 2);
          Complex even = array.getElement(evenIndex);
          Complex odd = array.getElement(oddIndex);

          Complex wm = Complex.expI(-2 * Math.PI * k / dM).mul(odd);
          array.setElement(evenIndex, wm.add(even));
          array.setElement(oddIndex, odd.sub(wm));
        }
      }
    }
  }

  private static int bitReversedIndex(int i, int bits) {
    int reversedN = i;
    int count = bits - 1;

    i >>= 1;
    while (i > 0) {
      reversedN = (reversedN << 1) | (i & 1);
      count--;
      i >>= 1;
    }

    return ((reversedN << count) & ((1 << bits) - 1));
  }

  private static void bitReverseArray(Vector<Complex> buffer, int bits) {
    for (int j = 1; j < buffer.getSize() / 2; j++) {
      int swapPos = bitReversedIndex(j, bits);
      Complex temp = buffer.getElement(j);
      buffer.setElement(j, buffer.getElement(swapPos));
      buffer.setElement(swapPos, temp);
    }
  }

  public static void inverse(Vector<Complex> array) {
    for (int i = 0; i < array.getSize(); i++) {
      array.setElement(i, array.getElement(i).conj());
    }

    iterativeFFT(array);

    double iN = 1d / ((double)array.getSize());
    for (int i = 0; i < array.getSize(); i++) {
      array.setElement(i, array.getElement(i).conj().scl(iN));
    }
  }
}
