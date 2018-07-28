package com.tambapps.math.fourier.fft_1d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.ArrayVector;
import com.tambapps.math.util.PowerOfTwo;
import com.tambapps.math.util.Vector;

//from https://rosettacode.org/wiki/Fast_Fourier_transform#Java
public class FastFourierTransform {

  public static void basicFFT(Vector<Complex> array, Vector<Complex> result) {
    double N = array.getSize();
    for (int k = 0; k < array.getSize(); k++) {
      Complex sum  = Complex.ZERO;
      for (int n = 0; n < array.getSize(); n++) {
        sum = sum.add(array.getElement(n).mul(Complex.expI(- 2d * Math.PI * ((double)k) * ((double)n) / N)));
      }
      result.setElement(k, sum);
    }
  }

    /**
     * Compute the 1D FFT in the given array
     * @param array the discrete function to compute the DFT
     */
  public static void iterativeFFT(Vector<Complex> array) {
    int n = array.getSize();

    int bits = PowerOfTwo.getExponent(n);
    bitReverseArray(array, bits);

    for (int m = 2; m <= n; m <<= 1) {
      double dM = (double)m;
      for (int i = 0; i < n; i += m) {
        for (int k = 0; k < m / 2; k++) {
          int evenIndex = i + k;
          int oddIndex = i + k + (m / 2);
          Complex even = array.getElement(evenIndex);
          Complex odd = array.getElement(oddIndex);

          Complex wm = Complex.expI(-2d * Math.PI * k / dM).mul(odd);
          array.setElement(evenIndex, even.add(wm));
          array.setElement(oddIndex, even.sub(wm));
        }
      }
    }
  }

  public static int bitReversedIndex(int n, int bits) {
    int reversedN = n;
    int count = bits - 1;

    n >>= 1;
    while (n > 0) {
      reversedN = (reversedN << 1) | (n & 1);
      count--;
      n >>= 1;
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

  public static void recursiveFFT(Vector<Complex> array) {
    Vector.copy(recursiveCopyFFT(array), array);
  }

  public static Vector<Complex> recursiveCopyFFT(Vector<Complex> array) {
    int N = array.getSize();
    if (N <= 1) {
      return array;
    }
    Vector<Complex> evens = recursiveCopyFFT(evensCopy(array));
    Vector<Complex> odds = recursiveCopyFFT(oddsCopy(array));

    Complex[] T = new Complex[N/2];
    for (int i = 0; i < N / 2; i++) {
      T[i] = odds.getElement(i).mul(Complex.expI(- 2d *  Math.PI * ((double)i) / ((double)N)));
    }

    Vector<Complex> result = new ArrayVector<>(N);
    for (int i = 0; i < N; i++) {
      if (i < N/2) {
        result.setElement(i, evens.getElement(i).add(T[i]));
      } else {
        result.setElement(i, evens
                .getElement(i - N/2).sub(T[i - N/2]));
      }
    }
    return result;
  }

  private static Vector<Complex> evensCopy(Vector<Complex> vector) {
    int size = vector.getSize() / 2;
    if (vector.getSize() % 2 != 0) {
      size++;
    }
    Vector<Complex> copy = new ArrayVector<>(size);
    int count = 0;
    for (int i = 0; i < vector.getSize(); i+= 2) {
      copy.setElement(count, vector.getElement(i));
      count++;
    }
    if (count != copy.getSize()) {
      System.err.println("ERRRORREVEN");
    }
    return copy;
  }

  private static Vector<Complex> oddsCopy(Vector<Complex> vector) {
    int size = vector.getSize() / 2;
    if (vector.getSize() % 2 != 0) {
      size++;
    }
    Vector<Complex> copy = new ArrayVector<>(size);
    int count = 0;
    for (int i = 1; i < vector.getSize(); i+= 2) {
      copy.setElement(count, vector.getElement(i));
      count++;
    }
    if (count != copy.getSize()) {
      System.err.println("ERRRORROODDS");
    }
    return copy;
  }
}
